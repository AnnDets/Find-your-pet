package services;

import dao.ChatDao;
import dao.ChatDaoFactory;
import dao.ReportDao;
import dao.ReportDaoFactory;
import models.Chat;
import models.Message;
import utils.LogUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ChatService {
    private final ChatDao chatDao;
    public ChatService(Connection connection) {
        ChatDaoFactory chatDaoFactory = ChatDaoFactory.getInstance();
        chatDao = chatDaoFactory.createChatDao("postgres", connection);
    }
    public boolean createChat(Chat chat) {
        return chatDao.create(chat);
    }

    public ArrayList<Chat> getUserChats(int userId) {
        return chatDao.readUsersChats(userId);
    }

    public boolean deleteChat(int chatId) {
        return chatDao.delete(chatId);
    }

    public ArrayList<Message> getChatMessages(int chatId) {
        return chatDao.readChatMessages(chatId);
    }


    public boolean isUserInChat(int userId, int chatId) {
        return true;
    }

    public ArrayList<Message> getMessages(int chatId) {
        return chatDao.readChatMessages(chatId);
    }
}
