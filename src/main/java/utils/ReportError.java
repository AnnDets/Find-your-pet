package utils;

public enum ReportError {
    MISSING_USER_ID("Не указан пользователь"),
    MISSING_FOUND_DATE("Не указана дата нахождения"),
    MISSING_LOCATION("Не указано место нахождения"),
    DB_ERROR("Ошибка базы данных");

    private final String message;

    ReportError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
