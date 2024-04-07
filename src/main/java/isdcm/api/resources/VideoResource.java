package isdcm.api.resources;

import isdcm.api.dto.VideoDTO;
import isdcm.api.exceptions.SystemErrorException;
import isdcm.api.exceptions.VideoNotFoundException;
import isdcm.api.mappers.VideoMapper;
import isdcm.api.models.Video;
import isdcm.api.repositories.VideoRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
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
    VideoMapper videoMapper;
    
    public VideoResource() {
        videoRepo = VideoRepository.GetInstance();
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
                query = query.trim();
                videos = videoRepo.getAllByQuery(query);
                videos = Video.SortByQuery(videos, query);
            } else if ((titulo != null && !titulo.isBlank()) ||
                       (autor != null && !autor.isBlank()) ||
                       (fechaCreacion != null && !fechaCreacion.isBlank())
            ) {
                titulo = titulo != null ? titulo.toLowerCase().trim() : "";
                autor = autor != null ? autor.toLowerCase().trim() : "";
                LocalDateTime[] dateRange;
                if (fechaCreacion != null) {
                    dateRange = parseFechaCreacionParam(fechaCreacion);
                } else {
                    dateRange = new LocalDateTime[] {
                        LocalDateTime.of(2000, 1, 1, 0,0),
                        LocalDateTime.of(9999, 12, 31, 23, 59)
                    };
                }
                videos = videoRepo.getAllByAdvancedSearch(titulo, autor, dateRange[0], dateRange[1]);
                if (!(titulo.isBlank() && autor.isBlank())) {
                    videos = Video.SortByAdvancedSearch(videos, titulo, autor);
                }
            } else {
                videos = videoRepo.getAll();
            }
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            ResourceError error = ResourceError.FECHA_CREACION_MALFORMED;
            return Response.status(error.status).entity(error.message).build();
        } catch (SystemErrorException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        ArrayList<VideoDTO> dtos = videoMapper.toDTOs(videos);
        return Response.status(Status.OK).entity(dtos).build();
    }
    
    @GET
    @Path("{id: [0-9]+}")
    public Response getById(@PathParam("id") String idParam) {
        int id = Integer.parseInt(idParam);
        
        Video video;
        try {
            video = videoRepo.readById(id);
        } catch (VideoNotFoundException e) {
            System.out.println(e.getMessage());
            ResourceError error = ResourceError.VIDEO_NOT_FOUND;
            return Response.status(error.status).entity(error.message).build();
        } catch (SystemErrorException e) {
            System.out.println(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        VideoDTO dto = videoMapper.toDTO(video);
        return Response.status(Status.OK).entity(dto).build();
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
