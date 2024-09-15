package DBControllers;
import utils.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController {
    final private Connection connection_;
    final private Logger logger_;
    public  UserController(Connection connection, Logger logger){
        connection_ = connection;
        logger_ = logger;
    }
    public ArrayList<AddUserError> addUser(String name, String email, String phone, String plainPassword, String address) {
        // Проверяем, существует ли пользователь с таким email или телефоном
        ArrayList<AddUserError> errors = new ArrayList<>();
        if(!PhoneAndEmailValidator.isEmailValid(email)){
            errors.add(PhoneAndEmailError.INVALID_EMAIL);
        }
        if(!PhoneAndEmailValidator.isValidNumber(phone, address)){
            errors.add((PhoneAndEmailError.INVALID_PHONE));
        }
        if (userExists(email, phone)) {
            logger_.warning("Пользователь с таким email или телефоном уже существует: " + email + ", " + phone);
            errors.add(PhoneAndEmailError.EMAIL_ALREADY_EXISTS); // Можно заменить на выброс исключения, если нужно
        }
        if (!errors.isEmpty()) {
            logger_.log(Level.SEVERE, "Некоректно введеный пароль при создании:\n"
                                                +"username: "+name+'\n'+"password: "+plainPassword +'\n' +errors+'\n' );
            for(PasswordErrorType error : PasswordUtils.validatePassword(plainPassword) ){
                errors.add(error);
            }
        }
        // Хэшируем пароль перед сохранением
        if(!errors.isEmpty()){
            return errors;
        }
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);
        String query = "INSERT INTO users (name, email, phone, password_hash, address) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, hashedPassword); // Сохраняем хэшированный пароль
            stmt.setString(5, address);
            stmt.executeUpdate();
            logger_.info("Пользователь добавлен: " + name);
            return new ArrayList<>();
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при добавлении пользователя", e);
        }
        return new ArrayList<>();
    }

    public boolean userExists(String email, String phone) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ? OR phone = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, phone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    return true; // Пользователь с таким email или телефоном уже существует
                }
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при проверке существования пользователя", e);
        }
        return false;
    }
    public boolean authenticateUser(String email, String plainPassword) {
        String query = "SELECT password_hash FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHashedPassword = rs.getString("password_hash");

                // Проверяем, совпадает ли введённый пароль с хранимым хэшем
                if (PasswordUtils.checkPassword(plainPassword, storedHashedPassword)) {
                    logger_.info("Аутентификация успешна для пользователя с email: " + email);
                    return true;
                } else {
                    logger_.warning("Неверный пароль для пользователя с email: " + email);
                    return false;
                }
            } else {
                logger_.warning("Пользователь с email " + email + " не найден.");
                return false;
            }
        } catch (SQLException e) {
            logger_.log(Level.SEVERE, "Ошибка при аутентификации пользователя", e);
            return false;
        }
    }


}
