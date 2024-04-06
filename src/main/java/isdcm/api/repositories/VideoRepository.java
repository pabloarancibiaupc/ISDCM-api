package isdcm.api.repositories;

import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.exceptions.VideoNotFoundException;
import isdcm.api.models.Usuario;
import isdcm.api.models.Video;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class VideoRepository {
    private static VideoRepository instance;
    
    public static VideoRepository GetInstance() {
        if (instance == null) {
            instance = new VideoRepository();
        }
        return instance;
    }
    
    String url;
    
    private VideoRepository() {
        url = "jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2";
    }
    
    public Video readById(int id) throws VideoNotFoundException, SystemErrorException {
        Video video;
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "SELECT v.titulo, v.duracion, v.reproducciones, v.descripcion, v.formato, v.fecha_creacion, " +
                       "       u.id AS usuario_id, u.nombre, u.apellido, u.email, u.username " +
                       "FROM videos v INNER JOIN usuarios u ON v.autor = u.username " +
                       "WHERE v.id = ?";
            PreparedStatement ps = c.prepareStatement(q);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int usuarioId = rs.getInt("usuario_id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String username = rs.getString("username");
                Usuario autor = new Usuario(usuarioId, nombre, apellido, email, username);
                
                String titulo = rs.getString("titulo");
                LocalDateTime fechaCreacion = rs.getTimestamp("fecha_creacion").toLocalDateTime();
                LocalTime duracion = rs.getTime("duracion").toLocalTime();
                int reproducciones = rs.getInt("reproducciones");
                String descripcion = rs.getString("descripcion");
                String formato = rs.getString("formato");
                video = new Video(id, titulo, autor, fechaCreacion, duracion, reproducciones, descripcion, formato);
            } else {
                throw new VideoNotFoundException();
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SystemErrorException(e);
        }
        return video;
    }
}
