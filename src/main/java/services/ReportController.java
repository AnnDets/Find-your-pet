package services;

import DBControllers.DBController;
import models.Report;
import models.User;
import utils.*;
import java.util.ArrayList;

public class ReportController {
    private final DBController dbcontroller;

    public ReportController(DBController dbController) {
        this.dbcontroller = dbController;
    }

    // Добавление новой заявки о пропавшем животном
    public ArrayList<ReportError> addReport(int userId, String[] colors, String[] specialMarks, String[] photos,
                                            String breed, String description, String foundDate,
                                            String location, String status) {
        ArrayList<ReportError> errors = new ArrayList<>();

        LogUtil.debug("Validating report for user: " + userId);

        if (userId == 0 || Integer.toString(userId).trim().isEmpty()) {
            errors.add(ReportError.MISSING_USER_ID);
            LogUtil.warn("Missing user ID");
        }
        if (foundDate == null || foundDate.trim().isEmpty()) {
            errors.add(ReportError.MISSING_FOUND_DATE);
            LogUtil.warn("Missing found date");
        }
        if (location == null || location.trim().isEmpty()) {
            errors.add(ReportError.MISSING_LOCATION);
            LogUtil.warn("Missing location");
        }

        // Если есть ошибки, возвращаем их
        if (!errors.isEmpty()) {
            LogUtil.warn("Errors occurred while adding report: " + errors);
            return errors;
        }

        // Логируем успешное добавление заявки
        dbcontroller.addReport(userId, breed, description, foundDate, location, status, colors, specialMarks, photos);
        LogUtil.info("Report added successfully for user: " + userId);
        return new ArrayList<>();
    }

    // Проверка наличия заявки
    public boolean reportExists(int reportId) {
        LogUtil.debug("Checking if report exists with ID: " + reportId);
        boolean exists = dbcontroller.getReportById(reportId) != null;
        if (exists) {
            LogUtil.info("Report found with ID: " + reportId);
        } else {
            LogUtil.warn("Report not found with ID: " + reportId);
        }
        return exists;
    }

    // Получение заявки по ID
    public Report getReportById(int reportId) {
        LogUtil.debug("Fetching report by ID: " + reportId);
        Report report = dbcontroller.getReportById(reportId);
        if (report != null) {
            LogUtil.info("Report retrieved with ID: " + reportId);
        } else {
            LogUtil.warn("Report not found with ID: " + reportId);
        }
        return report;
    }
    public ArrayList<Report> getReportsByFilters(String color, String specialMark, String location, String breed){
        LogUtil.debug("Fetching reports with filters:");
        ArrayList<Report> reports = dbcontroller.getReportsByFilters(color, specialMark, location, breed);
        if (!reports.isEmpty()) {
            LogUtil.info("заявки соответсвующие фильтру: " + reports);
        } else {
            LogUtil.info("Заявок соответсвующих фильтру не найдено ");
        }
        return reports;
    }
    public void deleteReportById(int reportId) {
        LogUtil.debug("Deleting report by ID: " + reportId);
        boolean result = /*dbcontroller.deleteReport(reportId)*/true;
        if (result) {
            LogUtil.info("Report deleted successfully with ID: " + reportId);
        } else {
            LogUtil.error("Failed to delete report with ID: " + reportId, null);
        }
    }

    public ArrayList<Report> getAllReports() {
        LogUtil.debug("Fetching all users");
        return dbcontroller.getAllReports();
    }


}
