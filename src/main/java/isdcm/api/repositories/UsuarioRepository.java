package isdcm.api.repositories;

import isdcm.api.exceptions.UsuarioConflictException;
import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.exceptions.UsuarioConflictException.UsuarioConflictError;
import isdcm.api.exceptions.UsuarioException;
import isdcm.api.exceptions.UsuarioNotFoundException;
import isdcm.api.mappers.UsuarioMapper;
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
    
    private final String url;
    private final UsuarioMapper usuarioMapper;
    
    private UsuarioRepository() {
        url = "jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2";
        usuarioMapper = UsuarioMapper.GetInstance();
    }
    
    public void insert(Usuario usuario) throws UsuarioConflictException, SystemErrorException {
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "INSERT INTO usuarios(nombre, apellido, email, username, password) " +
                       "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = c.prepareStatement(q);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getUsername());
            ps.setString(5, usuario.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            int error = Integer.parseInt(e.getSQLState());
            System.out.println("SQLState: " + error);
            if (error == 23505) {
                throw new UsuarioConflictException(UsuarioConflictError.EXISTING_USUARIO, e);
            }
            throw new SystemErrorException(e);
        }
    }
    
    public Usuario selectByUsername(String username) throws UsuarioNotFoundException, SystemErrorException {
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "SELECT id AS usuario_id, nombre, apellido, email, username " +
                       "FROM usuarios " +
                       "WHERE username = ?";
            PreparedStatement ps = c.prepareStatement(q);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return usuarioMapper.toModel(rs);
            } else {
                throw new UsuarioNotFoundException();
            }
        } catch (SQLException | UsuarioException e) {
            throw new SystemErrorException(e);
        }
    }
    
    public boolean checkByUsernameAndPassword(String username, String password) throws SystemErrorException {
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "SELECT * FROM usuarios " +
                       "WHERE username = ? AND password = ?";
            PreparedStatement ps = c.prepareStatement(q);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new SystemErrorException(e);
        }
    }
}
