package Module.Problem;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/problems")
public class ProblemController {
    @Inject
    private ProblemService problemService;

    public ProblemController() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ProblemEntity> get(@BeanParam SearchProblemModel searchProblemModel) {
        return problemService.get(searchProblemModel);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/Count")
    public int count(@BeanParam SearchProblemModel searchProblemModel) {
        return 100;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{problemId}")
    public ProblemEntity getId(@PathParam("problemId") int problemId) {
        return problemService.get(problemId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ProblemEntity create(ProblemEntity problemEntity) {
        return problemService.create(problemEntity);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{problemId}")
    public ProblemEntity update(@PathParam("problemId") int problemId, ProblemEntity problemEntity) {
        return problemService.update(problemId, problemEntity);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{problemId}")
    public void delete(@PathParam("problemId") int problemId) {
        problemService.delete(problemId);
    }

}