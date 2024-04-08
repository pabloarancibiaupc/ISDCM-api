package isdcm.api.repositories;

import isdcm.api.exceptions.ExistingUsuarioException;
import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.exceptions.UsuarioModelException;
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
    
    String url;
    UsuarioMapper usuarioMapper;
    
    private UsuarioRepository() {
        url = "jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2";
        usuarioMapper = UsuarioMapper.GetInstance();
    }
    
    public Usuario create(Usuario usuario) throws ExistingUsuarioException, SystemErrorException {
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
                throw new ExistingUsuarioException(e);
            }
            throw new SystemErrorException(e);
        }  catch (UsuarioModelException e) {
            System.out.println(e.getMessage());
            throw new SystemErrorException(e);
        }
        return usuario;
    }
    
    public Usuario readByUsername(String username) throws UsuarioNotFoundException, SystemErrorException {
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "SELECT id, nombre, apellido, email, username " +
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
        } catch (SQLException | UsuarioModelException e) {
            System.out.println(e.getMessage());
            throw new SystemErrorException(e);
        }
    }
    
    public Usuario readByUsernameAndPassword(String username, String password) throws UsuarioNotFoundException, SystemErrorException {
        try (Connection c = DriverManager.getConnection(url)) {
            String q = "SELECT id, nombre, apellido, email, username " +
                       "FROM usuarios " +
                       "WHERE username = ? AND password = ?";
            PreparedStatement ps = c.prepareStatement(q);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return usuarioMapper.toModel(rs);
            } else {
                throw new UsuarioNotFoundException();
            }
        } catch (SQLException | UsuarioModelException e) {
            System.out.println(e.getMessage());
            throw new SystemErrorException(e);
        }
    }
}
