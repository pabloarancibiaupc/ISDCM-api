package isdcm.api.repositories;

import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.exceptions.VideoNotFoundException;
import isdcm.api.mappers.VideoMapper;
import isdcm.api.models.Video;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class VideoRepository {
    private static VideoRepository instance;
    
    public static VideoRepository GetInstance() {
        if (instance == null) {
            instance = new VideoRepository();
        }
        return instance;
    }
    
    String url;
    VideoMapper videoMapper;
    
    private VideoRepository() {
        url = "jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2";
        videoMapper = VideoMapper.GetInstance();
    }
    
    public Video readById(int id) throws VideoNotFoundException, SystemErrorException {
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
        } catch (SQLException e) {
            System.out.println(e);
            throw new SystemErrorException(e);
        }
    }
    
    public ArrayList<Video> getAll() throws SystemErrorException {
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "SELECT v.id AS video_id, v.*, u.id AS usuario_id, u.* " +
                       "FROM videos v INNER JOIN usuarios u ON v.autor = u.username " +
                       "ORDER BY v.reproducciones DESC, v.fecha_creacion DESC";
            PreparedStatement ps = c.prepareStatement(q);
            ResultSet rs = ps.executeQuery();
            return videoMapper.toModels(rs);
        } catch (SQLException e) {
            System.out.println(e);
            throw new SystemErrorException(e);
        }
    }
    
    public ArrayList<Video> getAllByQuery(String query) throws SystemErrorException {
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
        } catch (SQLException e) {
            System.out.println(e);
            throw new SystemErrorException(e);
        }
    }
    
    public ArrayList<Video> getAllByAdvancedSearch(String titulo, String autor, LocalDateTime startDate, LocalDateTime endDate) throws SystemErrorException {
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
        } catch (SQLException e) {
            System.out.println(e);
            throw new SystemErrorException(e);
        }
    }
}
