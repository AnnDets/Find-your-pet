package services;

import DBControllers.DBController;
import models.User;
import org.json.JSONObject;
import utils.AddUserError;
import utils.JSONParser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleApp {
    static Scanner scanner = new Scanner(System.in);
    static UserController userController;
    ConsoleApp() {
        try ( DBController dbController = new DBController();){
            userController = new UserController(dbController);

            boolean running = true;

            while (running) {
                System.out.println("\nВыберите операцию:");
                System.out.println("1: Создать пользователя");
                System.out.println("2: Получить пользователя по ID");
                System.out.println("3: Обновить пользователя");
                System.out.println("4: Удалить пользователя");
                System.out.println("5: Получить всех пользователей");
                System.out.println("6: Выход");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        getUser();
                        break;
                    case 3:
                        updateUser();
                        break;
                    case 4:
                        deleteUser();
                        break;
                    case 5:
                        getAllUsers();
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный выбор, попробуйте еще раз.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {
        System.out.println("Добро пожаловать в User Management Console App");
        ConsoleApp app = new ConsoleApp();

        System.out.println("Завершение работы...");
    }

    private static void createUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите номер телефона: ");
        String phone = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        System.out.print("Введите адрес: ");
        String address = scanner.nextLine();

        ArrayList<AddUserError> errors = userController.addUser(name, email, phone, password, address);
        JSONObject response = JSONParser.parseAddUserErrors(errors);
        System.out.println("Ответ: " + response.toString(4));
    }

    private static void getUser() {
        System.out.print("Введите ID пользователя: ");
        int id = Integer.parseInt(scanner.nextLine());

        User user = userController.getUserById(id);
        if (user != null) {
            JSONObject response = JSONParser.parseUser(user);
            System.out.println("Ответ: " + response.toString(4));
        } else {
            System.out.println("{\"status\": 0, \"message\": \"Пользователь не найден\"}");
        }
    }

    private static void updateUser() {
        System.out.print("Введите ID пользователя: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите новое имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите новый email: ");
        String email = scanner.nextLine();
        System.out.print("Введите новый номер телефона: ");
        String phone = scanner.nextLine();
        System.out.print("Введите новый пароль: ");
        String password = scanner.nextLine();
        System.out.print("Введите новый адрес: ");
        String address = scanner.nextLine();

        ArrayList<AddUserError> errors = userController.updateUser(id, name, email, phone, password, address);
        JSONObject response = JSONParser.parseUpdateUserErrors(errors);
        System.out.println("Ответ: " + response.toString(4));
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя: ");
        int id = Integer.parseInt(scanner.nextLine());

        boolean success = userController.deleteUser(id);
        if (success) {
            System.out.println("{\"status\": 1, \"message\": \"Пользователь удален\"}");
        } else {
            System.out.println("{\"status\": 0, \"message\": \"Не удалось удалить пользователя\"}");
        }
    }

    private static void getAllUsers() {
        ArrayList<User> users = userController.getAllUsers();
        users.forEach(user -> {
            JSONObject userJSON = JSONParser.parseUser(user);
            System.out.println(userJSON.toString(4));
        });
    }
}

