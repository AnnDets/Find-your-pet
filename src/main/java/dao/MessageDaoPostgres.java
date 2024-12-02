package dao;

import models.Message;
import utils.LogUtil;

import java.sql.*;
import java.util.ArrayList;

public class MessageDaoPostgres {
    private final Connection connection;

    public MessageDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    //@Override
    public boolean create(Message message) {
        String query = "INSERT INTO messages (chat_id, user_id, content, sent_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, message.getChatId());
            stmt.setInt(2, message.getUserId());
            stmt.setString(3, message.getContent());
            stmt.setTimestamp(4, message.getSentAt());
            stmt.executeUpdate();
            LogUtil.info("Message добавлено успешно: " + message);
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении сообщения", e);
            return false;
        }
    }

    //@Override
    public ArrayList<Message> getMessagesByChatId(int chatId) {
        ArrayList<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM messages WHERE chat_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, chatId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("id"),
                        rs.getInt("chat_id"),
                        rs.getInt("user_id"),
                        rs.getString("content"),
                        rs.getTimestamp("sent_at")
                ));
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении сообщений чата", e);
        }
        return messages;
    }

    //@Override
    public boolean delete(int id) {
        String query = "DELETE FROM messages WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LogUtil.info("Message удалено: " + id);
                return true;
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при удалении сообщения", e);
        }
        return false;
    }
}
