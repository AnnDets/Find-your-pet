
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");

        // Пример ответа
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", "User registered successfully");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(responseJson.toString());
    }
}
