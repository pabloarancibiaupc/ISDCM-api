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
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VideoResource {
    
    @GET
    @Path("{id: [0-9]+}")
    public Response get(@PathParam("id") String idParam) {
        int id = Integer.parseInt(idParam);
        try {
            Video video = VideoRepository.GetInstance().readById(id);
            VideoDTO videoDTO = VideoMapper.GetInstance().toDTO(video);
            return Response.ok(videoDTO).build();
        } catch (VideoNotFoundException e) {
            System.out.println(e);
            throw new NotFoundException("VIDEO_NOT_FOUND");
        } catch (SystemErrorException e) {
            System.out.println(e);
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");
        }
    }
}
