package DBControllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PetController {
    final private String folder_ = "src/main/resources/images/";
    final private Connection connection_;
    final private Logger logger_;
    public PetController(Connection connection, Logger logger) {
        connection_ = connection;
        logger_ = logger;
    }

    public void addPet(String name, String species, String breed, int age, String gender) {
        String query = "INSERT INTO pets (name, species, breed, age, gender, chip_number) VALUES ( ?, ?, ?, ?, ?,)";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, name.isEmpty() ? "неизвестно" : name);
            stmt.setString(2, species);
            stmt.setString(3, breed.isEmpty() ? "неизвестно" : breed);
            stmt.setInt(4, age);
            stmt.setString(5, gender.isEmpty() ? "неизвестно" : gender);
            stmt.executeUpdate();
            logger_.info("Питомец добавлен: " + name);
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при добавлении питомца", e);
        }
    }

    public void addPhoto(Integer pet_id, String photo_url){
        String query = "INSERT INTO pet_photos(pet_id, photo_url, uploaded_time) VALUES (?,?,NOW())";
        try (PreparedStatement stmt = connection_.prepareStatement(query)){
            stmt.setString(1, pet_id.toString());
            stmt.setString(2, photo_url);
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при добавлении фото", e);
        }
    }

    public void addColor(Integer pet_id, String color){
        String query = "INSERT INTO pet_colors (pet_id, color) VALUES (?, ?)";
        try (PreparedStatement stmt = connection_.prepareStatement(query)){
            stmt.setString(1, pet_id.toString());
            stmt.setString(2, color);
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при добавлении цвета", e);
        }
    }

    public ArrayList<Integer> filter(String name, String species, String bread, Integer age, String gender) {
        ArrayList<Integer> lostIds = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder("SELECT lost_id FROM lost_pets WHERE pet_id IN (SELECT pet_id FROM some_table WHERE 1=1");

        if (!name.isEmpty()) {
            queryBuilder.append(" AND name = ?");
        }

        if (!species.isEmpty()) {
            queryBuilder.append(" AND species = ?");
        }

        if (!bread.isEmpty()) {
            queryBuilder.append(" AND bread = ?");
        }

        if (age != null) {
            queryBuilder.append(" AND age = ?");
        }

        if (!gender.isEmpty()) {
            queryBuilder.append(" AND gender = ?");
        }

        queryBuilder.append(")");

        String query = queryBuilder.toString();

        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            int index = 1;

            if (!name.isEmpty()) {
                stmt.setString(index++, name);
            }

            if (!species.isEmpty()) {
                stmt.setString(index++, species);
            }

            if (!bread.isEmpty()) {
                stmt.setString(index++, bread);
            }

            if (age != null) {
                stmt.setString(index++, age.toString());
            }

            if (!gender.isEmpty()) {
                stmt.setString(index++, gender);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lostIds.add(rs.getInt("lost_id")); // Предполагаем, что lost_id имеет тип INTEGER
                }
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при фильтрации", e);
        }

        return lostIds;
    }
}
