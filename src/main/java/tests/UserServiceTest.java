/*
package tests;

import DBControllers.DBController;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ReportController;
import services.UserService;
import utils.*;

import dao.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private DBController dbController;

    @BeforeEach
    void setUp() {
        dbController = mock(DBController.class);
        userService = new UserService(ReportController);
    }

    //Проверяет, что при вводе корректных данных функция addUser возвращает пустой список ошибок.
    @Test
    void testAddUserValidData() {
        // Arrange
        User user= new User(1,"Test User","test23@example.com",
                "1234567890", "Pewrvrvg#d135","Test Address");

        when(UserDao.create(user)).thenReturn(true);

        ArrayList<AddUserError> errors = userService.addUser(user);

        assertTrue(errors.isEmpty());
        verify(UserDao, times(1)).create(user);

    }

    //Проверяет, что при вводе некорректного адреса электронной почты функция addUser возвращает ошибку INVALID_EMAIL.
    @Test
    void testAddUserInvalidEmail() {
        User user= new User(2,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");

        // Act
        ArrayList<AddUserError> errors = userService.addUser(user);

        //assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_EMAIL));
        verify(UserDao, never()).create(user);
    }


    //Проверяет, что при вводе некорректного номера телефона функция addUser возвращает ошибку INVALID_PHONE.
    @Test
    void testAddUserInvalidPhone() {
        User user= new User(3,"Test User","test23@example.com",
                "invalid-phone", "Pewrvrvg#d135","Test Address");

        // Act
        ArrayList<AddUserError> errors = userService.addUser(user);

        //assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_PHONE));
        verify(UserDao, never()).create(user);
    }

    //Проверяет, что при вводе некорректного пароля функция addUser возвращает соответствующие ошибки валидации пароля.
    @SuppressWarnings("checkstyle:MethodName")
    @Test
    void testAddUserInvalidPassword() {
        User user= new User(4,"Test User","test23@example.com",
                "1234567890", "pas123","Test Address");

        ArrayList<AddUserError> errors = userService.addUser(user);

        //assertEquals(1, errors.size());
        assertTrue(errors.contains(PasswordErrorType.TOO_SHORT));
        verify(UserDao, never()).create(user);
    }

    //Проверяет, что при попытке добавить пользователя с уже существующим адресом электронной почты функция addUser возвращает ошибку EMAIL_ALREADY_EXISTS.
    @Test
    void testAddUserEmailAlreadyExists() {
        User user= new User(5,"Test User","test23@example.com",
                "1234567890", "Pewrvrvg#d135","Test Address");

        ArrayList<AddUserError> errors = userService.addUser(user);

        assertTrue(errors.contains(PhoneAndEmailError.EMAIL_ALREADY_EXISTS));
        verify(UserDao, never()).create(user);
    }

    //Проверяет, что при вводе корректных учетных данных функция authenticateUser возвращает true.
    @Test
    void testAuthenticateUserSuccess() {
        String email = "test23@example.com";
        String plainPassword = "Pewrvrvg#d135";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);

        when(dbController.getUserPasswordByEmail(email)).thenReturn(hashedPassword);
        // Act
        boolean success = userService.authenticateUser(email, plainPassword);
        // Assert
        assertTrue(success);
        verify(dbController).getUserPasswordByEmail(email);
    }

    //Проверяет, что authenticateUser возвращает false, если введенный пароль не совпадает с хэшированным паролем, хранящимся в базе данных.
    @Test
    void testAuthenticateUserInvalidPassword() {
        String email = "test23@example.com";
        String plainPassword = "Pewrvrvg#d135";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);

        when(dbController.getUserPasswordByEmail(email)).thenReturn(hashedPassword);

        // Act
        boolean success = userService.authenticateUser(email, plainPassword);

        // Assert
        assertFalse(success);
        verify(dbController).getUserPasswordByEmail(email);


        */
