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

public class DatabaseControllService implements IDatabaseControllService {
    public boolean setActive(int id) {
        if (id >= 0 && id < DatabaseEntity.getDatabaseModels().size()) {
            DatabaseEntity.setActive(id);
            return true;
        }
        return false;
    }

    public Configuration createConfiguration(DatabaseModel databaseModel) {
        Configuration cfg;
        if (databaseModel == null) return new Configuration();
        switch (databaseModel.typeDB) {
            case 0:
                cfg = new Configuration()
                        .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                        .setProperty("hibernate.connection.url", "jdbc:mysql://" + databaseModel.url + "/" + databaseModel.databaseName)
                        .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect")
                        .setProperty("hibernate.connection.username", databaseModel.userName)
                        .setProperty("hibernate.connection.password", databaseModel.passWord)
                        .setProperty("hibernate.hbm2ddl.auto", "update")
                        .setProperty("hibernate.connection.autoReconnect", "true")
                        .setProperty("hibernate.connection.verifyServerCertificate", "false")
//                        .setProperty("hibernate.c3p0.min_size","5")
//                        .setProperty("hibernate.c3p0.max_size","50")
//                        .setProperty("hibernate.c3p0.timeout","1")
//                        .setProperty("hibernate.c3p0.max_statements","50")
//                        .setProperty("hibernate.c3p0.idle_test_period","3000")
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
                        .setProperty("hibernate.hbm2ddl.auto", "update")
                        .setProperty("hibernate.id.new_generator_mappings", "false")
                        .setProperty("hibernate.connection.autoReconnect", "true")
                        .setProperty("hibernate.connection.verifyServerCertificate", "false")
//                        .setProperty("hibernate.c3p0.min_size","5")
//                        .setProperty("hibernate.c3p0.max_size","50")
//                        .setProperty("hibernate.c3p0.timeout","1")
//                        .setProperty("hibernate.c3p0.max_statements","50")
//                        .setProperty("hibernate.c3p0.idle_test_period","3000")
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
