package dao;

import models.Message;

import java.util.ArrayList;

public abstract class MessageDao implements DAO{
    @Override
    abstract public boolean create(Object object);

    @Override
    abstract public boolean update(Object object);

    public abstract ArrayList<Message> getMessagesByChatId(int chatId);

    @Override
    abstract public boolean delete(Object object) ;

    @Override
    abstract public Object read(Object object) ;

    @Override
    abstract public ArrayList<Object> readAll();

}
