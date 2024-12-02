package dao;

import utils.LogUtil;

import java.sql.Connection;

public class ReportDaoFactory {
    private static ReportDaoFactory instance;

    private ReportDaoFactory() {}
    public static synchronized ReportDaoFactory getInstance() {
        if (instance == null) {
            instance = new ReportDaoFactory();
        }
        return instance;
    }

    // Фабричный метод для создания UserDao
    public ReportDao  createUserDao(final String type, final Connection connection) {
        ReportDao reportDao;
        reportDao = switch (type) {
            case "postgres" -> new ReportDaoPostgres(connection);
            //case "fake" -> new ReportDAOFake();
            default -> throw new
                    IllegalArgumentException("Unknown UserDao type: " + type);

        };
        return reportDao;
    }
}


