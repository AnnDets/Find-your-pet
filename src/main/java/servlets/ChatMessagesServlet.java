package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Message;
import models.User;
import services.ChatService;
import services.JwtService;
import services.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/chats/messages")
public class ChatMessagesServlet extends HttpServlet {
    private ChatService chatService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        // Инициализация ChatService из ServletContext
        chatService = (ChatService) getServletContext().getAttribute("chatService");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Missing or invalid Authorization header.\"}");
            return;
        }

        String token = authHeader.substring(7); // Убираем "Bearer "
        int userId;
        try {
            userId = JwtService.getUserFromToken(token).getId();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid token.\"}");
            return;
        }

        String chatIdParam = request.getParameter("chatId");
        if (chatIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Chat ID is required.\"}");
            return;
        }

        int chatId;
        try {
            chatId = Integer.parseInt(chatIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid Chat ID.\"}");
            return;
        }

        // Проверяем, что пользователь имеет доступ к чату
        if (!chatService.isUserInChat(userId, chatId)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"You do not have access to this chat.\"}");
            return;
        }

        // Получаем сообщения из чата
        List<Message> messages = chatService.getMessages(chatId);

        // Отправляем JSON-ответ
        try (PrintWriter out = response.getWriter()) {
            out.write("[");
            for (int i = 0; i < messages.size(); i++) {
                Message message = messages.get(i);
                int senderId = message.getUserId();
                String senderName = userService.getUserById(new User(senderId, "", "", "", "","")).getName();
                out.write("{");
                out.write("\"id\": " + message.getId() + ",");
                out.write("\"senderId\": " + senderId + ",");
                out.write("\"senderName\": \"" + senderName + "\",");
                out.write("\"content\": \"" + message.getContent() + "\",");
                out.write("\"timestamp\": \"" + message.getSentAt() + "\"");
                out.write("}");
                if (i < messages.size() - 1) {
                    out.write(",");
                }
            }
            out.write("]");
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error processing request.\"}");
        }
    }
}
