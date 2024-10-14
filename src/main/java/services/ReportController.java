package services;

import DBControllers.DBController;
import models.Report;
import utils.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportController {
    private final DBController dbcontroller;
    private final Logger logger_;
    public ReportController(DBController dbController, Logger logger) {
        this.dbcontroller = dbController;
        logger_ = logger;
    }

    // Добавление новой заявки о пропавшем животном
    public ArrayList<ReportError> addReport(int userId, String[] colors, String[] specialMarks,
                                            String breed, String description, String foundDate,
                                            String location, String status){
        ArrayList<ReportError> errors = new ArrayList<>();

        if (userId == 0 || Integer.toString(userId).trim().isEmpty()) {
            errors.add(ReportError.MISSING_USER_ID);
        }
        if (foundDate == null || foundDate.trim().isEmpty()) {
            errors.add(ReportError.MISSING_FOUND_DATE);
        }
        if (location == null || location.trim().isEmpty()) {
            errors.add(ReportError.MISSING_LOCATION);
        }

        // Если есть ошибки, возвращаем их
        if (!errors.isEmpty()) {
            logger_.log(Level.WARNING, "Ошибка при добавлении заявки о пропавшем животном: " + errors);
            return errors;
        }
        dbcontroller.addReport(userId, breed, description, foundDate, location, status, colors, specialMarks);
        return new ArrayList<>();
    }

    // Проверка наличия заявки
    public boolean reportExists(int reportId) {
        return dbcontroller.getReportById(reportId) != null;
    }

    // Получение заявки по ID
    public Report getReportById(int reportId) {
        return dbcontroller.getReportById(reportId);
    }

    public void  deleteReportById(int reportId){
        //
    }
}
