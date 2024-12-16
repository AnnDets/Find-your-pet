package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import models.Report;
import org.json.JSONArray;
import org.json.JSONObject;
import services.ReportService;
import utils.JSONParser;

import java.io.IOException;
import java.util.List;

@WebServlet("/getReport")
public class GetReportServlet extends HttpServlet {
    private ReportService reportService;

    @Override
    public void init() throws ServletException {
        // Получение ReportService из ServletContext
        reportService = (ReportService) getServletContext().getAttribute("reportService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Получаем список всех отчетов из базы
            List<Object> reports = reportService.getAllReports();
            System.out.println(reports);
            // Преобразуем список отчетов в JSON
            JSONArray reportsJson = new JSONArray();
            for (Object report : reports) {
                System.out.println(JSONParser.reportToJSON((Report) report));
                reportsJson.put(JSONParser.reportToJSON((Report) report)); // Предполагается метод для конвертации Report в JSONObject
            }

            // Отправляем JSON-ответ
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(reportsJson.toString());
        } catch (Exception e) {
            // Логирование ошибки
            e.printStackTrace();

            // Возвращаем сообщение об ошибке
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Ошибка при получении данных отчетов");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(errorResponse.toString());
        }
    }
}
