package dao;

import models.Chat;
import models.Message;
import utils.LogUtil;

import java.sql.*;
import java.util.ArrayList;

public class ChatDaoPostgres extends ChatDao{
    private final Connection connection;

    public ChatDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Object obj) {
        Chat chat = (Chat) obj;
        String checkQuery = "SELECT COUNT(*) FROM chats WHERE (user1_id = ? AND user2_id = ?) OR (user1_id = ? AND user2_id = ?)";
        String insertQuery = "INSERT INTO chats (user1_id, user2_id, created_at) VALUES (?, ?, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            // Проверяем наличие существующего чата
            checkStmt.setInt(1, chat.getUser1Id());
            checkStmt.setInt(2, chat.getUser2Id());
            checkStmt.setInt(3, chat.getUser2Id());
            checkStmt.setInt(4, chat.getUser1Id());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                LogUtil.warn("Чат уже существует между пользователями: " + chat.getUser1Id() + " и " + chat.getUser2Id());
                return false;
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при проверке существующего чата", e);
            return false;
        }

        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            // Создаём новый чат
            insertStmt.setInt(1, chat.getUser1Id());
            insertStmt.setInt(2, chat.getUser2Id());
            insertStmt.setTimestamp(3, chat.getCreatedAt());
            insertStmt.executeUpdate();
            LogUtil.info("Chat создан успешно: " + chat);
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при создании чата", e);
            return false;
        }
    }


    @Override
    public boolean update(Object object) {
        return false;
    }

    @Override
    public Object read(Object object) {
        int id = ((Chat)object).getId();
        String query = "SELECT * FROM chats WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Chat(
                        rs.getInt("id"),
                        rs.getInt("user1_id"),
                        rs.getInt("user2_id"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при чтении чата", e);
        }
        return null;
    }

    @Override
    public boolean delete(Object object) {
        int id = ((Chat)object).getId();
        String query = "DELETE FROM chats WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LogUtil.info("Chat удалён: " + id);
                return true;
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при удалении чата", e);
        }
        return false;
    }

    public ArrayList<Object> readAll() {
        ArrayList<Object> chats = new ArrayList<>();
        String query = "SELECT * FROM chats";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                chats.add(new Chat(
                        rs.getInt("id"),
                        rs.getInt("user1_id"),
                        rs.getInt("user2_id"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении списка чатов", e);
        }
        return chats;
    }
    @Override
    public Object readByID(int id) {
        String query = "SELECT * FROM chats WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Chat(
                        rs.getInt("id"),
                        rs.getInt("user1_id"),
                        rs.getInt("user2_id"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при чтении чата", e);
        }
        return null;
    }

    @Override
    public ArrayList<Chat> readUsersChats(int userId) {
        ArrayList<Chat> chats = new ArrayList<>();
        String query = "SELECT id, user1_id, user2_id, created_at FROM chats WHERE user1_id = ? OR user2_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Chat chat = new Chat(
                        rs.getInt("id"),
                        rs.getInt("user1_id"),
                        rs.getInt("user2_id"),
                        rs.getTimestamp("created_at")
                );
                chats.add(chat);
            }

            LogUtil.info("Чаты пользователя с ID " + userId + " успешно загружены.");
        } catch (SQLException e) {
            LogUtil.error("Ошибка при загрузке чатов пользователя с ID " + userId, e);
        }

        return chats;
    }

    @Override
    public ArrayList<Message> readChatMessages(int chatId) {
        ArrayList<Message> messages = new ArrayList<>();
        String query = "SELECT id, chat_id, user_id, content, sent_at FROM messages WHERE chat_id = ? ORDER BY sent_at ASC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, chatId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("id"),
                        rs.getInt("chat_id"),
                        rs.getInt("user_id"),
                        rs.getString("content"),
                        rs.getTimestamp("sent_at")
                );
                messages.add(message);
            }

            LogUtil.info("Сообщения для чата с ID " + chatId + " успешно загружены.");
        } catch (SQLException e) {
            LogUtil.error("Ошибка при загрузке сообщений для чата с ID " + chatId, e);
        }

        return messages;
    }

}
