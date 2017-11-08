import Manager.Interface.IDatabaseControllService;
import Manager.Interface.IDatabaseService;
import Manager.Service.DatabaseControllService;
import Manager.Service.DatabaseService;
import Module.Edge.EdgeService;
import Module.File.FileService;
import Module.Point.PointService;
import Module.Problem.ProblemService;
import Module.Shape.ShapeService;
import Module.User.UserService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(new DatabaseService()).to(IDatabaseService.class);
        bind(new DatabaseControllService()).to(IDatabaseControllService.class);
        bind(new EdgeService()).to(EdgeService.class);
        bind(new FileService()).to(FileService.class);
        bind(new ProblemService(new FileService(), new ShapeService(), new EdgeService())).to(ProblemService.class);
        bind(new ShapeService()).to(ShapeService.class);
        bind(new UserService()).to(UserService.class);
        bind(new PointService()).to(PointService.class);
    }
}