package isdcm.api.repositories;

import isdcm.api.exceptions.VideoConflictException;
import isdcm.api.exceptions.VideoConflictException.VideoConflictError;
import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.exceptions.UsuarioException;
import isdcm.api.exceptions.VideoException;
import isdcm.api.exceptions.VideoException.VideoError;
import isdcm.api.exceptions.VideoNotFoundException;
import isdcm.api.mappers.VideoMapper;
import isdcm.api.models.Video;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class VideoRepository {
    private static VideoRepository instance;
    
    public static VideoRepository GetInstance() {
        if (instance == null) {
            instance = new VideoRepository();
        }
        return instance;
    }
    
    private final String url;
    private final VideoMapper videoMapper;
    
    private VideoRepository() {
        url = "jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2";
        videoMapper = VideoMapper.GetInstance();
    }
    
    public Video insert(Video video) throws VideoConflictException, SystemErrorException {
        String autor = video.getAutor().getUsername();
        Timestamp fechaCreacion = Timestamp.valueOf(video.getFechaCreacion());
        Time duracion = Time.valueOf(video.getDuracion());
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "INSERT INTO videos(titulo, autor, fecha_creacion, duracion, descripcion, formato, encrypted)" +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = c.prepareStatement(q, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, video.getTitulo());
            ps.setString(2, autor);
            ps.setTimestamp(3, fechaCreacion);
            ps.setTime(4, duracion);
            ps.setString(5, video.getDescripcion());
            ps.setString(6, video.getFormato());
            ps.setBoolean(7, video.getEncriptado());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                video.setId(id);
            }
        } catch (SQLException e) {
            int error = Integer.parseInt(e.getSQLState());
            System.out.println("SQLState: " + error);
            if (error == 23505) {
                throw new VideoConflictException(VideoConflictError.EXISTING_VIDEO, e);
            }
            if (error == 23503) {
                throw new VideoConflictException(VideoConflictError.AUTOR_NOT_EXISTS, e);
            }
            throw new SystemErrorException(e);
        } catch (VideoException e) {
            throw new SystemErrorException(e);
        }
        return video;
    }
    
    public ArrayList<Video> selectAll() throws SystemErrorException {
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "SELECT v.id AS video_id, v.*, u.id AS usuario_id, u.* " +
                       "FROM videos v INNER JOIN usuarios u ON v.autor = u.username " +
                       "ORDER BY v.reproducciones DESC, v.fecha_creacion DESC";
            PreparedStatement ps = c.prepareStatement(q);
            ResultSet rs = ps.executeQuery();
            return videoMapper.toModels(rs);
        } catch (SQLException | VideoException | UsuarioException e) {
            throw new SystemErrorException(e);
        }
    }
    
    public ArrayList<Video> selectByQuery(String query) throws SystemErrorException {
        query = query.trim().toLowerCase();
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "SELECT v.id AS video_id, v.*, u.id AS usuario_id, u.* " +
                       "FROM videos v INNER JOIN usuarios u ON v.autor = u.username " +
                       "WHERE LOWER(v.titulo) LIKE ? OR " +
                       "      LOWER(u.username) LIKE ? " +
                       "ORDER BY v.reproducciones DESC, v.fecha_creacion DESC";
            PreparedStatement ps = c.prepareStatement(q);
            String param = "%" + query + "%";
            ps.setString(1, param);
            ps.setString(2, param);
            ResultSet rs = ps.executeQuery();
            return videoMapper.toModels(rs);
        } catch (SQLException | VideoException | UsuarioException e) {
            throw new SystemErrorException(e);
        }
    }
    
    public ArrayList<Video> selectByAdvancedSearch(String titulo, String autor, String fechaCreacion) throws VideoException, SystemErrorException {
        LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(9999, 12, 31, 23, 59);
        if (fechaCreacion != null) {
            fechaCreacion = fechaCreacion.trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate startDate, endDate;
            try {
                switch (fechaCreacion.length()) {
                    case 4:
                        fechaCreacion = "01/01/" + fechaCreacion;
                        startDate = LocalDate.parse(fechaCreacion, formatter);
                        endDate = startDate.withMonth(12).withDayOfMonth(31);
                        break;
                    case 7:
                        fechaCreacion = "01/" + fechaCreacion;
                        startDate = LocalDate.parse(fechaCreacion, formatter);
                        endDate = startDate.plusMonths(1).minusDays(1);
                        break;
                    default:
                        startDate = endDate = LocalDate.parse(fechaCreacion, formatter);
                        break;
                }
            } catch (DateTimeParseException e) {
                throw new VideoException(VideoError.VIDEO_FECHA_CREACION_INVALID, e);
            }
            startDateTime = startDate.atTime(LocalTime.MIN);
            endDateTime = endDate.atTime(LocalTime.MAX);
        }
        return selectByAdvancedSearch(titulo, autor, startDateTime, endDateTime);
    }
    
    public ArrayList<Video> selectByAdvancedSearch(String titulo, String autor, LocalDateTime startDate, LocalDateTime endDate) throws SystemErrorException {
        titulo = titulo != null ? titulo.toLowerCase().trim() : "";
        autor = autor != null ? autor.toLowerCase().trim() : "";
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "SELECT v.id AS video_id, v.*, u.id AS usuario_id, u.* " +
                       "FROM videos v INNER JOIN usuarios u ON v.autor = u.username " +
                       "WHERE LOWER(v.titulo) LIKE ? AND " +
                       "      LOWER(u.username) LIKE ? AND " +
                       "      v.fecha_creacion BETWEEN ? AND ? " +
                       "ORDER BY v.reproducciones DESC, v.fecha_creacion DESC";
            PreparedStatement ps = c.prepareStatement(q);
            ps.setString(1, "%" + titulo + "%");
            ps.setString(2, "%" + autor + "%");
            ps.setTimestamp(3, startTimestamp);
            ps.setTimestamp(4, endTimestamp);
            ResultSet rs = ps.executeQuery();
            return videoMapper.toModels(rs);
        } catch (SQLException | VideoException | UsuarioException e) {
            throw new SystemErrorException(e);
        }
    }
    
    public Video selectById(int id) throws VideoNotFoundException, SystemErrorException {
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "SELECT v.id AS video_id, v.*, u.id AS usuario_id, u.* " +
                       "FROM videos v INNER JOIN usuarios u ON v.autor = u.username " +
                       "WHERE v.id = ?";
            PreparedStatement ps = c.prepareStatement(q);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return videoMapper.toModel(rs);
            } else {
                throw new VideoNotFoundException();
            }
        }  catch (SQLException | VideoException | UsuarioException e) {
            throw new SystemErrorException(e);
        }
    }
    
    public void update(Video video) throws VideoNotFoundException, VideoConflictException, SystemErrorException {
        String autor = video.getAutor().getUsername();
        Timestamp fechaCreacion = Timestamp.valueOf(video.getFechaCreacion());
        Time duracion = Time.valueOf(video.getDuracion());
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "UPDATE videos " +
                       "SET titulo = ?, autor = ?, fecha_creacion = ?, " +
                       "    duracion = ?, reproducciones = ?, descripcion = ?, " +
                       "    formato = ?, encrypted = ? " +
                       "WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(q);
            ps.setString(1, video.getTitulo());
            ps.setString(2, autor);
            ps.setTimestamp(3, fechaCreacion);
            ps.setTime(4, duracion);
            ps.setInt(5, video.getReproducciones());
            ps.setString(6, video.getDescripcion());
            ps.setString(7, video.getFormato());
            ps.setBoolean(8, video.getEncriptado());
            ps.setInt(9, video.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new VideoNotFoundException();
            }
        } catch (SQLException e) {
            int error = Integer.parseInt(e.getSQLState());
            System.out.println("SQLState: " + error);
            if (error == 23505) {
                throw new VideoConflictException(VideoConflictError.EXISTING_VIDEO, e);
            }
            if (error == 23503) {
                throw new VideoConflictException(VideoConflictError.AUTOR_NOT_EXISTS, e);
            }
            throw new SystemErrorException(e);
        }
    }
}
