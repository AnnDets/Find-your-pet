package dao;

import models.Chat;
import utils.LogUtil;

import java.sql.*;
import java.util.ArrayList;

public class ChatDaoPostgres{
    private final Connection connection;

    public ChatDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    public boolean create(Chat chat) {
        String query = "INSERT INTO chats (user1_id, user2_id, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, chat.getUser1Id());
            stmt.setInt(2, chat.getUser2Id());
            stmt.setTimestamp(3, chat.getCreatedAt());
            stmt.executeUpdate();
            LogUtil.info("Chat создан успешно: " + chat);
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при создании чата", e);
            return false;
        }
    }

    public Chat read(int id) {
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


    public boolean delete(int id) {
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


    public ArrayList<Chat> getAll() {
        ArrayList<Chat> chats = new ArrayList<>();
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
}
