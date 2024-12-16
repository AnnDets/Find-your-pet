package services;

import dao.ChatDaoFactory;
import dao.MessageDao;
import dao.MessageDaoFactory;
import models.Message;

import java.sql.Connection;
import java.util.ArrayList;

public class MessageService {
    private final MessageDao messageDao;

    public MessageService(Connection connection) {
        MessageDaoFactory messageDaoFactory = MessageDaoFactory.getInstance();
        messageDao = messageDaoFactory.createMessageDao("postgres", connection);
    }

    // Метод для отправки сообщения
    public boolean sendMessage(Message message) {
        return messageDao.create(message);
    }

    // Метод для получения сообщений по chatId
    public ArrayList<Message> getMessagesByChatId(int chatId) {
        return messageDao.getMessagesByChatId(chatId);
    }

    // Метод для удаления сообщения
    public boolean deleteMessage(Message message) {
        return messageDao.delete(message);
    }
}
