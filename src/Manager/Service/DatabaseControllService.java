package Manager.Service;


import Manager.Entity.DatabaseEntity;
import Manager.Interface.IDatabaseControllService;
import Manager.Model.DatabaseModel;
import Module.Edge.EdgeModel;
import Module.File.FileModel;
import Module.Point.PointModel;
import Module.Problem.ProblemModel;
import Module.Shape.ShapeModel;
import Module.User.UserModel;
import org.hibernate.cfg.Configuration;

/**
 * Created by Son on 5/12/2017.
 */

public class DatabaseControllService  implements IDatabaseControllService {
    public boolean setActive(int id) {
        if (id >= 0 && id < DatabaseEntity.getDatabaseModels().size()) {
            DatabaseEntity.setActive(id);
            return true;
        }
        return false;
    }

    public Configuration createConfiguration(DatabaseModel databaseModel) {
        Configuration cfg;
        switch (databaseModel.typeDB) {
            case 0:
                cfg = new Configuration()
                        .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                        .setProperty("hibernate.connection.url", "jdbc:mysql://" + databaseModel.url + "/" + databaseModel.databaseName)
                        .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                        .setProperty("hibernate.connection.username", databaseModel.userName)
                        .setProperty("hibernate.connection.password", databaseModel.passWord)
                        .addAnnotatedClass(EdgeModel.class)
                        .addAnnotatedClass(FileModel.class)
                        .addAnnotatedClass(PointModel.class)
                        .addAnnotatedClass(ProblemModel.class)
                        .addAnnotatedClass(ShapeModel.class)
                        .addAnnotatedClass(UserModel.class);
                break;
            default:
                cfg = new Configuration()
                        .setProperty("hibernate.connection.driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver")
                        .setProperty("hibernate.connection.url", "jdbc:sqlserver:://" + databaseModel.url + "/" + databaseModel.databaseName)
                        .setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect")
                        .setProperty("hibernate.connection.username", databaseModel.userName)
                        .setProperty("hibernate.connection.password", databaseModel.passWord)
                        .addAnnotatedClass(EdgeModel.class)
                        .addAnnotatedClass(FileModel.class)
                        .addAnnotatedClass(PointModel.class)
                        .addAnnotatedClass(ProblemModel.class)
                        .addAnnotatedClass(ShapeModel.class)
                        .addAnnotatedClass(UserModel.class);
                break;
        }
        return cfg;
    }
}
