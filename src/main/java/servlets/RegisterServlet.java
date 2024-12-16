package servlets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import models.User;
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
    @Operation(
            summary = "Register user",
            description = "Возвращает список ошибок при регистрации",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный ответ",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера"
                    )
            }
    )
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
