package isdcm.api.mappers;

import isdcm.api.dto.UsuarioDTO;
import isdcm.api.models.Usuario;

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
        dto.setPassword(usuario.getPassword());
        return dto;
    }
}
