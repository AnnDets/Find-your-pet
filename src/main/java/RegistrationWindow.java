import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegistrationWindow extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JTextField addressField;
    private JLabel emailErrorLabel;
    private JLabel phoneErrorLabel;
    private JLabel passwordErrorLabel;

    public RegistrationWindow() {
        setTitle("Регистрация пользователя");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Поля для ввода данных
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        passwordField = new JPasswordField(20);
        addressField = new JTextField(20);

        // Ошибки
        emailErrorLabel = new JLabel();
        emailErrorLabel.setForeground(Color.RED);
        phoneErrorLabel = new JLabel();
        phoneErrorLabel.setForeground(Color.RED);
        passwordErrorLabel = new JLabel();
        passwordErrorLabel.setForeground(Color.RED);

        JButton submitButton = new JButton("Зарегистрироваться");
        submitButton.addActionListener(e -> sendRegistrationData());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));
        panel.add(new JLabel("Имя:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(emailErrorLabel); // Отображение ошибки email
        panel.add(new JLabel("Телефон:"));
        panel.add(phoneField);
        panel.add(phoneErrorLabel); // Отображение ошибки телефона
        panel.add(new JLabel("Пароль:"));
        panel.add(passwordField);
        panel.add(passwordErrorLabel); // Отображение ошибки пароля
        panel.add(new JLabel("Адрес:"));
        panel.add(addressField);
        panel.add(submitButton);

        add(panel);
    }

    private void sendRegistrationData() {
        // Очищаем ошибки перед новой отправкой
        clearErrorLabels();

        // Собираем данные
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());
        String address = addressField.getText();

        // Подготавливаем JSON для отправки
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", name);
        jsonBody.put("email", email);
        jsonBody.put("phone", phone);
        jsonBody.put("password", password);
        jsonBody.put("address", address);

        // Отправляем запрос на сервер
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();

        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        // Обработка ответа от сервера
        response.thenAccept(res -> {
            JSONObject jsonResponse = new JSONObject(res.body());
            if (jsonResponse.getString("status").equals("success")) {
                JOptionPane.showMessageDialog(this, "Пользователь успешно зарегистрирован!");
            } else {
                JSONArray errors = jsonResponse.getJSONArray("errors");
                for (int i = 0; i < errors.length(); i++) {
                    handleServerError(errors.getString(i));
                }
            }
        }).exceptionally(e -> {
            JOptionPane.showMessageDialog(this, "Ошибка соединения с сервером: " + e.getMessage());
            return null;
        });
    }

    private void handleServerError(String errorMessage) {
        // Обработка ошибок
        if (errorMessage.contains("email")) {
            emailErrorLabel.setText(errorMessage);
        } else if (errorMessage.contains("Телефон")) {
            phoneErrorLabel.setText(errorMessage);
        } else if (errorMessage.contains("Пароль")) {
            passwordErrorLabel.setText(errorMessage);
        }
    }

    private void clearErrorLabels() {
        emailErrorLabel.setText("");
        phoneErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistrationWindow window = new RegistrationWindow();
            window.setVisible(true);
        });
    }
}
