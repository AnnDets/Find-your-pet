package tests;

import dao.UserDao;
import dao.UserDaoFactory;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import services.UserService;
import utils.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserDao userDaoMock;
    private Connection connectionMock;

    void testAddUser_validUser() {
        User validUser = new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");

        when(PhoneAndEmailValidator.isEmailValid(validUser.getEmail())).thenReturn(true);
        when(PhoneAndEmailValidator.isValidNumber(validUser.getPhone(), validUser.getAddress())).thenReturn(true);

        // Создаем пустой список ошибок
        ArrayList<PasswordErrorType> emptyErrors = new ArrayList<>();
        when(PasswordUtils.validatePassword(validUser.getPlainPassword())).thenReturn(emptyErrors);

        when(userDaoMock.read(validUser)).thenReturn(null);
        when(userDaoMock.create(validUser)).thenReturn(true);

        ArrayList<AddUserError> errors = userService.addUser(validUser);

        assertTrue(errors.isEmpty());
        verify(userDaoMock, times(1)).create(validUser);
    }

    @Test
    void testAddUser_invalidEmail() {
        User invalidEmailUser = new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");

        when(PhoneAndEmailValidator.isEmailValid(invalidEmailUser.getEmail())).thenReturn(false);

        ArrayList<AddUserError> errors = userService.addUser(invalidEmailUser);

        assertFalse(errors.isEmpty());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_EMAIL));
        verify(userDaoMock, never()).create(any(User.class));
    }

    @Test
    void testAuthenticateUser_success() {
        String email = "john.doe@example.com";
        String plainPassword = "password123";
        String hashedPassword = "hashedPassword123";

        User mockUser = new User(1,"Test User","user@mail.com",
                "1234567890", "Pewrvrvg#d135","Test Address");
        mockUser.setPasswordHash(hashedPassword);

        when(userDaoMock.readByLogin(email)).thenReturn(mockUser);
        when(PasswordUtils.checkPassword(plainPassword, hashedPassword)).thenReturn(true);

        boolean isAuthenticated = userService.authenticateUser(email, plainPassword);

        assertTrue(isAuthenticated);
    }

    @Test
    void testAuthenticateUser_wrongPassword() {
        String email = "john.doe@example.com";
        String plainPassword = "wrongPassword";
        String hashedPassword = "hashedPassword123";

        User mockUser = new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");
        mockUser.setPasswordHash(hashedPassword);

        when(userDaoMock.readByLogin(email)).thenReturn(mockUser);
        when(PasswordUtils.checkPassword(plainPassword, hashedPassword)).thenReturn(false);

        boolean isAuthenticated = userService.authenticateUser(email, plainPassword);

        assertFalse(isAuthenticated);
    }

    @Test
    void testUpdateUser_validData() {
        // Создаем тестового пользователя с валидными данными
        User validUser = new User(1, "Test User", "test@example.com",
                "1234567890", "Pewrvrvg#d135", "Test Address");

        // Моки для валидации email, телефона и пароля
        when(PhoneAndEmailValidator.isEmailValid(validUser.getEmail())).thenReturn(true);
        when(PhoneAndEmailValidator.isValidNumber(validUser.getPhone(), validUser.getAddress())).thenReturn(true);

        // Пустой список ошибок для пароля
        ArrayList<PasswordErrorType> emptyErrors = new ArrayList<>();
        when(PasswordUtils.validatePassword(validUser.getPlainPassword())).thenReturn(emptyErrors);

        // Вызов метода
        ArrayList<AddUserError> errors = userService.updateUser(validUser);

        // Проверяем, что ошибок нет
        assertTrue(errors.isEmpty());

        // Проверяем, что метод update был вызван
        String hashedPassword = PasswordUtils.hashPassword(validUser.getPlainPassword());
        validUser.setPasswordHash(hashedPassword); // Обновляем объект для проверки
        verify(userDaoMock, times(1)).update(validUser);
    }


    @Test
    void testDeleteUser_success() {
        User userToDelete = new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");
        userToDelete.setId(1);

        when(userDaoMock.delete(userToDelete)).thenReturn(true);

        boolean result = userService.deleteUser(userToDelete);

        assertTrue(result);
        verify(userDaoMock, times(1)).delete(userToDelete);
    }

    @Test
    void testGetUserById() {
        User user= new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");
        User mockUser = user;
        mockUser.setId(1);

        when(userDaoMock.read(mockUser)).thenReturn(mockUser);

        User fetchedUser = userService.getUserById(mockUser);

        assertNotNull(fetchedUser);
        assertEquals(mockUser.getId(), fetchedUser.getId());
    }

    @Test
    void testGetAllUsers() {
        List<Object> mockUsers = new ArrayList<>();
        User user= new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");
        mockUsers.add(user);

        when(userDaoMock.readAll()).thenReturn((ArrayList<Object>) mockUsers);

        ArrayList<Object> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
    }
}
