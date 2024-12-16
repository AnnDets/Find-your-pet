package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.JwtService;
import services.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/verifyToken")
public class TokenVerificationServlet extends HttpServlet {

    UserService userService; // Должно быть 256 бит
    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");

        // Проверка наличия заголовка Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"message\": \"Токен не предоставлен или некорректен.\"}");
            return;
        }
        String token = authHeader.substring(7); // Убираем "Bearer "
        boolean isValid = JwtService.verifyToken(token);
        if(isValid){

            // Если токен валиден, возвращаем статус 200
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Токен действителен.\"}");
        } else {
            // Если токен недействителен или истек, возвращаем 401
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"message\": \"Токен недействителен или истек.\"}");
        }
    }
}
