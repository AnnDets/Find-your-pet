import DBControllers.DBController;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.JSONObject;
import services.UserController;
import utils.AddUserError;
import utils.JSONParser;

public class SimpleServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/register", new RegisterHandler());
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

                try (DBController dbController = new DBController()) {
                    UserController userController = new UserController(dbController);

                    // Вызываем метод добавления пользователя
                    ArrayList<AddUserError> errors = userController.addUser(
                            jsonObject.getString("name"),
                            jsonObject.getString("email"),
                            jsonObject.getString("phone"),
                            jsonObject.getString("password"),
                            jsonObject.getString("address")
                    );

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
}
