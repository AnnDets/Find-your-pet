package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import services.UserService;
import utils.AddUserError;
import utils.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        // Получение UserService из ServletContext
        userService = (UserService) getServletContext().getAttribute("userService");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Чтение JSON из тела запроса
        StringBuilder jsonInput = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonInput.append(line);
            }
        }
        JSONObject jsonObject = new JSONObject(jsonInput.toString());
        System.out.println(jsonObject);
        // Ваша бизнес-логика регистрации
        ArrayList<AddUserError> errors = userService.addUser(JSONParser.jsonToUser(jsonObject));
        System.out.println(JSONParser.parseAddUserErrors(errors));

        resp.getWriter().write(JSONParser.parseAddUserErrors(errors).toString());
    }
}