/*when(userDao.getUserPasswordByEmail(email)).thenReturn(hashedPassword);
        when(PasswordUtils.checkPassword(plainPassword, hashedPassword)).thenReturn(false);

        boolean isAuthenticated = userService.authenticateUser(email, plainPassword);

        assertFalse(isAuthenticated);
        verify(userDao, times(1)).getUserPasswordByEmail(email);
        verify(PasswordUtils, times(1)).checkPassword(plainPassword, hashedPassword);*//*

    }


    //Проверяет аутентификацию несуществующего пользователя.
    @Test
    void testAuthenticateUserNotFound() {
        String email = "john.doe@example.com";
        String plainPassword = "password";
        when(UserDao.getUserPasswordByEmail(email)).thenReturn(null);

        boolean isAuthenticated = userService.authenticateUser(email, plainPassword);

        assertFalse(isAuthenticated);
        verify(UserDao, times(1)).getUserPasswordByEmail(email);
        verify(PasswordUtils, never()).checkPassword(plainPassword, anyString());
    }


    //Проверить, что updateUser не возвращает ошибок, если переданы валидные данные пользователя.
    @Test
    void testUpdateUserValidData() {
        User user= new User(1,"Test User","test23@example.com",
                "1234567890", "Pewrvrvg#d135","Test Address");

        // Act
        ArrayList<AddUserError> errors = userService.updateUser(user);

        // Assert
        assertTrue(errors.isEmpty());
        //verify(dbController).updateUserInDB(user);
    }

    //Проверить, что updateUser возвращает ошибку INVALID_EMAIL, если передан некорректный email.
    @Test
    void testUpdateUserInvalidEmail() {
        User user= new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");
        ArrayList<AddUserError> errors = userService.updateUser(user);

        assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_EMAIL));
        verify(UserDao, never()).update(user);
        }

    //Проверить, что updateUser возвращает ошибку INVALID_PHONE, если передан некорректный номер телефона
    @Test
    void testUpdateUserInvalidPhone() {
        User user= new User(1,"Test User","test23@example.com",
                "invalid-phone", "Pewrvrvg#d135","Test Address");
        ArrayList<AddUserError> errors = userService.updateUser(user);

        assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_PHONE));
        verify(UserDao, never()).update(user);
    }

    //Проверить, что updateUser возвращает ошибки, связанные с паролем, если передан некорректный пароль.
    @Test
    void testUpdateUserInvalidPassword() {
        User user= new User(1,"Test User","test23@example.com",
                "1234567890", "Pewrvrvg#d135","Test Address");

        ArrayList<AddUserError> errors = userService.updateUser(user);

        assertEquals(1, errors.size());
        assertTrue(errors.contains(PasswordErrorType.TOO_SHORT));
        verify(UserDao, never()).update(user);
    }

    // Проверяет, что функция deleteUser удаляет пользователя.
    @Test
    void testDeleteUserSuccess() {
        User user= new User(1,"Test User","test23@example.com",
                "1234567890", "Pewrvrvg#d135","Test Address");

        userService.deleteUser(user);
        // Assert
        boolean result = userService.deleteUser(user);
        assertTrue(result);
        verify(UserDao, times(1)).delete(user);
    }

    //Проверяет неудачное удаление пользователя.
    @Test
    void testDeleteUserFailed() {
        User user= new User(1,"Test User","test23@example.com",
                "1234567890", "Pewrvrvg#d135","Test Address");

        userService.deleteUser(user);
        // Assert
        boolean result = userService.deleteUser(user);
        assertTrue(result);
        verify(UserDao, times(1)).delete(user);
    }

    //Проверяет, что функция getUserById возвращает пользователя по его идентификатору.
    @Test
    void testGetUserById() {
        // Arrange
        int userId = 1;
        User user= new User(1,"Test User","test23@example.com",
                "1234567890", "Pewrvrvg#d135","Test Address");when(dbController.getUserFromDB(userId)).thenReturn(user);

        // Act
        User returnedUser = userService.getUserById(user);

        // Assert
        assertEquals(user, returnedUser);
        verify(dbController).getUserFromDB(userId);
    }

    //Проверяет, что функция getAllUsers возвращает всех пользователей
    @Test
    void testGetAllUsers() {
        List<Object> users = new ArrayList<>();
        when(UserService.getAllUsers()).thenReturn(users);

        List<Object> fetchedUsers = userService.getAllUsers();

        assertEquals(users, fetchedUsers);
        verify(userDao, times(1)).readAll();
    }
}*/
