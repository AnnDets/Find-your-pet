package dao;

import utils.LogUtil;

import java.sql.Connection;
public class ChatDaoFactory {
    private static ChatDaoFactory instance;

    private ChatDaoFactory() {}
    public static synchronized ChatDaoFactory getInstance() {
        if (instance == null) {
            instance = new ChatDaoFactory();
        }
        return instance;
    }

    // Фабричный метод для создания ChatDao
    public ChatDao createChatDao(final String type, final Connection connection) {
        ChatDao chatDao;
        chatDao = switch (type) {
            case "postgres" -> new ChatDaoPostgres(connection);
            case "fake" -> new ChatDaoPostgres(connection);
            default -> throw new
                    IllegalArgumentException("Unknown ChatDao type: " + type);

        };
        return chatDao;
    }
}


