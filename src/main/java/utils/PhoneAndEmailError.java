package utils;


/**
 * Enum representing different error types related to phone and email validation.
 * Each enum constant has an associated error message.
 */
public enum PhoneAndEmailError implements AddUserError {
    EMAIL_ALREADY_EXISTS("Этот email уже занят"),
    INVALID_PHONE("Неверный номер"),
    INVALID_EMAIL("Неверный email");

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    PhoneAndEmailError(String message) {
        this.message = message;
    }
}
