package isdcm.api.mappers;

import isdcm.api.dto.UsuarioCreationDTO;
import isdcm.api.dto.UsuarioDTO;
import isdcm.api.exceptions.UsuarioModelException;
import isdcm.api.models.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioMapper {
    private static UsuarioMapper instance;
    
    public static UsuarioMapper GetInstance() {
        if (instance == null) {
            instance = new UsuarioMapper();
        }
        return instance;
    }
    
    private UsuarioMapper() { }
    
    public UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setUsername(usuario.getUsername());
        return dto;
    }
    
    public Usuario toModel(UsuarioDTO dto) throws UsuarioModelException {
        Integer id = dto.getId();
        String nombre = dto.getNombre();
        String apellido = dto.getApellido();
        String email = dto.getEmail();
        String username = dto.getUsername();
        return new Usuario(id, nombre, apellido, email, username);
    }
    
    public Usuario toModel(UsuarioCreationDTO dto) throws UsuarioModelException {
        String nombre = dto.getNombre();
        String apellido = dto.getApellido();
        String email = dto.getEmail();
        String username = dto.getUsername();
        String password = dto.getPassword();
        return new Usuario(nombre, apellido, email, username, password);
    }
    
    public Usuario toModel(ResultSet rs) throws SQLException, UsuarioModelException {
        int usuarioId = rs.getInt("usuario_id");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String email = rs.getString("email");
        String username = rs.getString("username");
        return new Usuario(usuarioId, nombre, apellido, email, username);
    }
}
