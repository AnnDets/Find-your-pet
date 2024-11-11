//import DBControllers.DBController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import models.Report;
import org.json.JSONObject;
import services.ReportService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleServer {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/findyourpet";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "123456";
    private static UserService userService;
    private static ReportService reportService;

    static {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
            userService = new UserService(connection);
            reportService = new ReportService(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database connection", e);
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/register", new RegisterHandler());
        server.createContext("/updateUser", new UserUpdateHandler());
        server.createContext("/addReport", new ReportAddHandler());
        server.createContext("/getReports", new GetReportsHandler());
        server.createContext("/getFilteredReports", new GetFilteredReportsHandler());
        server.createContext("/login", new LoginHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");
    }

    public static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Установка заголовков CORS
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // Проверка, является ли запрос предзапросом (OPTIONS)
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1); // Успешный ответ для предзапросов
                return;
            }

            // Обработка POST-запросов на регистрацию
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try (InputStream inputStream = exchange.getRequestBody();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                    String requestBody = reader.lines().reduce("", String::concat);
                    JSONObject jsonObject = new JSONObject(requestBody);

                    // Основная логика добавления пользователя
                    ArrayList<AddUserError> errors = userService.addUser(JSONParser.jsonToUser(jsonObject));

                    if (!errors.isEmpty()) {
                        String jsonResponse = JSONParser.parseAddUserErrors(errors).toString();
                        sendResponse(exchange, 400, jsonResponse);
                    } else {
                        sendResponse(exchange, 200, "User registered successfully!");
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }

        // Метод для отправки ответа
        private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
            exchange.sendResponseHeaders(statusCode, responseText.getBytes().length);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(responseText.getBytes());
            }
        }
    }

    public static class LoginHandler implements HttpHandler {
        private static final String SECRET_KEY = "cviyckkchkcfhdgxjzdzd"; // Секретный ключ для JWT

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // CORS-заголовки
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1); // No Content для предзапросов
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try (InputStream inputStream = exchange.getRequestBody();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                    String requestBody = reader.lines().reduce("", String::concat);
                    JSONObject jsonObject = new JSONObject(requestBody);

                    String email = jsonObject.getString("email");
                    String password = jsonObject.getString("password");

                    if (userService.authenticateUser(email, password)) {
                        String token = createToken(email);
                        sendResponse(exchange, 200, new JSONObject().put("token", token).toString());
                    } else {
                        sendResponse(exchange, 401, "Неверные учетные данные");
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }

        private boolean authenticate(String email, String password) {
            // Ваша логика проверки учетных данных
            return "test@example.com".equals(email) && "password".equals(password);
        }

        private String createToken(String email) {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withSubject(email)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 час
                    .sign(algorithm);
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(statusCode, responseText.getBytes("UTF-8").length);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(responseText.getBytes("UTF-8"));
            }
        }
    }

    static class ReportAddHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
                try (InputStream inputStream = exchange.getRequestBody();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String requestBody = reader.lines().reduce("", String::concat);
                    JSONObject jsonObject = new JSONObject(requestBody);
                    Report report = JSONParser.jsonToReport(jsonObject);

                    ArrayList<ReportError> errors = reportService.addReport(report);
                    if (errors.isEmpty()) {
                        sendResponse(exchange, 200, "Report added successfully!");
                    } else {
                        String jsonResponse = JSONParser.parseReportErrors(errors).toString();
                        sendResponse(exchange, 400, jsonResponse);
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class UserUpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try (InputStream inputStream = exchange.getRequestBody();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String requestBody = reader.lines().reduce("", String::concat);
                    JSONObject jsonObject = new JSONObject(requestBody);

                    ArrayList<AddUserError> errors = userService.updateUser(JSONParser.jsonToUser(jsonObject));
                    if (!errors.isEmpty()) {
                        String jsonResponse = JSONParser.parseAddUserErrors(errors).toString();
                        sendResponse(exchange, 400, jsonResponse);
                    } else {
                        sendResponse(exchange, 200, "User updated successfully!");
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class GetReportsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                ArrayList<Report> reports = new ArrayList<>();
                String jsonResponse = JSONParser.serializeReports(reports).toString();
                sendResponse(exchange, 200, jsonResponse);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

    static class GetFilteredReportsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = parseQuery(query);

                String color = params.getOrDefault("color", null);
                String specialMark = params.getOrDefault("specialMark", null);
                String location = params.getOrDefault("location", null);
                String breed = params.getOrDefault("breed", null);

                ArrayList<Report> reports = reportService.getReportsByFilters(color, specialMark, location, breed);
                String jsonResponse = JSONParser.serializeReports(reports).toString();
                sendResponse(exchange, 200, jsonResponse);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }

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

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
