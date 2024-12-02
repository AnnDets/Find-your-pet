package dao;

import models.User;
import utils.LogUtil;

import java.sql.*;
import java.util.ArrayList;

public class UserDaoPostgres extends UserDao {
    private final Connection connection_;

    public UserDaoPostgres(Connection connection) {
        this.connection_ = connection;
    }

    @Override
    public Object readByLogin(String login) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password_hash"),
                        "",
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении пользователя: ", e);
        }
        return null;
    }

    @Override
    public Object readByID(int id) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password_hash"),
                        "",
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении пользователя: ", e);
        }
        return null;
    }

    @Override
    public boolean create(Object object) {
        User user = (User)object;
        String query = "INSERT INTO users (name, email, phone, password_hash, address) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getPasswordHash());
            stmt.setString(5, user.getAddress());
            stmt.executeUpdate();
            LogUtil.info("Пользователь добавлен: " + user.getName());
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при добавлении пользователя: ", e);
            return false;
        }
    }

    @Override
    public boolean update(Object object) {
        User user = (User)object;
        String query = "UPDATE users SET name = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getAddress());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
            LogUtil.info("Пользователь обновлен: " + user.getName());
            return true;
        } catch (SQLException e) {
            LogUtil.error("Ошибка при обновлении пользователя: ", e);
            return false;
        }
    }

    @Override
    public boolean delete(Object user) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            stmt.setInt(1, ((User)user).getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LogUtil.info("Пользователь удален с ID: " + ((User)user).getId());
                return true;
            } else {
                LogUtil.warn("Пользователь с ID " + ((User)user).getId() + " не найден.");
                return false;
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при удалении пользователя: ", e);
            return false;
        }
    }

    @Override
    public Object read(Object object) {
        return null;
    }

    @Override
    public ArrayList<Object> readAll() {
        ArrayList<Object> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (PreparedStatement stmt = connection_.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password_hash"),
                        "",
                        rs.getString("address")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            LogUtil.error("Ошибка при получении всех пользователей: ", e);
        }
        return users;
    }

}
