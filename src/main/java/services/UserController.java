package services;

import DBControllers.DBController;
import models.User;
import utils.LogUtil;
import utils.*;

import java.util.ArrayList;

public class UserController {
    private DBController dbController_;

    public UserController(DBController dbController) {
        this.dbController_ = dbController;
    }

    public ArrayList<AddUserError> addUser(String name, String email, String phone, String plainPassword, String address) {
        ArrayList<AddUserError> errors = new ArrayList<>();

        LogUtil.debug("Validating user data: name=" + name + ", email=" + email + ", phone=" + phone);

        if (!PhoneAndEmailValidator.isEmailValid(email)) {
            LogUtil.warn("Invalid email: " + email);
            errors.add(PhoneAndEmailError.INVALID_EMAIL);
        }
        if (!PhoneAndEmailValidator.isValidNumber(phone, address)) {
            LogUtil.warn("Invalid phone: " + phone);
            errors.add(PhoneAndEmailError.INVALID_PHONE);
        }

        ArrayList<PasswordErrorType> passwordErrors = PasswordUtils.validatePassword(plainPassword);
        if (!passwordErrors.isEmpty()) {
            LogUtil.debug("Password validation failed for user: " + email);
        }
        errors.addAll(passwordErrors);

        if (errors.isEmpty()) {
            String hashedPassword = PasswordUtils.hashPassword(plainPassword);
            boolean success = dbController_.addUserToDB(name, email, phone, hashedPassword, address);
            if (!success) {
                LogUtil.error("User already exists with email: " + email, null);
                errors.add(PhoneAndEmailError.EMAIL_ALREADY_EXISTS);
            } else {
                LogUtil.info("User successfully registered: " + email);
            }
        } else {
            LogUtil.debug("User registration failed due to validation errors");
        }

        return errors;
    }

    public boolean authenticateUser(String email, String plainPassword) {
        LogUtil.debug("Authenticating user: " + email);
        String storedHashedPassword = dbController_.getUserPasswordByEmail(email);
        if (storedHashedPassword != null) {
            boolean isAuthenticated = PasswordUtils.checkPassword(plainPassword, storedHashedPassword);
            if (isAuthenticated) {
                LogUtil.info("User authenticated: " + email);
                return true;
            } else {
                LogUtil.warn("Authentication failed for user: " + email);
            }
        } else {
            LogUtil.warn("User not found: " + email);
        }
        return false;
    }

    public ArrayList<AddUserError> updateUser(int id, String name, String email, String phone, String plainPassword, String address) {
        LogUtil.debug("Updating user: id=" + id + ", email=" + email);
        ArrayList<AddUserError> errors = new ArrayList<>();

        if (!PhoneAndEmailValidator.isEmailValid(email)) {
            LogUtil.warn("Invalid email for update: " + email);
            errors.add(PhoneAndEmailError.INVALID_EMAIL);
        }
        if (!PhoneAndEmailValidator.isValidNumber(phone, address)) {
            LogUtil.warn("Invalid phone for update: " + phone);
            errors.add(PhoneAndEmailError.INVALID_PHONE);
        }

        ArrayList<PasswordErrorType> passwordErrors = PasswordUtils.validatePassword(plainPassword);
        errors.addAll(passwordErrors);

        if (errors.isEmpty()) {
            String hashedPassword = PasswordUtils.hashPassword(plainPassword);
            dbController_.updateUserInDB(id, name, email, phone, hashedPassword, address);
            LogUtil.info("User updated successfully: id=" + id + ", email=" + email);
        } else {
            LogUtil.debug("User update failed due to validation errors");
        }

        return errors;
    }

    public boolean deleteUser(int userId) {
        LogUtil.debug("Deleting user: id=" + userId);
        boolean result = dbController_.deleteUserFromDB(userId);
        if (result) {
            LogUtil.info("User deleted successfully: id=" + userId);
        } else {
            LogUtil.error("Failed to delete user: id=" + userId, null);
        }
        return result;
    }

    public User getUserById(int userId) {
        LogUtil.debug("Fetching user by id: " + userId);
        return dbController_.getUserFromDB(userId);
    }

    public ArrayList<User> getAllUsers() {
        LogUtil.debug("Fetching all users");
        return dbController_.getAllUsers();
    }
}
