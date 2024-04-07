package isdcm.api.resources;

import isdcm.api.dto.UsuarioDTO;
import isdcm.api.exceptions.ExistingUserException;
import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.mappers.UsuarioMapper;
import isdcm.api.models.Usuario;
import isdcm.api.repositories.UsuarioRepository;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {
    
    UsuarioRepository usuarioRepo;
    UsuarioMapper usuarioMapper;
    
    public UsuarioResource() {
        usuarioRepo = UsuarioRepository.GetInstance();
        usuarioMapper = UsuarioMapper.GetInstance();
    }
    
    @POST
    public Response post(UsuarioDTO dtoReq) {
        // TODO: Comprobar camps
        Usuario usuarioReq = usuarioMapper.toModel(dtoReq);
        Usuario usuarioRes;
        try {
            usuarioRes = usuarioRepo.create(usuarioReq);
        } catch (ExistingUserException e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                           .entity("EXISTING_USER")
                           .build();
        } catch (SystemErrorException e) {
            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("INTERNAL_SERVER_ERROR")
                           .build();
        }
        UsuarioDTO dtoRes = usuarioMapper.toDTO(usuarioRes);
        return Response.status(Response.Status.CREATED)
                       .entity(dtoRes)
                       .build();
    }
}
