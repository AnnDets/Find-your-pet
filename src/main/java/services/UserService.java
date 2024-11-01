package services;

import dao.UserDAOFactory;
import dao.UserDao;
import models.User;
import utils.LogUtil;
import utils.*;

import java.sql.Connection;
import java.util.ArrayList;

public class UserService {
    private UserDao userDao;

    public UserService(Connection connection) {
        this.userDao = UserDAOFactory.createUserDao("fake", connection);
    }

    public ArrayList<AddUserError> addUser(User user) {
        ArrayList<AddUserError> errors = new ArrayList<>();

        LogUtil.debug("Validating user data: name=" + user.getName() + ", email=" + user.getEmail() + ", phone=" + user.getPhone());

        if (!PhoneAndEmailValidator.isEmailValid(user.getEmail())) {
            LogUtil.warn("Invalid email: " + user.getEmail());
            errors.add(PhoneAndEmailError.INVALID_EMAIL);
        }
        if (!PhoneAndEmailValidator.isValidNumber(user.getPhone(), user.getAddress())) {
            LogUtil.warn("Invalid phone: " + user.getPhone());
            errors.add(PhoneAndEmailError.INVALID_PHONE);
        }

        ArrayList<PasswordErrorType> passwordErrors = PasswordUtils.validatePassword(user.getPasswordHash()); // кринж, нужен PlainPassword
        if (!passwordErrors.isEmpty()) {
            LogUtil.debug("Password validation failed for user: " + user.getEmail());
        }
        errors.addAll(passwordErrors);

        if (errors.isEmpty()) {
            String hashedPassword = PasswordUtils.hashPassword(user.getPlainPassword());
            boolean success = userDao.create(user);
            if (!success) {
                LogUtil.error("user already exists with email: " + user.getEmail(), null);
                errors.add(PhoneAndEmailError.EMAIL_ALREADY_EXISTS);
            } else {
                LogUtil.info("user successfully registered: " + user.getEmail());
            }
        } else {
            LogUtil.debug("user registration failed due to validation errors");
        }

        return errors;
    }

    public boolean authenticateUser(String email, String plainPassword) {
        LogUtil.debug("Authenticating user: " + email);
        String storedHashedPassword = userDao.getUserPasswordByEmail(email);
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

    public ArrayList<AddUserError> updateUser(User user) {
        LogUtil.debug("Updating user: id=" + user.getId() + ", email=" + user.getEmail());
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
            userDao.update(user);
            LogUtil.info("User updated successfully: id=" + id + ", email=" + email);
        } else {
            LogUtil.debug("User update failed due to validation errors");
        }

        return errors;
    }

    public boolean deleteUser(User user) {
        LogUtil.debug("Deleting user: id=" + user.getId());
        boolean result = userDao.delete(user);
        if (result) {
            LogUtil.info("User deleted successfully: id=" + user.getId());
        } else {
            LogUtil.error("Failed to delete user: id=" + user.getId(), null);
        }
        return result;
    }

    public User getUserById(User user) {
        LogUtil.debug("Fetching user by id: " + user.getId());
        return (User) userDao.read(user);
    }

    public ArrayList<Object> getAllUsers() {
        LogUtil.debug("Fetching all users");
        return userDao.readAll();
    }
}
