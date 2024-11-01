package tests;

import DBControllers.DBController;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserService;
import utils.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private DBController dbController;

    @BeforeEach
    void setUp() {
        dbController = mock(DBController.class);
        userService = new UserService(dbController);
    }

    //Проверяет, что при вводе корректных данных функция addUser возвращает пустой список ошибок.
    @Test
    void addUser_ValidData_ShouldReturnEmptyErrors() {
        // Arrange
        String name = "Test User";
        String email = "test23@example.com";
        String phone = "1234567890";
        String plainPassword = "Pewrvrvg#d135";
        String address = "Test Address";

        //when(dbController.addUserToDB(name, email, phone, anyString(), address)).thenReturn(true);

        // Act
        ArrayList<AddUserError> errors = userService.addUser(
                name, email, phone, plainPassword, address);

        // Assert
        assertTrue(errors.isEmpty());
        verify(dbController).addUserToDB(name, email, phone, anyString(), address);
    }

    //Проверяет, что при вводе некорректного адреса электронной почты функция addUser возвращает ошибку INVALID_EMAIL.
    @Test
    void addUser_InvalidEmail_ShouldReturnInvalidEmailError() {
        // Arrange
        String name = "Test User";
        String email = "invalid.email";
        String phone = "1234567890";
        String plainPassword = "Password123";
        String address = "Test Address";

        // Act
        ArrayList<AddUserError> errors = userService.addUser(name, email, phone, plainPassword, address);

        // Assert
        //assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_EMAIL));
        verify(dbController, never()).addUserToDB(anyString(), anyString(), anyString(), anyString(), anyString());
    }


    //Проверяет, что при вводе некорректного номера телефона функция addUser возвращает ошибку INVALID_PHONE.
    @Test
    void addUser_InvalidPhone_ShouldReturnInvalidPhoneError() {
        // Arrange
        String name = "Test User";
        String email = "test@example.com";
        String phone = "123456789"; // Invalid phone number
        String plainPassword = "Password123";
        String address = "Test Address";

        // Act
        ArrayList<AddUserError> errors = userService.addUser(name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_PHONE));
        verify(dbController, never()).addUserToDB(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    //Проверяет, что при вводе некорректного пароля функция addUser возвращает соответствующие ошибки валидации пароля.
    @SuppressWarnings("checkstyle:MethodName")
    @Test
    void addUserInvalidPasswordShouldReturnPasswordErrors() {
        // Arrange
        String name = "Test User";
        String email = "test@example.com";
        String phone = "1234567890";
        String plainPassword = "Pa#24"; // Invalid password (too short)
        String address = "Test Address";

        // Act
        ArrayList<AddUserError> errors = userService.addUser(name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PasswordErrorType.TOO_SHORT));
        verify(dbController, never()).addUserToDB(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    //Проверяет, что при попытке добавить пользователя с уже существующим адресом электронной почты функция addUser возвращает ошибку EMAIL_ALREADY_EXISTS.
    @Test
    void addUserEmailAlreadyExistsShouldReturnEmailAlreadyExistsError() {
        // Arrange
        String name = "Test User";
        String email = "test@example.com";
        String phone = "1234567890";
        String plainPassword = "Password123";
        String address = "Test Address";

        //when(dbController.addUserToDB(name, email, phone, anyString(), address)).thenReturn(false);

        // Act
        ArrayList<AddUserError> errors = userService.addUser(name, email, phone, plainPassword, address);

        // Assert
        //assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.EMAIL_ALREADY_EXISTS));
        verify(dbController).addUserToDB(name, email, phone, anyString(), address);
    }

    //Проверяет, что при вводе корректных учетных данных функция authenticateUser возвращает true.
    @Test
    void authenticateUser_ValidCredentials_ShouldReturnTrue() {
        // Arrange
        String email = "test@example.com";
        String plainPassword = "Password123";
        String hashedPassword = PasswordUtils.hashPassword(plainPassword);

        when(dbController.getUserPasswordByEmail(email)).thenReturn(hashedPassword);

        // Act
        boolean success = userService.authenticateUser(email, plainPassword);

        // Assert
        assertTrue(success);
        verify(dbController).getUserPasswordByEmail(email);
    }

    //Проверяет, что при вводе некорректного адреса электронной почты функция updateUser возвращает ошибку
    @Test
    void authenticateUser_InvalidEmail_ShouldReturnFalse() {
        // Arrange
        String email = "test@example.com";
        String plainPassword = "Password123";
        when(dbController.getUserPasswordByEmail(email)).thenReturn(null);

        // Act
        boolean success = userService.authenticateUser(email, plainPassword);

        // Assert
        assertFalse(success);
        verify(dbController).getUserPasswordByEmail(email);
    }

    //Проверяет, что authenticateUser возвращает false, если введенный пароль не совпадает с хэшированным паролем, хранящимся в базе данных.
    @Test
    void authenticateUser_InvalidPassword_ShouldReturnFalse() {
        // Arrange
        String email = "test@example.com";
        String plainPassword = "Password123";
        String hashedPassword = PasswordUtils.hashPassword("WrongPassword");

        when(dbController.getUserPasswordByEmail(email)).thenReturn(hashedPassword);

        // Act
        boolean success = userService.authenticateUser(email, plainPassword);

        // Assert
        assertFalse(success);
        verify(dbController).getUserPasswordByEmail(email);
    }

    //Проверить, что updateUser не возвращает ошибок, если переданы валидные данные пользователя.
    @Test
    void updateUser_ValidData_ShouldReturnEmptyErrors() {
        // Arrange
        int id = 1;
        String name = "Updated User";
        String email = "updated@example.com";
        String phone = "+375255125747";
        String plainPassword = "NewSomething14;";
        String address = "Updated Address";

        // Act
        ArrayList<AddUserError> errors = userService.updateUser(id, name, email, phone, plainPassword, address);

        // Assert
        assertTrue(errors.isEmpty());
        verify(dbController).updateUserInDB(id, name, email, phone, anyString(), address);
    }

    //Проверить, что updateUser возвращает ошибку INVALID_EMAIL, если передан некорректный email.
    @Test
    void updateUser_InvalidEmail_ShouldReturnInvalidEmailError() {
        // Arrange
        int id = 1;
        String name = "Updated User";
        String email = "invalid.email";
        String phone = "9876543210";
        String plainPassword = "NewPassword";
        String address = "Updated Address";

        // Act
        ArrayList<AddUserError> errors = userService.updateUser(id, name, email, phone, plainPassword, address);

        // Assert
        //assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_EMAIL));
        verify(dbController, never()).updateUserInDB(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    //Проверить, что updateUser возвращает ошибку INVALID_PHONE, если передан некорректный номер телефона
    @Test
    void updateUser_InvalidPhone_ShouldReturnInvalidPhoneError() {
        // Arrange
        int id = 1;
        String name = "Updated User";
        String email = "updated@example.com";
        String phone = "987654321"; // Invalid phone number
        String plainPassword = "NewPassword";
        String address = "Updated Address";

        // Act
        ArrayList<AddUserError> errors = userService.updateUser(id, name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_PHONE));
        verify(dbController, never()).updateUserInDB(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    //Проверить, что updateUser возвращает ошибки, связанные с паролем, если передан некорректный пароль.
    @Test
    void updateUser_InvalidPassword_ShouldReturnPasswordErrors() {
        // Arrange
        int id = 1;
        String name = "Updated User";
        String email = "updated@example.com";
        String phone = "9876543210";
        String plainPassword = "Pa#3"; // Invalid password (too short)
        String address = "Updated Address";

        // Act
        ArrayList<AddUserError> errors = userService.updateUser(id, name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PasswordErrorType.TOO_SHORT));
        verify(dbController, never()).updateUserInDB(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    // Проверяет, что функция deleteUser удаляет пользователя.
    @Test
    void deleteUser_ShouldCallDeleteUserFromDB() {
        // Arrange
        int userId = 1;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(dbController).deleteUserFromDB(userId);
    }

    //Проверяет, что функция getUserById возвращает пользователя по его идентификатору.
    @Test
    void getUserById_ShouldCallGetUserFromDB() {
        // Arrange
        int userId = 1;
        User user = new User(userId, "Test User", "test@example.com", "1234567890", "Password123", "Test Address");

        when(dbController.getUserFromDB(userId)).thenReturn(user);

        // Act
        User returnedUser = userService.getUserById(userId);

        // Assert
        assertEquals(user, returnedUser);
        verify(dbController).getUserFromDB(userId);
    }

    //Проверяет, что функция getAllUsers возвращает всех пользователей
    @Test
    void getAllUsers_ShouldCallGetAllUsers() {
        // Arrange
        ArrayList<User> users = new ArrayList<>(Arrays.asList(
                new User(1, "User 1", "user1@example.com", "1234567890", "Password123", "Address 1"),
                new User(2, "User 2", "user2@example.com", "9876543210", "Password456", "Address 2")
        ));

        when(dbController.getAllUsers()).thenReturn(users);

        // Act
        ArrayList<User> returnedUsers = userService.getAllUsers();

        // Assert
        assertEquals(users, returnedUsers);
        verify(dbController).getAllUsers();
    }
}