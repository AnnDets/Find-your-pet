package dao;

import models.User;
import utils.PasswordUtils;

import java.util.ArrayList;
import java.util.Optional;

public class UserDAOFake extends UserDao {
    private final ArrayList<User> users = new ArrayList<>();
    private int currentId = 1;

    @Override
    public String readByLogin(String login) {
        return users.stream()
                .filter(user -> user.getEmail().equals(login))
                .map(User::getPasswordHash)
                .findFirst()
                .orElse(null);
    }

    @Override
    public User readByID(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean create(Object object) {
        if (object instanceof User user && users.stream().noneMatch(u -> u.getEmail().equals(user.getEmail()))) {
            user.setId(currentId++);
            users.add(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Object object) {
        if (object instanceof User user) {
            Optional<User> existingUser = users.stream()
                    .filter(u -> u.getId() == user.getId())
                    .findFirst();
            existingUser.ifPresent(u -> {
                u.setEmail(user.getEmail());
                u.setPhone(user.getPhone());
                u.setPasswordHash(user.getPasswordHash());
            });
            return existingUser.isPresent();
        }
        return false;
    }

    @Override
    public boolean delete(Object object) {
        if (object instanceof User user) {
            return users.removeIf(u -> u.getId() == user.getId());
        }
        return false;
    }

    @Override
    public Object read(Object object) {
        if (object instanceof User user) {
            return readByID(user.getId());
        }
        return null;
    }

    @Override
    public ArrayList<Object> readAll() {
        return new ArrayList<>(users);
    }
}
