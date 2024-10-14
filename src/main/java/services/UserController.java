package services;

import DBControllers.DBController;
import models.User;
import utils.*;

import java.util.ArrayList;

public class UserController {
    private DBController dbController_;

    public UserController(DBController dbController) {
        this.dbController_ = dbController;
    }

    public ArrayList<AddUserError> addUser(String name, String email, String phone, String plainPassword, String address) {
        ArrayList<AddUserError> errors = new ArrayList<>();

        // Валидация данных пользователя
        if (!PhoneAndEmailValidator.isEmailValid(email)) {
            errors.add(PhoneAndEmailError.INVALID_EMAIL);
        }
        if (!PhoneAndEmailValidator.isValidNumber(phone, address)) {
            errors.add(PhoneAndEmailError.INVALID_PHONE);
        }

        // Валидация пароля
        ArrayList<PasswordErrorType> passwordErrors = PasswordUtils.validatePassword(plainPassword);
        errors.addAll(passwordErrors);

        if (errors.isEmpty()) {
            // Хэширование пароля
            String hashedPassword = PasswordUtils.hashPassword(plainPassword);

            // Добавление пользователя через DBController
            boolean success = dbController_.addUserToDB(name, email, phone, hashedPassword, address);
            if (!success) {
                errors.add(PhoneAndEmailError.EMAIL_ALREADY_EXISTS);
            }
        }

        return errors;
    }

    public boolean authenticateUser(String email, String plainPassword) {
        String storedHashedPassword = dbController_.getUserPasswordByEmail(email);
        if (storedHashedPassword != null) {
            return PasswordUtils.checkPassword(plainPassword, storedHashedPassword);
        }
        return false;
    }

    public ArrayList<AddUserError> updateUser(int id, String name, String email, String phone, String plainPassword, String address) {
        ArrayList<AddUserError> errors = new ArrayList<>();

        // Валидация данных пользователя
        if (!PhoneAndEmailValidator.isEmailValid(email)) {
            errors.add(PhoneAndEmailError.INVALID_EMAIL);
        }
        if (!PhoneAndEmailValidator.isValidNumber(phone, address)) {
            errors.add(PhoneAndEmailError.INVALID_PHONE);
        }
        // Валидация пароля
        ArrayList<PasswordErrorType> passwordErrors = PasswordUtils.validatePassword(plainPassword);
        errors.addAll(passwordErrors);

        if (errors.isEmpty()) {
            String hashedPassword = PasswordUtils.hashPassword(plainPassword);
            dbController_.updateUserInDB(id, name, email, phone, hashedPassword, address);
        }

        return errors;
    }

    public boolean deleteUser(int userId) {
        return dbController_.deleteUserFromDB(userId);
    }

    public User getUserById(int userId) {
        return dbController_.getUserFromDB(userId);
    }

    public ArrayList<User> getAllUsers() {
        return dbController_.getAllUsers();
    }
}
