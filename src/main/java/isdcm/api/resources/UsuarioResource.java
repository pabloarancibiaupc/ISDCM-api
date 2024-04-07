package isdcm.api.resources;

import isdcm.api.dto.LoginDTO;
import isdcm.api.dto.UsuarioDTO;
import isdcm.api.exceptions.ExistingUsuarioException;
import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.exceptions.UsuarioModelException;
import isdcm.api.exceptions.UsuarioNotFoundException;
import isdcm.api.mappers.UsuarioMapper;
import isdcm.api.models.Usuario;
import isdcm.api.repositories.UsuarioRepository;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
        Usuario usuarioRes;
        try {
            Usuario usuarioReq = usuarioMapper.toModel(dtoReq);
            usuarioRes = usuarioRepo.create(usuarioReq);
        } catch (UsuarioModelException e) {
            String msg = e.getMessage();
            System.out.println(msg);
            return Response.status(Status.BAD_REQUEST).entity(msg).build();
        } catch (ExistingUsuarioException e) {
            String msg = e.getMessage();
            System.out.println(msg);
            return Response.status(Status.CONFLICT).entity(msg).build();
        } catch (SystemErrorException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        UsuarioDTO dtoRes = usuarioMapper.toDTO(usuarioRes);
        return Response.status(Response.Status.CREATED).entity(dtoRes).build();
    }
    
    @POST
    @Path("login")
    public Response login(LoginDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        Usuario usuario;
        try {
            usuario = usuarioRepo.readByUsernameAndPassword(username, password);
        } catch (UsuarioNotFoundException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.UNAUTHORIZED).build();
        } catch (SystemErrorException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        UsuarioDTO dtoRes = usuarioMapper.toDTO(usuario);
        return Response.status(Response.Status.OK).entity(dtoRes).build();
    }
}
