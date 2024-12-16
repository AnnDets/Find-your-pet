package dao;

import models.Report;
import models.User;
import utils.LogUtil;

import java.sql.*;
import java.util.ArrayList;

public class ReportDaoPostgres extends ReportDao {
    private final Connection connection;

    public ReportDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Object object) {
        if (!(object instanceof Report report)) {
            LogUtil.error("Передан неверный тип объекта для создания", null);
            return false;
        }

        String query = "INSERT INTO found_pets (user_id, species, breed, description, date_found, location_found, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, report.getUser().getId());
            stmt.setString(2, report.getSpecies());
            stmt.setString(3, report.getBreed());
            stmt.setString(4, report.getDescription());
            stmt.setDate(5, java.sql.Date.valueOf(report.getFoundDate()));
            stmt.setString(6, report.getLocation());
            stmt.setString(7, report.getStatus());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long reportId = generatedKeys.getLong(1);
                report.setId((int) reportId);
                addColors(report, report.getColors().toArray(new String[0]));
                addMarks(report, report.getSpecialMarks().toArray(new String[0]));
                addPhotos(report, report.getPhotos().toArray(new String[0]));
            }
            LogUtil.info("Заявка добавлена успешно");
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении заявки о пропавшем животном", e);
            return false;
        }
    }

    @Override
    public boolean update(Object object) {
        if (!(object instanceof Report report)) {
            LogUtil.error("Передан неверный тип объекта для обновления", null);
            return false;
        }

        String query = "UPDATE found_pets SET breed = ?, description = ?, found_date = ?, location = ?, status = ? WHERE found_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, report.getBreed());
            stmt.setString(2, report.getDescription());
            stmt.setString(3, report.getFoundDate());
            stmt.setString(4, report.getLocation());
            stmt.setString(5, report.getStatus());
            stmt.setInt(6, report.getId());
            stmt.executeUpdate();

            LogUtil.info("Заявка обновлена успешно: id=" + report.getId());
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при обновлении заявки", e);
            return false;
        }
    }

    @Override
    public boolean delete(Object object) {
        if (!(object instanceof Report report)) {
            LogUtil.error("Передан неверный тип объекта для удаления", null);
            return false;
        }

        String query = "DELETE FROM found_pets WHERE found_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, report.getId());
            stmt.executeUpdate();

            LogUtil.info("Заявка удалена успешно: id=" + report.getId());
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при удалении заявки", e);
            return false;
        }
    }

    @Override
    public Object read(Object object) {
        if (!(object instanceof Report report)) {
            LogUtil.error("Передан неверный тип объекта для чтения", null);
            return null;
        }
        return getById(report.getId());
    }

    @Override
    public ArrayList<Object> readAll() {
        ArrayList<Object> reports = new ArrayList<>();
        String query = "SELECT * FROM found_pets";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Report report = mapResultSetToReport(rs);
                reports.add(report);
            }
            LogUtil.info("Получены все заявки");
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении заявок", e);
        }
        return reports;
    }

    private Report mapResultSetToReport(ResultSet rs) throws SQLException {
        int reportId = rs.getInt("found_id");
        int userId = rs.getInt("user_id");
        String species = rs.getString("species");
        String breed = rs.getString("breed");
        String description = rs.getString("description");
        String foundDate = rs.getString("date_found");
        String location = rs.getString("location_found");
        String status = rs.getString("status");

        ArrayList<String> colors = getReportColors(reportId);
        ArrayList<String> marks = getReportMarks(reportId);
        ArrayList<String> photos = getReportPhotos(reportId);

        UserDaoFactory userDaoFactory = UserDaoFactory.getInstance();
        UserDao userDao = userDaoFactory.createUserDao("postgres", connection);
        return new Report(reportId, (User) userDao.readByID(userId),species, colors, marks, photos, breed, description, foundDate, location, status);
    }

    @Override
    public boolean addColors(Report report, String[] colors) {
        String query = "INSERT INTO report_colors (report_id, color_name) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (String color : colors) {
                stmt.setInt(1, report.getId());
                stmt.setString(2, color);
                stmt.addBatch();
            }
            stmt.executeBatch();
            LogUtil.info("Цвета добавлены к заявке: " + report.getId());
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении цветов к заявке", e);
            return false;
        }
    }

    @Override
    public boolean addPhotos(Report report, String[] photos) {
        String query = "INSERT INTO report_photos (report_id, photo_url) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (String photo : photos) {
                stmt.setInt(1, report.getId());
                stmt.setString(2, photo);
                stmt.addBatch();
            }
            stmt.executeBatch();
            LogUtil.info("Фотографии добавлены к заявке: " + report.getId());
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении фотографий к заявке", e);
            return false;
        }
    }

    @Override
    public boolean addMarks(Report report, String[] marks) {
        String query = "INSERT INTO report_marks (report_id, special_mark) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (String mark : marks) {
                stmt.setInt(1, report.getId());
                stmt.setString(2, mark);
                stmt.addBatch();
            }
            stmt.executeBatch();
            LogUtil.info("Особые приметы добавлены к заявке: " + report.getId());
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении особых примет к заявке", e);
            return false;
        }
    }

    @Override
    public Report getById(int reportId) {
        String query = "SELECT * FROM found_pets WHERE found_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToReport(rs);
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении данных о заявке", e);
        }
        return null;
    }

    @Override
    public ArrayList<Report> getReportsByFilters(String color, String specialMark, String location, String breed) {
        return null;
    }

    private ArrayList<String> getReportMarks(int reportId) {
        ArrayList<String> marks = new ArrayList<>();
        String query = "SELECT special_mark FROM report_marks WHERE report_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                marks.add(rs.getString("special_mark"));
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении особых примет заявки", e);
        }
        return marks;
    }

    private ArrayList<String> getReportColors(int reportId) {
        ArrayList<String> colors = new ArrayList<>();
        String query = "SELECT color_name FROM report_colors WHERE report_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                colors.add(rs.getString("color_name"));
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении цветов заявки", e);
        }
        return colors;
    }

    private ArrayList<String> getReportPhotos(int reportId) {
        ArrayList<String> photos = new ArrayList<>();
        String query = "SELECT photo_url FROM report_photos WHERE report_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                photos.add(rs.getString("photo_url"));
            }
            return photos;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении", e);
        }
        return photos;
    }
}