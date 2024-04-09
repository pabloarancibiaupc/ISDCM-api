package isdcm.api.mappers;

import isdcm.api.dto.UsuarioDTO;
import isdcm.api.dto.VideoCreationDTO;
import isdcm.api.dto.VideoDTO;
import isdcm.api.dto.VideoUpdateDTO;
import isdcm.api.exceptions.UsuarioException;
import isdcm.api.exceptions.VideoException;
import isdcm.api.models.Usuario;
import isdcm.api.models.Video;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class VideoMapper {
    private static VideoMapper instance;
    
    public static VideoMapper GetInstance() {
        if (instance == null) {
            instance = new VideoMapper();
        }
        return instance;
    }
    
    private final UsuarioMapper usuarioMapper;
    
    private VideoMapper() {
        usuarioMapper = UsuarioMapper.GetInstance();
    }
    
    public ArrayList<VideoDTO> toDTOs(ArrayList<Video> videos) {
        ArrayList<VideoDTO> dtos = new ArrayList<>();
        for (Video video : videos) {
            VideoDTO dto = toDTO(video);
            dtos.add(dto);
        }
        return dtos;
    }
    
    public VideoDTO toDTO(Video video) {
        Usuario autor = video.getAutor();
        UsuarioDTO autorDTO = usuarioMapper.toDTO(autor);
        String fechaCreacion = video.getFechaCreacion().toString();
        String duracion = video.getDuracion().toString();
        
        VideoDTO dto = new VideoDTO();
        dto.setId(video.getId());
        dto.setTitulo(video.getTitulo());
        dto.setAutor(autorDTO);
        dto.setFechaCreacion(fechaCreacion);
        dto.setDuracion(duracion);
        dto.setReproducciones(video.getReproducciones());
        dto.setDescripcion(video.getDescripcion());
        dto.setFormato(video.getFormato());
        return dto;
    }
    
    public ArrayList<Video> toModels(ResultSet rs) throws SQLException, UsuarioException, VideoException {
        ArrayList<Video> videos = new ArrayList<>();
        while (rs.next()) {
            Video video = toModel(rs);
            videos.add(video);
        }
        return videos;
    }
    
    public Video toModel(VideoCreationDTO dto) throws UsuarioException, VideoException {
        String titulo = dto.getTitulo();
        Usuario autor = new Usuario(dto.getAutor());
        String duracion = dto.getDuracion();
        String descripcion = dto.getDescripcion();
        String formato = dto.getFormato();
        return new Video(titulo, autor, duracion, descripcion, formato);
    }
    
    public Video toModel(VideoUpdateDTO dto) throws UsuarioException, VideoException {
        String titulo = dto.getTitulo();
        Usuario autor = new Usuario(dto.getAutor());
        String fechaCreacion = dto.getFechaCreacion();
        String duracion = dto.getDuracion();
        int reproducciones = dto.getReproducciones();
        String descripcion = dto.getDescripcion();
        String formato = dto.getFormato();
        return new Video(titulo, autor, fechaCreacion, duracion, reproducciones, descripcion, formato);
    }
    
    public Video toModel(VideoDTO dto) throws UsuarioException, VideoException {
        Integer id = dto.getId();
        String titulo = dto.getTitulo();
        Usuario autor = usuarioMapper.toModel(dto.getAutor());
        String fechaCreacion = dto.getFechaCreacion();
        String duracion = dto.getDuracion();
        int reproducciones = dto.getReproducciones();
        String descripcion = dto.getDescripcion();
        String formato = dto.getFormato();
        return new Video(id, titulo, autor, fechaCreacion, duracion, reproducciones, descripcion, formato);
    }
    
    public Video toModel(ResultSet rs) throws SQLException, UsuarioException, VideoException {
        int videoId = rs.getInt("video_id");
        String titulo = rs.getString("titulo");
        LocalDateTime fechaCreacion = rs.getTimestamp("fecha_creacion").toLocalDateTime();
        LocalTime duracion = rs.getTime("duracion").toLocalTime();
        int reproducciones = rs.getInt("reproducciones");
        String descripcion = rs.getString("descripcion");
        String formato = rs.getString("formato");
        Usuario autor = usuarioMapper.toModel(rs);
        return new Video(videoId, titulo, autor, fechaCreacion, duracion, reproducciones, descripcion, formato);
    }
}
