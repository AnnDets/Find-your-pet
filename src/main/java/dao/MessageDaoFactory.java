package dao;

import utils.LogUtil;

import java.sql.Connection;

public class MessageDaoFactory {
    private static MessageDaoFactory instance;

    private MessageDaoFactory() {}
    public static synchronized MessageDaoFactory getInstance() {
        if (instance == null) {
            instance = new MessageDaoFactory();
        }
        return instance;
    }

    // Фабричный метод для создания UserDao
    public MessageDao  createMessageDao(final String type, final Connection connection) {
        MessageDao messageDao;
        messageDao = switch (type) {
            case "postgres" -> new MessageDaoPostgres(connection);
            //case "fake" -> new MessageDAOFake();
            default -> throw new
                    IllegalArgumentException("Unknown UserDao type: " + type);

        };
        return messageDao;
    }
}


