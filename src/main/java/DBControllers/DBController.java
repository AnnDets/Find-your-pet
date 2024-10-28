package DBControllers;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import models.*;
import utils.LogUtil;

public class DBController implements AutoCloseable {
    private Connection connection_;
    final private String jdbcUrl_ = "jdbc:postgresql://localhost:5432/findyourpet";
    String username_ = "postgres";
    String password_ = "123456";
    private static final Logger logger_ = Logger.getLogger(DBController.class.getName());

    public DBController() throws SQLException {
        try {
            connect();
        } catch (SQLException e) {
            LogUtil.error("Ошибка подключения к базе данных: ", e);
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
        LogUtil.info("Соединение с базой данных установлено.");
    }
    private void closeConnection(){
        try {
            if (connection_ != null) {
                connection_.close();
                LogUtil.info("Соединение с базой данных закрыто.");
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при закрытии соединения", e);
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
            LogUtil.info("Пользователь добавлен: " + name);
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении пользователя: ", e);
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
            LogUtil.error("Ошибка при получении пароля: ", e);
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
            LogUtil.info("Пользователь обновлен: " + name);
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при обновлении пользователя: ", e);
            return false;
        }
    }

    // Удаление пользователя
    public boolean deleteUserFromDB(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate(); // Получаем количество затронутых строк
            if (rowsAffected > 0) {
                LogUtil.info("Пользователь удален с ID: " + userId);
                return true;
            } else {
                LogUtil.warn("Пользователь с ID " + userId + " не найден.");
                return false; // Возвращаем false, если пользователь не найден
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при удалении пользователя: ", e);
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
            LogUtil.error("Ошибка при получении пользователя: ", e);
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
            LogUtil.error("Ошибка при получении пользователя: ", e);
        }
        return users;
    }
    // Добавление новой заявки о пропавшем животном
    public void addReport(int userId, String breed, String description, String foundDate, String location, String status, String[] colors, String[] specialMarks, String[] photos) {
        String query = "INSERT INTO found_id (user_id, breed, description, found_date, location, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection_.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, Integer.toString(userId));
            stmt.setString(2, breed);
            stmt.setString(3, description);
            stmt.setString(4, foundDate);
            stmt.setString(5, location);
            stmt.setString(6, status);
            stmt.executeUpdate();


            // Получаем сгенерированный ID заявки
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long reportId = generatedKeys.getLong(1);
                addReportColors(reportId, colors);
                addReportMarks(reportId, specialMarks);
                addReportPhotos(reportId, photos);
            }
            LogUtil.info("Заявка добавлена успешно");
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении заявки о пропавшем животном", e);
        }
    }

