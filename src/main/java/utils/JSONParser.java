package utils;

import models.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSONParser {

    public static JSONObject parseAddUserErrors(ArrayList<AddUserError> errors) {
        JSONObject result = new JSONObject();

        // Устанавливаем статус в зависимости от наличия ошибок
        result.put("status", errors.isEmpty() ? 1 : 0);

        // Создаем Map для группировки ошибок по классу
        Map<String, JSONArray> errorGroups = new HashMap<>();

        // Обрабатываем ошибки и группируем их по типу
        for (AddUserError error : errors) {
            String errorClassName = error.getClass().getSimpleName();

            // Если класс ошибки еще не добавлен, создаем новый массив для ошибок этого класса
            errorGroups.putIfAbsent(errorClassName, new JSONArray());

            // Добавляем имя ошибки в массив
            errorGroups.get(errorClassName).put(error.name());
        }

        // Преобразуем Map в JSON-массив
        JSONArray errorsArray = new JSONArray();
        for (Map.Entry<String, JSONArray> entry : errorGroups.entrySet()) {
            JSONArray errorDetails = new JSONArray();
            errorDetails.put(entry.getKey());       // имя класса ошибки
            errorDetails.put(entry.getValue());     // массив имен ошибок этого класса

            // Добавляем в основной массив ошибок
            errorsArray.put(errorDetails);
        }

        // Добавляем массив ошибок в JSON-объект
        result.put("errors", errorsArray);
        return result;
    }
    public static JSONObject parseReportErrors(ArrayList<ReportError> errors) {
        JSONObject result = new JSONObject();

        // Устанавливаем статус на основе наличия ошибок
        result.put("status", errors.isEmpty() ? 1 : 0);

        // Создаем Map для группировки ошибок по типу
        Map<String, JSONArray> errorGroups = new HashMap<>();

        // Обрабатываем ошибки и группируем по типу
        for (ReportError error : errors) {
            String errorClassName = error.getClass().getSimpleName();

            // Если класс ошибки еще не добавлен, создаем новый массив для ошибок этого класса
            errorGroups.putIfAbsent(errorClassName, new JSONArray());

            // Добавляем имя ошибки в массив
            errorGroups.get(errorClassName).put(error.getMessage());
        }

        // Преобразуем Map в JSON-массив
        JSONArray errorsArray = new JSONArray();
        for (Map.Entry<String, JSONArray> entry : errorGroups.entrySet()) {
            JSONArray errorDetails = new JSONArray();
            errorDetails.put(entry.getKey());       // имя класса ошибки
            errorDetails.put(entry.getValue());     // массив сообщений ошибок этого класса

            // Добавляем в основной массив ошибок
            errorsArray.put(errorDetails);
        }

        // Добавляем массив ошибок в JSON-объект
        result.put("errors", errorsArray);
        return result;
    }


    public static JSONObject parseUpdateUserErrors(ArrayList<AddUserError> errors) {
        JSONObject result = new JSONObject();

        // Устанавливаем статус в зависимости от наличия ошибок
        result.put("status", errors.isEmpty() ? 1 : 0);

        // Создаем Map для группировки ошибок по классу
        Map<String, JSONArray> errorGroups = new HashMap<>();

        // Обрабатываем ошибки и группируем их по типу
        for (AddUserError error : errors) {
            String errorClassName = error.getClass().getSimpleName();

            // Если класс ошибки еще не добавлен, создаем новый массив для ошибок этого класса
            errorGroups.putIfAbsent(errorClassName, new JSONArray());

            // Добавляем имя ошибки в массив
            errorGroups.get(errorClassName).put(error.name());
        }

        // Преобразуем Map в JSON-массив
        JSONArray errorsArray = new JSONArray();
        for (Map.Entry<String, JSONArray> entry : errorGroups.entrySet()) {
            JSONArray errorDetails = new JSONArray();
            errorDetails.put(entry.getKey());       // имя класса ошибки
            errorDetails.put(entry.getValue());     // массив имен ошибок этого класса

            // Добавляем в основной массив ошибок
            errorsArray.put(errorDetails);
        }

        // Добавляем массив ошибок в JSON-объект
        result.put("errors", errorsArray);
        return result;
    }
    public static JSONObject parseUser(User user) {
        JSONObject userJSON = new JSONObject();

        userJSON.put("id", user.getId());
        userJSON.put("name", user.getName());
        userJSON.put("email", user.getEmail());
        userJSON.put("phone", user.getPhone());
        userJSON.put("password_hash", user.getPasswordHash());
        userJSON.put("address", user.getAddress());

        return userJSON;
    }
    // Преобразование Report в JSON
    public static JSONObject reportToJSON(Report report) {
        JSONObject json = new JSONObject();
        json.put("id", report.getId());
        json.put("user", parseUser(report.getUser()));
        json.put("colors", new JSONArray(report.getColors()));
        json.put("specialMarks", new JSONArray(report.getSpecialMarks()));
        json.put("photos", new JSONArray(report.getPhotos()));
        json.put("breed", report.getBreed());
        json.put("description", report.getDescription());
        json.put("foundDate", report.getFoundDate());
        json.put("location", report.getLocation());
        json.put("status", report.getStatus());
        return json;
    }
    public static JSONObject userToJSON(User user) {
        JSONObject json = new JSONObject();
        json.put("user_id", user.getId());
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("phone", user.getPhone());
        json.put("password_hash", user.getPasswordHash());
        json.put("address", user.getAddress());
        return json;
    }

    // Преобразование JSON в User
    public static User jsonToUser(JSONObject json) {
        String name = json.getString("username");
        String email = json.getString("email");
        String phone = json.getString("phone");
        String password = json.getString("password");
        String address = json.getString("address");

        return new User(0, name, email, phone, "",password, address);
    }
    // Преобразование JSON в Report
    public static Report jsonToReport(JSONObject json) {
        int id = json.getInt("id");
        User user = jsonToUser(json.getJSONObject("user"));
        ArrayList<String> colors = jsonArrayToList(json.getJSONArray("colors"));
        ArrayList<String> specialMarks = jsonArrayToList(json.getJSONArray("specialMarks"));
        ArrayList<String> photos = jsonArrayToList(json.getJSONArray("photos"));
        String breed = json.getString("breed");
        String description = json.getString("description");

        String foundDate = json.getString("foundDate");
        String location = json.getString("location");
        String status = json.getString("status");

        return new Report(id, user, colors, specialMarks,photos, breed, description, foundDate, location, status);
    }
    public static JSONArray serializeReports(ArrayList<Report> reports) {
        JSONArray reportsArray = new JSONArray();

        for (Report report : reports) {
            JSONObject reportJson = new JSONObject();
            reportJson.put("id", report.getId());
            reportJson.put("user", JSONParser.userToJSON(report.getUser()));
            reportJson.put("colors", new JSONArray(report.getColors()));
            reportJson.put("specialMarks", new JSONArray(report.getSpecialMarks()));
            reportJson.put("breed", report.getBreed());
            reportJson.put("description", report.getDescription());
            reportJson.put("foundDate", report.getFoundDate());
            reportJson.put("location", report.getLocation());
            reportJson.put("status", report.getStatus());

            reportsArray.put(reportJson);
        }
        return reportsArray;
    }

    // Преобразование JSONArray в ArrayList<String>
    private static ArrayList<String> jsonArrayToList(JSONArray array) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }
}
