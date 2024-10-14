package utils;

import models.User;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.PasswordErrorType;

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

}
