package services;

//import DBControllers.DBController;
import dao.ReportDao;
import dao.ReportDaoFactory;
import dao.ReportDaoPostgres;
import models.Report;
import utils.*;

import java.sql.Connection;
import java.util.ArrayList;

public class ReportService {
    private final ReportDao reportDao;
    public ReportService(Connection connection) {
        ReportDaoFactory reportDaoFactory = ReportDaoFactory.getInstance();
        reportDao = reportDaoFactory.createUserDao("postgres", connection);
    }

    // Добавление новой заявки о пропавшем животном
    public ArrayList<ReportError> addReport(Report report) {
        ArrayList<ReportError> errors = new ArrayList<>();

        LogUtil.debug("Validating report for user: " + report.getUser());

        if (report.getUser() == null) {
            errors.add(ReportError.MISSING_USER_ID);
            LogUtil.warn("Missing user ID");
        }
        if (report.getFoundDate() == null || report.getFoundDate().trim().isEmpty()) {
            errors.add(ReportError.MISSING_FOUND_DATE);
            LogUtil.warn("Missing found date");
        }
        if (report.getLocation() == null || report.getLocation().trim().isEmpty()) {
            errors.add(ReportError.MISSING_LOCATION);
            LogUtil.warn("Missing location");
        }

        if (!errors.isEmpty()) {
            LogUtil.warn("Errors occurred while adding report: " + errors);
            return errors;
        }

        // Логируем успешное добавление заявки
        reportDao.create(report);
        LogUtil.info("Report added successfully for user: " + report.getUser());
        return new ArrayList<>();
    }


    // Проверка наличия заявки
    public boolean reportExists(int reportId) {
        LogUtil.debug("Checking if report exists with ID: " + reportId);
        boolean exists = reportDao.getById(reportId) != null;
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
        Report report = reportDao.getById(reportId);
        if (report != null) {
            LogUtil.info("Report retrieved with ID: " + reportId);
        } else {
            LogUtil.warn("Report not found with ID: " + reportId);
        }
        return report;
    }
    public ArrayList<Report> getReportsByFilters(String color, String specialMark, String location, String breed){
        LogUtil.debug("Fetching reports with filters:");
        ArrayList<Report> reports = reportDao.getReportsByFilters(color, specialMark, location, breed);
        if (!reports.isEmpty()) {
            LogUtil.info("заявки соответсвующие фильтру: " + reports);
        } else {
            LogUtil.info("Заявок соответсвующих фильтру не найдено ");
        }
        return reports;
    }
    public void deleteReportById(Report report) {
        LogUtil.debug("Deleting report: " + report);
        boolean result = reportDao.delete(report);
        if (result) {
            LogUtil.info("Report deleted successfully with ID: " + report);
        } else {
            LogUtil.error("Failed to delete report with ID: " + report, null);
        }
    }

    public ArrayList<Object> getAllReports() {
        LogUtil.debug("Fetching all users");
        return reportDao.readAll();
    }


}
