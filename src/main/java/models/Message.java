package models;

import java.sql.Timestamp;

public class Message {
    private int id;
    private int chatId;
    private int userId;
    private String content;
    private Timestamp sentAt;

    public Message(int id, int chatId, int userId, String content, Timestamp sentAt) {
        this.id = id;
        this.chatId = chatId;
        this.userId = userId;
        this.content = content;
        this.sentAt = sentAt;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }
}
