package dao;

import models.User;

public abstract class UserDao implements DAO {
    public abstract Object readByLogin(String login);
    abstract boolean logInUser(String login, String plainPassword);
    public abstract Object readByID(int id);
}
