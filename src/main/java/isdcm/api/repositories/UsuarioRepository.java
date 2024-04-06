package isdcm.api.repositories;

import isdcm.api.exceptions.ExistingUserException;
import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.models.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepository {
    private static UsuarioRepository instance;
    
    public static UsuarioRepository GetInstance() {
        if (instance == null) {
            instance = new UsuarioRepository();
        }
        return instance;
    }
    
    String url;
    
    private UsuarioRepository() {
        url = "jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2";
    }
    
    public Usuario create(Usuario usuario) throws ExistingUserException, SystemErrorException {
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "INSERT INTO usuarios(nombre, apellido, email, username, password) " +
                       "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = c.prepareStatement(q, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getUsername());
            ps.setString(5, usuario.getPassword());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                usuario.setId(id);
            }
        } catch (SQLException e) {
            System.out.println(e);
            if (e.getErrorCode() == 30000) {
                throw new ExistingUserException(e);
            }
            throw new SystemErrorException(e);
        }
        return usuario;
    }
}
