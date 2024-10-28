package services;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class ClientApp {
    private static final String SERVER_URL = "http://localhost:8080";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        System.out.println("1. Register User\n2. Update User\n3. Add Report\n4. Get All Reports\n5. Get Filtered Reports\nChoose an option:");
        while (running) {
        int choice = scanner.nextInt();
        scanner.nextLine(); // clear buffer


            switch (choice) {
                case 1 -> registerUser(scanner);
                case 2 -> updateUser(scanner);
                case 3 -> addReport(scanner);
                case 4 -> getAllReports();
                case 5 -> getFilteredReports(scanner);
                case 6 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        try {
            JSONObject user = new JSONObject();
            System.out.println("Enter name:");
            user.put("name", scanner.nextLine());
            System.out.println("Enter email:");
            user.put("email", scanner.nextLine());
            System.out.println("Enter phone:");
            user.put("phone", scanner.nextLine());
            System.out.println("Enter password:");
            user.put("password", scanner.nextLine());
            System.out.println("Enter address:");
            user.put("address", scanner.nextLine());

            sendRegistrationData(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void sendRegistrationData(JSONObject json) {


        // Отправляем запрос на сервер
        try {

            // Устанавливаем URL для отправки запроса
            URL url = new URL("http://localhost:8080/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Отправляем данные
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Получаем ответ
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            InputStream inputStream;
            if (responseCode >= 200 && responseCode < 300) {
                // Если успешный запрос
                inputStream = conn.getInputStream();
            } else {
                // Если произошла ошибка
                inputStream = conn.getErrorStream();
            }

            // Читаем ответ от сервера
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // Выводим полученный JSON в консоль
                System.out.println("Response JSON: " + response.toString());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static void sendUpdateData(JSONObject json) {


        // Отправляем запрос на сервер
        try {

            // Устанавливаем URL для отправки запроса
            URL url = new URL("http://localhost:8080/updateUser");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Отправляем данные
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Получаем ответ
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            InputStream inputStream;
            if (responseCode >= 200 && responseCode < 300) {
                // Если успешный запрос
                inputStream = conn.getInputStream();
            } else {
                // Если произошла ошибка
                inputStream = conn.getErrorStream();
            }

            // Читаем ответ от сервера
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // Выводим полученный JSON в консоль
                System.out.println("Response JSON: " + response.toString());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static void updateUser(Scanner scanner) {
        try {
            JSONObject user = new JSONObject();
            System.out.println("Enter user ID:");
            user.put("id", scanner.nextInt());
            scanner.nextLine();
            System.out.println("Enter name:");
            user.put("name", scanner.nextLine());
            System.out.println("Enter email:");
            user.put("email", scanner.nextLine());
            System.out.println("Enter phone:");
            user.put("phone", scanner.nextLine());
            System.out.println("Enter password:");
            user.put("password", scanner.nextLine());
            System.out.println("Enter address:");
            user.put("address", scanner.nextLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addReport(Scanner scanner) {
        try {
            JSONObject report = new JSONObject();
            System.out.println("Enter user ID:");
            report.put("userId", scanner.nextInt());
            scanner.nextLine();
            System.out.println("Enter breed:");
            report.put("breed", scanner.nextLine());
            System.out.println("Enter description:");
            report.put("description", scanner.nextLine());
            System.out.println("Enter found date:");
            report.put("foundDate", scanner.nextLine());
            System.out.println("Enter location:");
            report.put("location", scanner.nextLine());
            System.out.println("Enter status:");
            report.put("status", scanner.nextLine());

            sendPostRequest("/addReport", report);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getAllReports() {
        try {
            sendGetRequest("/getReports");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getFilteredReports(Scanner scanner) {
        try {
            JSONObject filters = new JSONObject();
            System.out.println("Enter color (or leave blank):");
            String color = scanner.nextLine();
            if (!color.isEmpty()) filters.put("color", color);

            System.out.println("Enter special mark (or leave blank):");
            String specialMark = scanner.nextLine();
            if (!specialMark.isEmpty()) filters.put("specialMark", specialMark);

            System.out.println("Enter location (or leave blank):");
            String location = scanner.nextLine();
            if (!location.isEmpty()) filters.put("location", location);

            System.out.println("Enter breed (or leave blank):");
            String breed = scanner.nextLine();
            if (!breed.isEmpty()) filters.put("breed", breed);

            sendPostRequest("/getFilteredReports", filters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPostRequest(String endpoint, JSONObject jsonData) throws Exception {
        URL url = new URL(SERVER_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonData.toString().getBytes());
            os.flush();
        }

        printResponse(conn);
    }

    private static void sendPutRequest(String endpoint, JSONObject jsonData) throws Exception {
        URL url = new URL(SERVER_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonData.toString().getBytes());
            os.flush();
        }

        printResponse(conn);
    }

    private static void sendGetRequest(String endpoint) throws Exception {
        URL url = new URL(SERVER_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }

    private static void printResponse(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println("Response: " + content.toString());
        } catch (IOException e) {
            System.out.println("Error reading response: " + e.getMessage());
        }
    }
}
