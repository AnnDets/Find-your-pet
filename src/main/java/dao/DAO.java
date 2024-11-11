package dao;

import models.User;

import java.util.ArrayList;

public interface DAO {
    boolean create(Object object);
    boolean update(Object object);
    boolean delete(Object object);
    Object read(Object object);
    ArrayList<Object> readAll();

}
