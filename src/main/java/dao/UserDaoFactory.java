package dao;

import utils.LogUtil;

import java.sql.Connection;

public class UserDaoFactory {
    private static UserDaoFactory instance;

    private UserDaoFactory() {}
    public static synchronized UserDaoFactory getInstance() {
        if (instance == null) {
            instance = new UserDaoFactory();
        }
        return instance;
    }

    // Фабричный метод для создания UserDao
    public UserDao createUserDao(final String type, final Connection connection) {
        UserDao userDao;
        userDao = switch (type) {
            case "postgres" -> new UserDaoPostgreSQL(connection);
            case "fake" -> new UserDAOFake();
            default -> throw new
                    IllegalArgumentException("Unknown UserDao type: " + type);

        };
        return userDao;
    }
}


