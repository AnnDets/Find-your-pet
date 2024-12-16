package dao;

import models.Chat;
import models.Message;

import java.util.ArrayList;

public abstract class ChatDao implements DAO {
    public abstract Object readByID(int id);
    public abstract ArrayList<Chat> readUsersChats(int userId);

    public abstract ArrayList<Message> readChatMessages(int chatId);
}