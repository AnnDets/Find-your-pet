package tests;

import DBControllers.DBController;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import services.UserController;
import utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private DBController dbController;

    @BeforeEach
    void setUp() {
        dbController = Mockito.mock(DBController.class);
        userController = new UserController(dbController);
    }

    @Test
    void addUser_ValidData_ShouldReturnEmptyErrors() {
        // Arrange
        String name = "John Doe";
        String email = "john.doe@example.com";
        String phone = "1234567890";
        String plainPassword = "Password123";
        String address = "123 Main St";

        when(dbController.addUserToDB(name, email, phone, anyString(), address)).thenReturn(true);

        // Act
        ArrayList<AddUserError> errors = userController.addUser(name, email, phone, plainPassword, address);

        // Assert
        assertTrue(errors.isEmpty());
        verify(dbController).addUserToDB(name, email, phone, anyString(), address);
    }

    @Test
    void addUser_InvalidEmail_ShouldReturnInvalidEmailError() {
        // Arrange
        String name = "John Doe";
        String email = "invalid_email";
        String phone = "1234567890";
        String plainPassword = "Password123";
        String address = "123 Main St";

        // Act
        ArrayList<AddUserError> errors = userController.addUser(name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_EMAIL));
        verify(dbController, never()).addUserToDB(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void addUser_InvalidPhone_ShouldReturnInvalidPhoneError() {
        // Arrange
        String name = "John Doe";
        String email = "john.doe@example.com";
        String phone = "123456789"; // Invalid phone number
        String plainPassword = "Password123";
        String address = "123 Main St";

        // Act
        ArrayList<AddUserError> errors = userController.addUser(name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_PHONE));
        verify(dbController, never()).addUserToDB(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void addUser_InvalidPassword_ShouldReturnPasswordErrors() {
        // Arrange
        String name = "John Doe";
        String email = "john.doe@example.com";
        String phone = "1234567890";
        String plainPassword = "Pass"; // Invalid password
        String address = "123 Main St";

        when(PasswordUtils.validatePassword(plainPassword)).thenReturn((ArrayList<PasswordErrorType>) Arrays.asList(PasswordErrorType.TOO_SHORT));

        // Act
        ArrayList<AddUserError> errors = userController.addUser(name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PasswordErrorType.TOO_SHORT));
        verify(dbController, never()).addUserToDB(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void addUser_EmailAlreadyExists_ShouldReturnEmailAlreadyExistsError() {
        // Arrange
        String name = "John Doe";
        String email = "john.doe@example.com";
        String phone = "1234567890";
        String plainPassword = "Password123";
        String address = "123 Main St";

        when(dbController.addUserToDB(name, email, phone, anyString(), address)).thenReturn(false);

        // Act
        ArrayList<AddUserError> errors = userController.addUser(name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.EMAIL_ALREADY_EXISTS));
        verify(dbController).addUserToDB(name, email, phone, anyString(), address);
    }

    @Test
    void authenticateUser_ValidCredentials_ShouldReturnTrue() {
        // Arrange
        String email = "john.doe@example.com";
        String plainPassword = "Password123";
        String hashedPassword = "hashedPassword";

        when(dbController.getUserPasswordByEmail(email)).thenReturn(hashedPassword);
        when(PasswordUtils.checkPassword(plainPassword, hashedPassword)).thenReturn(true);

        // Act
        boolean isAuthenticated = userController.authenticateUser(email, plainPassword);

        // Assert
        assertTrue(isAuthenticated);
        verify(dbController).getUserPasswordByEmail(email); // Verify the DBController call
        // No need to verify PasswordUtils.checkPassword here as it's called internally
    }


    @Test
    void authenticateUser_InvalidCredentials_ShouldReturnFalse() {
        // Arrange
        String email = "john.doe@example.com";
        String plainPassword = "Password123";

        when(dbController.getUserPasswordByEmail(email)).thenReturn(null); // Returns null, indicating no user found

        // Act
        boolean isAuthenticated = userController.authenticateUser(email, plainPassword);

        // Assert
        assertFalse(isAuthenticated);
        verify(dbController).getUserPasswordByEmail(email); // Verify the DBController call
        //verify(PasswordUtils, never()).checkPassword(anyString(), anyString()); // Verify PasswordUtils.checkPassword was never called
    }

    @Test
    void updateUser_ValidData_ShouldUpdateTheUser() {
        // Arrange
        int id = 1;
        String name = "John Doe";
        String email = "john.doe@example.com";
        String phone = "1234567890";
        String plainPassword = "Password123";
        String address = "123 Main St";

        // Act
        ArrayList<AddUserError> errors = userController.updateUser(id, name, email, phone, plainPassword, address);

        // Assert
        assertTrue(errors.isEmpty());
        verify(dbController).updateUserInDB(id, name, email, phone, anyString(), address);
    }

    @Test
    void updateUser_InvalidEmail_ShouldReturnInvalidEmailError() {
        // Arrange
        int id = 1;
        String name = "John Doe";
        String email = "invalid_email";
        String phone = "1234567890";
        String plainPassword = "Password123";
        String address = "123 Main St";

        // Act
        ArrayList<AddUserError> errors = userController.updateUser(id, name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_EMAIL));
        verify(dbController, never()).updateUserInDB(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void updateUser_InvalidPhone_ShouldReturnInvalidPhoneError() {
        // Arrange
        int id = 1;
        String name = "John Doe";
        String email = "john.doe@example.com";
        String phone = "123456789"; // Invalid phone number
        String plainPassword = "Password123";
        String address = "123 Main St";

        // Act
        ArrayList<AddUserError> errors = userController.updateUser(id, name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PhoneAndEmailError.INVALID_PHONE));
        verify(dbController, never()).updateUserInDB(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void updateUser_InvalidPassword_ShouldReturnPasswordErrors() {
        // Arrange
        int id = 1;
        String name = "John Doe";
        String email = "john.doe@example.com";
        String phone = "1234567890";
        String plainPassword = "Pass"; // Invalid password
        String address = "123 Main St";

        when(PasswordUtils.validatePassword(plainPassword)).thenReturn((ArrayList<PasswordErrorType>) Arrays.asList(PasswordErrorType.TOO_SHORT));

        // Act
        ArrayList<AddUserError> errors = userController.updateUser(id, name, email, phone, plainPassword, address);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(PasswordErrorType.TOO_SHORT));
        verify(dbController, never()).updateUserInDB(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void deleteUser_ShouldDeleteTheUser() {
        // Arrange
        int userId = 1;

        when(dbController.deleteUserFromDB(userId)).thenReturn(true);

        // Act
        boolean success = userController.deleteUser(userId);

        // Assert
        assertTrue(success);
        verify(dbController).deleteUserFromDB(userId);
    }

    @Test
    void getUserById_ShouldReturnTheUser() {
        // Arrange
        int userId = 1;
        User user = new User(userId, "John Doe", "john.doe@example.com", "1234567890", "hashedPassword", "123 Main St");

        when(dbController.getUserFromDB(userId)).thenReturn(user);

        // Act
        User returnedUser = userController.getUserById(userId);

        // Assert
        assertEquals(user, returnedUser);
        verify(dbController).getUserFromDB(userId);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        User user1 = new User(1, "John Doe", "john.doe@example.com", "1234567890", "hashedPassword", "123 Main St");
        User user2 = new User(2, "Jane Doe", "jane.doe@example.com", "9876543210", "hashedPassword", "456 Oak St");
        List<User> users = Arrays.asList(user1, user2);

        when(userController.getAllUsers()).thenReturn(new ArrayList<>(users));
        //when(dbController.getAllUsers()).thenReturn(new ArrayList<>(users));

        // Act
        ArrayList<User> returnedUsers = userController.getAllUsers();

        // Assert
        assertEquals(users, returnedUsers);
        verify(dbController).getAllUsers();
    }
}

