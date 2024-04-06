package isdcm.api.mappers;

import isdcm.api.dto.UsuarioDTO;
import isdcm.api.dto.VideoDTO;
import isdcm.api.models.Usuario;
import isdcm.api.models.Video;

public class VideoMapper {
    private static VideoMapper instance;
    
    public static VideoMapper GetInstance() {
        if (instance == null) {
            instance = new VideoMapper();
        }
        return instance;
    }
    
    private VideoMapper() { }
    
    public VideoDTO toDTO(Video video) {
        Usuario autor = video.getAutor();
        UsuarioDTO autorDTO = UsuarioMapper.GetInstance().toDTO(autor);
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
}
