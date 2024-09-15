package utils;

public enum PhoneAndEmailError implements AddUserError {
    EMAIL_ALREADY_EXISTS("Этот email уже занят"),
    INVALID_PHONE("Неверный номер"),
    INVALID_EMAIL("Неверный email");

    private final String message;
    @Override
    public String getMessage() {
        return message;
    }
    PhoneAndEmailError(String message){
        this.message = message;
    }
}
