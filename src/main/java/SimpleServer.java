import DBControllers.DBController;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServer {

    private volatile boolean running = true;
    private HttpServer server;
    private ExecutorService executor;

    public void start() {
        try (DBController dbcontroller = new DBController()) {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/register", new RegisterHandler(dbcontroller));
            executor = Executors.newFixedThreadPool(2);  // Пул потоков для обработки запросов
            server.setExecutor(executor); // Привязка пула потоков
            server.start();

            System.out.println("Сервер запущен на порту 8080. Введите 'close' для остановки сервера.");

            // Ожидаем команду для завершения работы
            waitForShutdownCommand();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            shutdownServer();
        }
    }

    private void waitForShutdownCommand() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String command;
            while ((command = reader.readLine()) != null) {
                if ("close".equalsIgnoreCase(command.trim())) {
                    System.out.println("Остановка сервера...");
                    running = false;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shutdownServer() {
        if (server != null) {
            server.stop(0);
        }
        if (executor != null) {
            executor.shutdownNow();
        }
        System.out.println("Сервер остановлен.");
    }

    public static void main(String[] args) {
        SimpleServer server = new SimpleServer();
        server.start();
    }

    // Вложенный класс для обработки регистрационных запросов
    static class RegisterHandler implements HttpHandler {

        private DBController dbcontroller;

        public RegisterHandler(DBController dbcontroller) {
            this.dbcontroller = dbcontroller;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            /*if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new BufferedReader(new InputStreamReader(is))
                        .lines().collect(Collectors.joining("\n"));

                // Разбираем запрос (например, в формате JSON)
                // JSONObject json = new JSONObject(body);

                String name = ...;  // Извлекаем параметры из тела запроса
                String email = ...;
                String phone = ...;
                String password = ...;
                String address = ...;

                AddUserResults result = dbcontroller.addUser(name, email, phone, password, address);
                String response;

                if (result == AddUserResults.SUCCESS) {
                    response = "Пользователь успешно добавлен.";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                } else {
                    response = "Ошибка регистрации: " + result.toString();
                    exchange.sendResponseHeaders(400, response.getBytes().length);
                }

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }*/
        }
    }
}
