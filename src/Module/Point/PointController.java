package Module.Point;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/points")
public class PointController {
    @Inject
    private PointService pointService;

    public PointController() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<PointEntity> get(@BeanParam SearchPointModel searchPointModel) {
        return pointService.get(searchPointModel);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Count")
    public int count(@BeanParam SearchPointModel searchPointModel) {
        return 100;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{pointId}")
    public PointEntity getId(@PathParam("pointId") int pointId) {
        return pointService.get(pointId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PointEntity create(PointEntity pointEntity) {
        return pointService.create(pointEntity);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{pointId}")
    public PointEntity update(@PathParam("pointId") int pointId, PointEntity pointEntity) {
        return pointService.update(pointId, pointEntity);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{pointId}")
    public void delete(@PathParam("pointId") int pointId) {
        pointService.delete(pointId);
    }

}