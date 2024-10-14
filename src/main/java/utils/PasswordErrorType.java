package utils;

public enum PasswordErrorType implements AddUserError {
    TOO_SHORT("Пароль должен быть не менее 8 символов."),
    NO_UPPERCASE("Пароль должен содержать хотя бы одну заглавную букву."),
    NO_LOWERCASE("Пароль должен содержать хотя бы одну строчную букву."),
    NO_DIGIT("Пароль должен содержать хотя бы одну цифру."),
    NO_SPECIAL_CHAR("Пароль должен содержать хотя бы один специальный символ."),
    CONTAINS_POPULAR_SUBSTRING("Пароль содержит популярные подстроки, такие как 'qwerty', '123', и т.д.");

    private final String message;

    PasswordErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
