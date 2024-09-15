package DBControllers;

import utils.AddUserError;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class DBController implements AutoCloseable  {
    private Connection connection_;
    final private String jdbcUrl_ = "jdbc:postgresql://localhost:5432/findyourpet";
    String username_ = "postgres";
    String password_ = "123456";
    private static final Logger logger_ = Logger.getLogger(DBController.class.getName());
    public DBController() throws SQLException {
        try {
            setupLogger();
        } catch (IOException e){
            System.err.println("Ошибка настройки логгера: " + e.getMessage());
        }
        try {
            connect();
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
    private void setupLogger() throws IOException {
        FileHandler fileHandler = new FileHandler("DBlog.log", true); // true для добавления, а не перезаписи
        fileHandler.setFormatter(new SimpleFormatter()); // Простой формат логов
        logger_.addHandler(fileHandler);
        logger_.setLevel(Level.ALL);
    }

    void connect() throws SQLException {
        connection_ = DriverManager.getConnection(jdbcUrl_, username_, password_);
        logger_.info("Соединение с базой данных установлено.");
    }
    private void closeConnection(){
        try {
            if (connection_ != null) {
                connection_.close();
                logger_.info("Соединение с базой данных закрыто.");
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при закрытии соединения", e);
        }
    }
    public ArrayList<AddUserError> AddUser(String name, String email, String phone, String plainPassword, String address){
        UserController userController = new UserController(connection_, logger_);
        return userController.addUser(name, email, phone, plainPassword, address);
    }

    public void addPet(String name, String species, String breed, int age, String gender, String chipNumber) {
        String query = "INSERT INTO pets (name, species, breed, age, gender, chip_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(2, name);
            stmt.setString(3, species);
            stmt.setString(4, breed);
            stmt.setInt(5, age);
            stmt.setString(6, gender);
            stmt.setString(7, chipNumber);
            stmt.executeUpdate();
            logger_.info("Питомец добавлен: " + name);
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при добавлении питомца", e);
        }
    }

    public boolean authenticateUser(String email, String plainPassword){
        UserController userController = new UserController(connection_, logger_);
        return userController.authenticateUser(email, plainPassword);
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}