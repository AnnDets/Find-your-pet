package servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import services.ReportService;
import services.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class WebAppInitializer implements ServletContextListener {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/findyourpet";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "123456";
    private static UserService userService;
    private static ReportService reportService;
    private static final String USER_SERVICE_ATTRIBUTE = "userService";
    private static final String REPORT_SERVICE_ATTRIBUTE = "reportService";
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        try {

            try {
                Class.forName("org.postgresql.Driver");
                System.out.println("PostgreSQL Driver Registered!");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("PostgreSQL JDBC Driver not found!", e);
            }
            Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
            userService = new UserService(connection);
            reportService = new ReportService(connection);
            context.setAttribute(USER_SERVICE_ATTRIBUTE, userService);
            context.setAttribute(REPORT_SERVICE_ATTRIBUTE, reportService);
            System.out.println("UserService инициализирован и доступен через ServletContext.");
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database connection", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Очистка ресурсов (если нужно)
        System.out.println("Приложение завершено.");
    }
}
