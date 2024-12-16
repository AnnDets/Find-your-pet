package servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Chat;
import models.User;
import services.ChatService;
import services.JwtService;
import services.ReportService;
import services.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/chats")
public class GetChatsServlet extends HttpServlet {
    private ChatService chatService;
    private UserService userService;
    @Override
    public void init() throws ServletException {
        // Получение ReportService из ServletContext
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

        String token = authHeader.substring(7); // Удаляем "Bearer "
        int userId;
        try {
            userId = JwtService.getUserFromToken(token).getId();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid token.\"}");
            return;
        }

        ArrayList<Chat> chats = chatService.getUserChats(userId);

        try (PrintWriter out = response.getWriter()) {
            out.write("[");
            for (int i = 0; i < chats.size(); i++) {
                Chat chat = chats.get(i);

                // Получение имени второго пользователя
                int otherUserId = (chat.getUser1Id() == userId) ? chat.getUser2Id() : chat.getUser1Id();
                String otherUserName = userService.getUserById(new User(otherUserId, "", "", "", "", "", "")).getName();

                out.write("{");
                out.write("\"id\": " + chat.getId() + ",");
                out.write("\"user1Id\": " + chat.getUser1Id() + ",");
                out.write("\"user2Id\": " + chat.getUser2Id() + ",");
                out.write("\"user2Name\": \"" + otherUserName + "\",");
                out.write("\"createdAt\": \"" + chat.getCreatedAt() + "\"");
                out.write("}");
                if (i < chats.size() - 1) {
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
