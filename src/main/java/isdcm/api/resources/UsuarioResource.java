package isdcm.api.resources;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import isdcm.api.dto.UsuarioCreationDTO;
import isdcm.api.dto.UsuarioDTO;
import isdcm.api.exceptions.UsuarioConflictException;
import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.exceptions.UsuarioException;
import isdcm.api.exceptions.UsuarioNotFoundException;
import isdcm.api.mappers.UsuarioMapper;
import isdcm.api.models.Usuario;
import isdcm.api.repositories.UsuarioRepository;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Path("usuarios")
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {
    
    private final String issuer;
    private final byte[] key;
    private final UsuarioRepository usuarioRepo;
    private final UsuarioMapper usuarioMapper;
    
    public UsuarioResource() {
        issuer = "localhost:8080/api";
        String keyBase64 = "crgvLrH16i32bEGukqrsPapijuANmfilnZS/Jhpecng3DYaVLcBvGV75116FNkjjvl6k2dIQSEDMzC7sA/eT1w==";
        key = Base64.getDecoder().decode(keyBase64);
        usuarioRepo = UsuarioRepository.GetInstance();
        usuarioMapper = UsuarioMapper.GetInstance();
    }
    
    @GET
    public Response get(@QueryParam("username") String username) {
        try {
            Usuario usuario = usuarioRepo.selectByUsername(username);
            UsuarioDTO dto = usuarioMapper.toDTO(usuario);
            return Response.status(Status.ACCEPTED).entity(dto).build();
        } catch (UsuarioNotFoundException e) {
            System.out.println(e);
            return Response.status(Status.NOT_FOUND).build();
        } catch (SystemErrorException e) {
            System.out.println(e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(UsuarioCreationDTO dto) {
        try {
            Usuario usuario = usuarioMapper.toModel(dto);
            usuarioRepo.insert(usuario);
            return Response.status(Response.Status.CREATED).build();
        } catch (UsuarioException e) {
            System.out.println(e);
            String msg = e.getMessage();
            return Response.status(Status.BAD_REQUEST).entity(msg).build();
        } catch (UsuarioConflictException e) {
            System.out.println(e);
            String msg = e.getMessage();
            return Response.status(Status.CONFLICT).entity(msg).build();
        } catch (SystemErrorException e) {
            System.out.println(e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam("username") String username,
                          @FormParam("password") String password) {
        try {
            boolean exists = usuarioRepo.checkByUsernameAndPassword(username, password);
            if (!exists) {
                return Response.status(Status.UNAUTHORIZED).build();
            }
            Date date = new Date();
            LocalDateTime expirationLocalDateTime = LocalDateTime.now().plusHours(1);
            Date expiration = Date.from(expirationLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
            String jwt = Jwts.builder().setHeaderParam("typ", "JWT")
                                    .setIssuer(issuer)
                                    .setSubject(username)
                                    .setAudience(issuer)
                                    .setIssuedAt(date)
                                    .setNotBefore(date)
                                    .setExpiration(expiration)
                                    .setId(UUID.randomUUID().toString())
                                    .signWith(
                                        SignatureAlgorithm.HS512,
                                        key
                                    ).compact();
            System.out.println(jwt);
            return Response.status(Status.ACCEPTED).entity(jwt).build();
        } catch (SystemErrorException e) {
            System.out.println(e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
