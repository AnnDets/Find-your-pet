package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Path;
import models.Report;
import org.json.JSONObject;
import services.ReportService;
import services.UserService;
import utils.JSONParser;
import utils.ReportError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;


@WebServlet("/uploadPetData")
@Path("/customers")
public class SavePetServlet extends HttpServlet {
    private ReportService reportService;
    @Override
    public void init() throws ServletException {
        // Получение UserService из ServletContext
        reportService = (ReportService) getServletContext().getAttribute("reportService");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jsonString = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        JSONObject json = new JSONObject(jsonString);
        Report report = utils.JSONParser.jsonToReport(json);
        ArrayList<ReportError> errors = reportService.addReport(report);
        // Логика сохранения отчета в базе данных

        resp.setContentType("application/json");
        System.out.println(JSONParser.parseReportErrors(errors));

        resp.getWriter().write(JSONParser.parseReportErrors(errors).toString());
    }
}
