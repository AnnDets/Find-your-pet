package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;
import org.json.JSONObject;
import services.JwtService;
import services.UserService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        JSONObject jsonRequest = new JSONObject(body);

        String username = jsonRequest.optString("username");
        String password = jsonRequest.optString("password");

        if (username.isEmpty() || password.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("error", "Username and password are required.");
                out.print(errorResponse.toString());
            }
            return;
        }

        boolean isAuth = userService.authenticateUser(username, password);
        if (!isAuth){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("error", "Invalid username or password.");
                out.print(errorResponse.toString());
            }
        }
        User user = userService.getUserByLogin(username);
        String token = JwtService.generateToken(user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                JSONObject successResponse = new JSONObject();
                successResponse.put("token", token);
                out.print(successResponse.toString());
            }
    }
}
