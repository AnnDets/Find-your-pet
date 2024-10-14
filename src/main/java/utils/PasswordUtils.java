package utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import  utils.PasswordErrorType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PasswordUtils {

    // Метод для хэширования пароля
    public static String hashPassword(String plainPassword) {
        // Хэширование пароля с добавлением соли
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    // Метод для проверки соответствия введенного пароля и хэшированного
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        // Сравнение введенного пароля с хэшированным
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
        return result.verified;
    }

     private static boolean containsPopularSubstring(String password) {
         String[] popularSubstrings = {"qwerty", "123", "password", "abc", "admin", "letmein"};

         for (String substring : popularSubstrings) {
             if (password.toLowerCase().contains(substring)) {
                 return true;
             }
         }
         return false;
     }

     // Метод для проверки пароля
     public static ArrayList<PasswordErrorType> validatePassword(String password) {
         ArrayList<PasswordErrorType> errors = new ArrayList<>();

         if (password.length() < 8) {
             errors.add(PasswordErrorType.TOO_SHORT);
         }

         boolean hasUppercase = false;
         boolean hasLowercase = false;
         boolean hasDigit = false;
         boolean hasSpecialChar = false;

         for (char c : password.toCharArray()) {
             if (Character.isUpperCase(c)) {
                 hasUppercase = true;
             } else if (Character.isLowerCase(c)) {
                 hasLowercase = true;
             } else if (Character.isDigit(c)) {
                 hasDigit = true;
             } else if (!Character.isLetterOrDigit(c)) {
                 hasSpecialChar = true;
             }
         }

         if (!hasUppercase) {
             errors.add(PasswordErrorType.NO_UPPERCASE);
         }
         if (!hasLowercase) {
             errors.add(PasswordErrorType.NO_LOWERCASE);
         }
         if (!hasDigit) {
             errors.add(PasswordErrorType.NO_DIGIT);
         }
         if (!hasSpecialChar) {
             errors.add(PasswordErrorType.NO_SPECIAL_CHAR);
         }

         if (containsPopularSubstring(password)) {
             errors.add(PasswordErrorType.CONTAINS_POPULAR_SUBSTRING);
         }
         if(!errors.isEmpty()){
         }
         return errors;
     }
}

