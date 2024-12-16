package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Message;
import services.JwtService;
import services.MessageService;

import java.io.IOException;

@WebServlet("/chats/messages/send")
public class SendMessageServlet extends HttpServlet {
    private MessageService messageService;

    @Override
    public void init() throws ServletException {
        // Получение MessageService из ServletContext
        messageService = (MessageService) getServletContext().getAttribute("messageService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Проверка токена
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Missing or invalid Authorization header.\"}");
            return;
        }

        String token = authHeader.substring(7);
        int userId;
        try {
            userId = JwtService.getUserFromToken(token).getId();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid token.\"}");
            return;
        }

        // Чтение параметров запроса
        String chatIdParam = request.getParameter("chatId");
        String messageContent = request.getParameter("content");

        if (chatIdParam == null || messageContent == null || messageContent.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing chatId or content.\"}");
            return;
        }

        int chatId;
        try {
            chatId = Integer.parseInt(chatIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid chatId.\"}");
            return;
        }

        // Создание сообщения
        Message message = new Message(chatId, userId, messageContent);

        // Сохранение сообщения
        try {
            messageService.sendMessage(message);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error saving message.\"}");
        }
    }
}
