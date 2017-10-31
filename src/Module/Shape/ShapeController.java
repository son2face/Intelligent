package Module.Shape;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/shapes")
public class ShapeController {
    @Inject
    private ShapeService shapeService;

    public ShapeController() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ShapeEntity> get(@BeanParam SearchShapeModel searchShapeModel) {
        return shapeService.get(searchShapeModel);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Count")
    public int count(@BeanParam SearchShapeModel searchShapeModel) {
        return 100;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{shapeId}")
    public ShapeEntity getId(@PathParam("shapeId") int shapeId) {
        return shapeService.get(shapeId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ShapeEntity create(ShapeEntity shapeEntity) {
        return shapeService.create(shapeEntity);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{shapeId}")
    public ShapeEntity update(@PathParam("shapeId") int shapeId, ShapeEntity shapeEntity) {
        return shapeService.update(shapeId, shapeEntity);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{shapeId}")
    public void delete(@PathParam("shapeId") int shapeId) {
        shapeService.delete(shapeId);
    }

}