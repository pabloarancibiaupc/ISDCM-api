package isdcm.api.resources;

import isdcm.api.dto.VideoCreationDTO;
import isdcm.api.dto.VideoDTO;
import isdcm.api.dto.VideoUpdateDTO;
import isdcm.api.exceptions.ExistingVideoException;
import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.exceptions.UsuarioModelException;
import isdcm.api.exceptions.UsuarioNotFoundException;
import isdcm.api.exceptions.VideoModelException;
import isdcm.api.exceptions.VideoModelException.VideoErrorCode;
import isdcm.api.exceptions.VideoNotFoundException;
import isdcm.api.mappers.VideoMapper;
import isdcm.api.models.Usuario;
import isdcm.api.models.Video;
import isdcm.api.repositories.UsuarioRepository;
import isdcm.api.repositories.VideoRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

@Path("videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VideoResource {
    
    VideoRepository videoRepo;
    UsuarioRepository usuarioRepo;
    VideoMapper videoMapper;
    
    public VideoResource() {
        videoRepo = VideoRepository.GetInstance();
        usuarioRepo = UsuarioRepository.GetInstance();
        videoMapper = VideoMapper.GetInstance();
    }
    
    @GET
    public Response get(@QueryParam("query") String query,
                        @QueryParam("titulo") String titulo,
                        @QueryParam("autor") String autor,
                        @QueryParam("fechaCreacion") String fechaCreacion
    ) {
        ArrayList<Video> videos;
        try {
            if (query != null && !query.isBlank()) {
                videos = videoRepo.readByQuery(query);
                videos = Video.SortByQuery(videos, query);
            } else if ((titulo != null && !titulo.isBlank()) || (autor != null && !autor.isBlank()) || (fechaCreacion != null && !fechaCreacion.isBlank())) {
                LocalDateTime[] dateRange;
                if (fechaCreacion != null) {
                    dateRange = parseFechaCreacionParam(fechaCreacion);
                } else {
                    dateRange = new LocalDateTime[] {
                        LocalDateTime.of(2000, 1, 1, 0,0),
                        LocalDateTime.of(9999, 12, 31, 23, 59)
                    };
                }
                videos = videoRepo.readByAdvancedSearch(titulo, autor, dateRange[0], dateRange[1]);
                if ((titulo != null && !titulo.isBlank()) || (autor != null && !autor.isBlank())) {
                    videos = Video.SortByAdvancedSearch(videos, titulo, autor);
                }
            } else {
                videos = videoRepo.readAll();
            }
            ArrayList<VideoDTO> dtos = videoMapper.toDTOs(videos);
            return Response.status(Status.OK).entity(dtos).build();
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            String message = VideoErrorCode.VIDEO_DURACION_INVALID.toString();
            return Response.status(Status.BAD_REQUEST).entity(message).build();
        } catch (SystemErrorException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    public Response post(VideoCreationDTO dtoReq) {
        try {
            Video videoReq = videoMapper.toModel(dtoReq);
            Video videoRes = videoRepo.create(videoReq);
            String username = videoRes.getAutor().getUsername();
            Usuario autor = usuarioRepo.readByUsername(username);
            videoRes.setAutor(autor);
            VideoDTO dtoRes = videoMapper.toDTO(videoRes);
            return Response.status(Status.CREATED).entity(dtoRes).build();
        } catch (VideoModelException | UsuarioModelException e) {
            String msg = e.getMessage();
            System.out.println(msg);
            return Response.status(Status.BAD_REQUEST).entity(msg).build();
        } catch (ExistingVideoException e) {
            String msg = e.getMessage();
            System.out.println(msg);
            return Response.status(Status.CONFLICT).entity(msg).build();
        } catch (SystemErrorException | UsuarioNotFoundException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GET
    @Path("{id: [0-9]+}")
    public Response getById(@PathParam("id") String idParam) {
        int id = Integer.parseInt(idParam);
        try {
            Video video = videoRepo.readById(id);
            VideoDTO dto = videoMapper.toDTO(video);
            return Response.status(Status.OK).entity(dto).build();
        } catch (VideoNotFoundException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.NOT_FOUND).build();
        } catch (SystemErrorException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
    
    @PUT
    @Path("{id: [0-9]+}")
    public Response putById(@PathParam("id") String idParam, VideoUpdateDTO dto) {
        int id = Integer.parseInt(idParam);
        try {
            Video video = videoMapper.toModel(dto);
            video.setId(id);
            videoRepo.update(video);
            return Response.status(Status.NO_CONTENT).build();
        } catch (VideoModelException | UsuarioModelException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (VideoNotFoundException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.NOT_FOUND).build();
        } catch (ExistingVideoException e) {
            String msg = e.getMessage();
            System.out.println(msg);
            return Response.status(Status.CONFLICT).entity(msg).build();
        } catch (SystemErrorException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
    
    private LocalDateTime[] parseFechaCreacionParam(String fechaCreacionParam) throws DateTimeParseException {
        LocalDate startDate, endDate;
        String fechaCreacion = fechaCreacionParam.trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        switch (fechaCreacionParam.length()) {
            case 4:
                fechaCreacion = "01/01/" + fechaCreacion;
                startDate = LocalDate.parse(fechaCreacion, formatter);
                endDate = startDate.withMonth(12).withDayOfMonth(31);
                break;
            case 7:
                fechaCreacion = "01/" + fechaCreacion;
                startDate = LocalDate.parse(fechaCreacion, formatter);
                endDate = startDate.plusMonths(1).minusDays(1);
                break;
            default:
                startDate = endDate = LocalDate.parse(fechaCreacionParam, formatter);
                break;
        }
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return new LocalDateTime[] {startDateTime, endDateTime};
    }
}
