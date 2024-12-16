package servlets;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.JwtService;
import services.UserService;
import models.User;
import utils.JSONParser;

import java.io.IOException;

@WebServlet("/user")
public class UserProfileServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getHeader("token");
        DecodedJWT decode = JWT.decode(token);
        Integer userId = decode.getClaim("id").asInt();
        if (!JwtService.verifyToken(token)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Недействительный токен\"}");
            return;
        }
        User user = userService.getUserById(new User(userId, "","","", "","",""));
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Пользователь не найден\"}");
            return;
        }

        // Возвращаем данные пользователя
        resp.setContentType("application/json");
        resp.getWriter().write(JSONParser.userToJSON(user).toString());
    }
}
