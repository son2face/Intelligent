import Manager.Entity.DatabaseEntity;
import org.glassfish.jersey.server.ResourceConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;

import static java.sql.DriverManager.*;

public class InitClass extends ResourceConfig implements ServletContextListener {

    public InitClass() {
        register(new ApplicationBinder());
        packages(true);
    }

    public void contextInitialized(ServletContextEvent arg0) {
        DatabaseEntity.setFileDir("database.txt");
        DatabaseEntity.loadData();
//        File file = new File(Tools.FullPath + GetPDF.LOCAL_URL);
//        System.out.println(file.getPath());
//        System.out.println(file.getAbsolutePath());
//        DiemThi.makeData(file);
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        try {
            DatabaseEntity.saveData();
            Enumeration<Driver> drivers = getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    deregisterDriver(driver);
                } catch (SQLException e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}