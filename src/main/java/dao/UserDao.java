package dao;

import models.User;

public abstract class UserDao implements DAO {
    public abstract Object readByLogin(String login);
    public abstract Object readByID(int id);
}
