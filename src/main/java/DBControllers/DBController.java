package DBControllers;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import models.*;

public class DBController implements AutoCloseable {
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

    // Добавление пользователя
    public boolean addUserToDB(String name, String email, String phone, String passwordHash, String address) {
        String query = "INSERT INTO users (name, email, phone, password_hash, address) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, passwordHash);
            stmt.setString(5, address);
            stmt.executeUpdate();
            logger_.info("Пользователь добавлен: " + name);
            return true;
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при добавлении пользователя: " + e.getMessage());
            return false;
        }
    }

    // Получение пароля по email
    public String getUserPasswordByEmail(String email) {
        String query = "SELECT password_hash FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password_hash");
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при получении пароля: " + e.getMessage());
        }
        return null;
    }

    // Обновление данных пользователя
    public boolean updateUserInDB(int id, String name, String email, String phone, String plainPassword, String address) {
        String query = "UPDATE users SET name = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            logger_.info("Пользователь обновлен: " + name);
            return true;
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при обновлении пользователя: " + e.getMessage());
            return false;
        }
    }

    // Удаление пользователя
    public boolean deleteUserFromDB(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            logger_.info("Пользователь удален с ID: " + userId);
            return true;
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при удалении пользователя: " + e.getMessage());
            return false;
        }
    }

    // Получение данных пользователя
    public User getUserFromDB(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password_hash"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при получении пользователя: " + e.getMessage());
        }
        return null;
    }
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (PreparedStatement stmt = connection_.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password_hash"),
                        rs.getString("address")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при получении пользователя: " + e.getMessage());
        }
        return users;
    }
    // Добавление новой заявки о пропавшем животном
    public void addReport(int userId, String breed, String description, String foundDate, String location, String status, String[] colors, String[] specialMarks) {
        String query = "INSERT INTO reports (user_id, breed, description, found_date, location, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection_.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, Integer.toString(userId));
            stmt.setString(2, breed);
            stmt.setString(3, description);
            stmt.setString(4, foundDate);
            stmt.setString(5, location);
            stmt.setString(6, status);
            stmt.executeUpdate();
            logger_.info("Заявка добавлена успешно: " + description);

            // Получаем сгенерированный ID заявки
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long reportId = generatedKeys.getLong(1);
                addReportColors(reportId, colors);
                addReportMarks(reportId, specialMarks);
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при добавлении заявки о пропавшем животном", e);
        }
    }

    // Добавление цветов к заявке
    private void addReportColors(long reportId, String[] colors) {
        String query = "INSERT INTO report_colors (report_id, color_id) VALUES (?, (SELECT id FROM colors WHERE name = ?))";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            for (String color : colors) {
                stmt.setLong(1, reportId);
                stmt.setString(2, color);
                stmt.executeUpdate();
            }
            logger_.info("Цвета добавлены к заявке: " + reportId);
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при добавлении цветов к заявке", e);
        }
    }

    // Добавление особых примет к заявке
    private void addReportMarks(long reportId, String[] marks) {
        String query = "INSERT INTO report_marks (report_id, mark_id) VALUES (?, (SELECT id FROM marks WHERE name = ?))";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            for (String mark : marks) {
                stmt.setLong(1, reportId);
                stmt.setString(2, mark);
                stmt.executeUpdate();
            }
            logger_.info("Особые приметы добавлены к заявке: " + reportId);
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при добавлении особых примет к заявке", e);
        }
    }

    // Получение заявки по ID с цветами и особями
    public Report getReportById(int reportId) {
        Report report = null;
        String query = "SELECT * FROM reports WHERE id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, Integer.toString(reportId));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = Integer.parseInt(rs.getString("user_id"));
                String breed = rs.getString("breed");
                String description = rs.getString("description");
                String foundDate = rs.getString("found_date");
                String location = rs.getString("location");
                String status = rs.getString("status");

                ArrayList<String> colors = getReportColors(reportId);
                ArrayList<String> marks = getReportMarks(reportId);

                report = new Report(reportId, getUserFromDB(userId), colors, marks, breed, description, foundDate, location, status);
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при получении данных о заявке", e);
        }
        return report;
    }

    // Получение цветов заявки
    private ArrayList<String> getReportColors(int reportId) throws SQLException {
        ArrayList<String> colors = new ArrayList<>();
        String query = "SELECT c.name FROM report_colors rc JOIN colors c ON rc.color_id = c.id WHERE rc.report_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, Integer.toString(reportId));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                colors.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при получении цветов заявки", e);
            throw e;
        }
        return colors;
    }

    // Получение особых примет заявки
    private ArrayList<String> getReportMarks(int reportId) {
        ArrayList<String> marks = new ArrayList<>();
        String query = "SELECT m.name FROM report_marks rm JOIN marks m ON rm.mark_id = m.id WHERE rm.report_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, Integer.toString(reportId));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                marks.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при получении особых примет заявки", e);
        }
        return marks;
    }
    @Override
    public void close() {
        if (connection_ != null) {
            try {
                connection_.close();
                logger_.info("Соединение с базой данных закрыто");
            } catch (SQLException e) {
                logger_.log(Level.SEVERE, "Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }



}
