package utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JSONParser {
    Connection connection_;
    Logger logger_;
    public String getLostPetsData(ArrayList<Integer> lostIds) {
        JSONArray jsonArray = new JSONArray();

        if (lostIds.isEmpty()) {
            return jsonArray.toString(); // Возвращаем пустой JSON массив, если нет lost_id
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM lost_pets WHERE lost_id IN (");

        for (int i = 0; i < lostIds.size(); i++) {
            queryBuilder.append("?");
            if (i < lostIds.size() - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");

        String query = queryBuilder.toString();


        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            for (int i = 0; i < lostIds.size(); i++) {
                stmt.setInt(i + 1, lostIds.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("lost_id", rs.getInt("lost_id"));
                    jsonObject.put("pet_id", rs.getInt("pet_id"));
                    jsonObject.put("name", rs.getString("name"));
                    jsonObject.put("species", rs.getString("species"));
                    jsonObject.put("bread", rs.getString("bread"));
                    jsonObject.put("age", rs.getInt("age"));
                    jsonObject.put("gender", rs.getString("gender"));
                    // Добавьте другие поля по необходимости

                    jsonArray.put(jsonObject);
                }
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при получении данных о пропавших животных", e);
        }

        return jsonArray.toString(); // Возвращаем JSON строку
    }
}