    // Добавление цветов к заявке
    private void addReportColors(long reportId, String[] colors) {
        String query = "INSERT INTO report_colors (report_id, special_mark) VALUES (?,?)";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            for (String color : colors) {
                stmt.setLong(1, reportId);
                stmt.setString(2, color);
                stmt.executeUpdate();
            }
            LogUtil.info("Цвета добавлены к заявке: " + reportId);
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении цветов к заявке", e);
        }
    }
    // Добавление фотографий к заявке
    private void addReportPhotos(long reportId, String[] photos) {
        String query = "INSERT INTO report_photos (report_id, photo_url) VALUES (?, ?)";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            for (String photo : photos) {
                stmt.setLong(1, reportId);
                stmt.setString(2, photo);
                stmt.executeUpdate();
            }
            LogUtil.info("Фотографии добавлены к заявке: " + reportId);
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении фотографий к заявке", e);
        }
    }
    // Добавление особых примет к заявке
    private void addReportMarks(long reportId, String[] marks) {
        String query = "INSERT INTO report_marks (report_id, special_mark) VALUES (?,?)";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            for (String mark : marks) {
                stmt.setLong(1, reportId);
                stmt.setString(2, mark);
                stmt.executeUpdate();
            }
            LogUtil.info("Особые приметы добавлены к заявке: " + reportId);
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении особых примет к заявке", e);
        }
    }

    // Получение заявки по ID с цветами и особями
    public Report getReportById(int reportId) {
        Report report = null;
        String query = "SELECT * FROM found_pets WHERE found_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String breed = rs.getString("breed");
                String description = rs.getString("description");
                String foundDate = rs.getString("found_date");
                String location = rs.getString("location");
                String status = rs.getString("status");

                ArrayList<String> colors = getReportColors(reportId);
                ArrayList<String> marks = getReportMarks(reportId);
                ArrayList<String> photos = getReportPhotos(reportId);

                report = new Report(reportId, getUserFromDB(userId), colors, marks,photos, breed, description, foundDate, location, status);
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении данных о заявке", e);
        }
        return report;
    }

    // Получение цветов заявки
    private ArrayList<String> getReportColors(int reportId) throws SQLException {
        ArrayList<String> colors = new ArrayList<>();
        String query = "SELECT c.name FROM report_colors rc JOIN colors c ON rc.color_id = c.id WHERE rc.report_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                colors.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении цветов заявки", e);
            throw e;
        }
        return colors;
    }

    // Получение фото заявки
    private ArrayList<String> getReportPhotos(int reportId) {
        ArrayList<String> photos = new ArrayList<>();
        String query = "SELECT photo_url FROM report_photos WHERE report_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                photos.add(rs.getString("photo_url"));
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении фото заявки", e);
        }
        return photos;
    }


    // Получение особых примет заявки
    private ArrayList<String> getReportMarks(int reportId) {
        ArrayList<String> marks = new ArrayList<>();
        String query = "SELECT m.name FROM report_marks rm JOIN marks m ON rm.mark_id = m.id WHERE rm.found_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, Integer.toString(reportId));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                marks.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            LogUtil.debug("Ошибка при получении особых примет заявки: " + e.getMessage());
        }
        LogUtil.debug("особые приметы заявки c ID="+reportId+": "+ marks.toString()+"\n");
        return marks;
    }

    public ArrayList<Report> getAllReports() {
        ArrayList<Report> reports = new ArrayList<>();
        String query = "SELECT * FROM found_pets";

        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int reportId = rs.getInt("found_id");
                int userId = rs.getInt("user_id");
                String breed = rs.getString("breed");
                String description = rs.getString("description");
                String foundDate = rs.getString("found_date");
                String location = rs.getString("location");
                String status = rs.getString("status");

                ArrayList<String> colors = getReportColors(reportId);
                ArrayList<String> marks = getReportMarks(reportId);
                ArrayList<String> photos = getReportPhotos(reportId);
                Report report = new Report(
                        reportId,
                        getUserFromDB(userId),
                        colors,
                        marks,
                        photos,
                        breed,
                        description,
                        foundDate,
                        location,
                        status
                );
                reports.add(report);
            }
            LogUtil.info("Получены все заявки");
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении заявок", e);
        }
        return reports;
    }

    public ArrayList<Report> getReportsByFilters(String color, String specialMark, String location, String breed) {
        ArrayList<Report> reports = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM found_pets fp ");
        query.append("LEFT JOIN report_colors rc ON fp.found_id = rc.found_id ");
        query.append("LEFT JOIN report_marks rm ON fp.found_id = rm.found_id WHERE 1=1 ");

        // Добавляем фильтры, если параметры не пустые
        if (color != null && !color.isEmpty()) {
            query.append("AND rc.color_name = ? ");
        }
        if (specialMark != null && !specialMark.isEmpty()) {
            query.append("AND rm.mark_name = ? ");
        }
        if (location != null && !location.isEmpty()) {
            query.append("AND fp.location = ? ");
        }
        if (breed != null && !breed.isEmpty()) {
            query.append("AND fp.breed = ? ");
        }

        try (PreparedStatement stmt = connection_.prepareStatement(query.toString())) {
            int paramIndex = 1;
            if (color != null && !color.isEmpty()) {
                stmt.setString(paramIndex++, color);
            }
            if (specialMark != null && !specialMark.isEmpty()) {
                stmt.setString(paramIndex++, specialMark);
            }
            if (location != null && !location.isEmpty()) {
                stmt.setString(paramIndex++, location);
            }
            if (breed != null && !breed.isEmpty()) {
                stmt.setString(paramIndex++, breed);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Report report = new Report(
                        rs.getInt("found_id"),
                        getUserFromDB(rs.getInt("user_id")),
                        getReportColors(rs.getInt("found_id")),
                        getReportMarks(rs.getInt("found_id")),
                        getReportPhotos(rs.getInt("found_id")),
                        rs.getString("breed"),
                        rs.getString("description"),
                        rs.getString("found_date"),
                        rs.getString("location"),
                        rs.getString("status")
                );
                reports.add(report);
            }
            LogUtil.info("Получены заявки по фильтрам");
        } catch (SQLException e){
            LogUtil.error("Ошибка при получении заявок", e);
        }
        return reports;
    }


    @Override
    public void close() {
        if (connection_ != null) {
            try {
                connection_.close();
                LogUtil.info("Соединение с базой данных закрыто");
            } catch (SQLException e) {
                LogUtil.fatal("Ошибка при закрытии соединения: ", e);
            }
        }
    }




}
