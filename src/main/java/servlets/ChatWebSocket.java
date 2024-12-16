package servlets;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import services.MessageService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chat")
public class ChatWebSocket {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    @OnOpen
    public void onOpen(Session session) {
        String chatId = session.getRequestParameterMap().get("chatId").get(0);
        System.out.println("Подключен чат: " + chatId);
        session.getUserProperties().put("chatId", chatId);
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session senderSession) {
        synchronized (sessions) {
            sessions.forEach(session -> {
                try {
                    if (session.isOpen() && !session.equals(senderSession)) {
                        session.getBasicRemote().sendText(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessions.remove(session);
        throwable.printStackTrace();
    }
}
