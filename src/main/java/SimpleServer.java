import DBControllers.DBController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import models.Report;
import org.json.JSONObject;
import services.ReportController;
import services.UserService;
import utils.AddUserError;
import utils.JSONParser;
import utils.ReportError;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/register", new RegisterHandler());
        server.createContext("/updateUser", new UserUpdateHandler());
        server.createContext("/addReport", new ReportAddHandler());
        server.createContext("/getReports", new GetReportsHandler());
        server.createContext("/getFilteredReports", new GetFilteredReportsHandler());
        server.setExecutor(null); // Use default executor
        server.start();
        System.out.println("Server started on port 8080");
    }

    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Получаем тело запроса
                InputStream inputStream = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String requestBody = stringBuilder.toString();
                System.out.println("Request body: " + requestBody);

                // Преобразуем строку в JSON
                JSONObject jsonObject = new JSONObject(requestBody);
                String jdbcUrl_ = "jdbc:postgresql://localhost:5432/findyourpet";
                String username_ = "postgres";
                String password_ = "123456";
                try (Connection connection_ = DriverManager.getConnection(jdbcUrl_, username_, password_)) {
                    UserService userService = new UserService(connection_);

                    // Вызываем метод добавления пользователя
                    ArrayList<AddUserError> errors = userService.addUser(jsonObject.getString("name"), jsonObject.getString("email"), jsonObject.getString("phone"), jsonObject.getString("password"), jsonObject.getString("address"));

                    // Проверяем ошибки
                    if (!errors.isEmpty()) {
                        JSONObject errorResponse = JSONParser.parseAddUserErrors(errors);
                        String jsonResponse = errorResponse.toString();

                        // Отправляем ошибки клиенту
                        exchange.sendResponseHeaders(400, jsonResponse.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(jsonResponse.getBytes());
                        os.close();
                    } else {
                        // Успешная регистрация
                        String response = "User registered successfully!";
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }


    static class ReportAddHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                Report report = JSONParser.jsonToReport(jsonObject);

                ArrayList<ReportError> errors;
                try (DBController dbController = new DBController()) {
                    ReportController reportController = new ReportController(dbController);
                    errors = reportController.addReport(report.getUser().getId(), report.getColors().toArray(new String[0]), report.getColors().toArray(new String[0]), report.getColors().toArray(new String[0]), report.getBreed(), report.getDescription(), report.getFoundDate(), report.getLocation(), report.getStatus());
                } catch (SQLException e) {
                    exchange.sendResponseHeaders(500, -1); // Internal Server Error
                    return;
                }

                if (errors.isEmpty()) {
                    String response = "Report added successfully!";
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                } else {
                    String response = JSONParser.parseReportErrors(errors).toString();
                    exchange.sendResponseHeaders(400, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                }
                exchange.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class UserUpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Получаем тело запроса
                InputStream inputStream = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String requestBody = stringBuilder.toString();
                System.out.println("Request body: " + requestBody);

                // Преобразуем строку в JSON
                JSONObject jsonObject = new JSONObject(requestBody);

                try (DBController dbController = new DBController()) {
                    UserService userService = new UserService(dbController);

                    // Вызываем метод добавления пользователя
                    ArrayList<AddUserError> errors = userService.updateUser(userService.getUserById(jsonObject.getInt("userId")).getId(), jsonObject.getString("name"), jsonObject.getString("email"), jsonObject.getString("phone"), jsonObject.getString("password"), jsonObject.getString("address"));

                    // Проверяем ошибки
                    if (!errors.isEmpty()) {
                        JSONObject errorResponse = JSONParser.parseAddUserErrors(errors);
                        String jsonResponse = errorResponse.toString();

                        // Отправляем ошибки клиенту
                        exchange.sendResponseHeaders(400, jsonResponse.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(jsonResponse.getBytes());
                        os.close();
                    } else {
                        // Успешная регистрация
                        String response = "User update successfully!";
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class GetReportsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                try (DBController dbController = new DBController()) {
                    ReportController reportController = new ReportController(dbController);
                    ArrayList<Report> reports = reportController.getAllReports();

                    String jsonResponse = JSONParser.serializeReports(reports).toString();
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    exchange.getResponseBody().write(jsonResponse.getBytes());
                } catch (SQLException e) {
                    exchange.sendResponseHeaders(500, -1);
                }
                exchange.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class GetFilteredReportsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Извлекаем параметры запроса
                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = parseQuery(query);

                String color = params.getOrDefault("color", null);
                String specialMark = params.getOrDefault("specialMark", null);
                String location = params.getOrDefault("location", null);
                String breed = params.getOrDefault("breed", null);

                try (DBController dbController = new DBController()) {
                    ReportController reportController = new ReportController(dbController);
                    ArrayList<Report> reports = reportController.getReportsByFilters(color, specialMark, location, breed);

                    String jsonResponse = JSONParser.serializeReports(reports).toString();
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    exchange.getResponseBody().write(jsonResponse.getBytes());
                } catch (SQLException e) {
                    exchange.sendResponseHeaders(500, -1);
                }
                exchange.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }

        // Вспомогательный метод для разбора параметров из строки запроса
        private Map<String, String> parseQuery(String query) {
            Map<String, String> params = new HashMap<>();
            if (query != null) {
                for (String param : query.split("&")) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length > 1) {
                        params.put(keyValue[0], keyValue[1]);
                    }
                }
            }
            return params;
        }
    }

}
