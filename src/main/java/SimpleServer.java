import DBControllers.DBController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.AddUserError;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SimpleServer {

    private static DBController dbController;

    public static void main(String[] args) throws IOException {
        dbController = new DBController(); // Инициализируем DBController
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/register", new RegisterHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Сервер запущен на порту 8080");
    }

    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(requestBody);

                // Извлекаем данные из запроса
                String name = json.getString("name");
                String email = json.getString("email");
                String phone = json.getString("phone");
                String plainPassword = json.getString("password");
                String address = json.getString("address");

                // Вызываем метод addUser из DBController
                List<AddUserError> errors = dbController.addUser(name, email, phone, plainPassword, address);

                JSONObject responseJson = new JSONObject();
                if (errors.isEmpty()) {
                    responseJson.put("status", "success");
                } else {
                    responseJson.put("status", "error");
                    JSONArray errorArray = new JSONArray();
                    for (AddUserError error : errors) {
                        errorArray.put(error.getMessage());
                    }
                    responseJson.put("errors", errorArray);
                }

                // Отправляем ответ
                String response = responseJson.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}
