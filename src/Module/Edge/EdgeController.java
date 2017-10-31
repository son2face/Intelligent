package Module.Edge;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/edges")
public class EdgeController {
    @Inject
    private EdgeService edgeService;

    public EdgeController() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<EdgeEntity> get(@BeanParam SearchEdgeModel searchEdgeModel) {
        return edgeService.get(searchEdgeModel);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Count")
    public int count(@BeanParam SearchEdgeModel searchEdgeModel) {
        return 100;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{edgeId}")
    public EdgeEntity getId(@PathParam("edgeId") int edgeId) {
        return edgeService.get(edgeId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public EdgeEntity create(EdgeEntity edgeEntity) {
        return edgeService.create(edgeEntity);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{edgeId}")
    public EdgeEntity update(@PathParam("edgeId") int edgeId, EdgeEntity edgeEntity) {
        return edgeService.update(edgeId, edgeEntity);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{edgeId}")
    public void delete(@PathParam("edgeId") int edgeId) {
        edgeService.delete(edgeId);
    }

}