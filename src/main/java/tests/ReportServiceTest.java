package tests;

import dao.ReportDao;
import dao.ReportDaoFactory;
import models.Report;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import services.ReportService;
import utils.LogUtil;
import utils.ReportError;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

/*testAddReport_validReport: Проверяет успешное добавление корректного отчета.
testAddReport_missingUser: Проверяет, что при отсутствии пользователя возвращается ошибка.
testAddReport_missingFoundDate: Проверяет, что при отсутствии даты возвращается ошибка.
testAddReport_missingLocation: Проверяет, что при отсутствии местоположения возвращается ошибка.
testReportExists_reportExists: Проверяет, что существующий отчет правильно определяется.
testReportExists_reportDoesNotExist: Проверяет, что несуществующий отчет корректно определяется.
testGetReportById_validId: Проверяет получение отчета по валидному ID.
testGetReportById_invalidId: Проверяет, что при некорректном ID возвращается null.
testGetReportsByFilters: Проверяет фильтрацию отчетов по заданным параметрам.
testDeleteReportById_success: Проверяет успешное удаление отчета.
testGetAllReports: Проверяет получение всех отчетов.*/


public class ReportServiceTest {

    private ReportService reportService;
    private ReportDao reportDaoMock;
    private Connection connectionMock;

    @Test
    void testAddReport_validReport() {
        User user1 = new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");

        Report validReport = new Report(1, user1 ,new ArrayList<>(Arrays.asList("Black", "White")),
                new ArrayList<>(Arrays.asList("Scar on right ear")),
                new ArrayList<>(Arrays.asList("photo1.jpg", "photo2.jpg")),
                "Golden", "Friendly, loves to play fetch.",
                "2023-10-27","minsk","Lost");

        ArrayList<ReportError> errors = reportService.addReport(validReport);

        assertTrue(errors.isEmpty());
        verify(reportDaoMock, times(1)).create(validReport);
    }

    @Test
    void testAddReport_missingUser() {
        Report reportWithoutUser = new Report(
                1,
                null,
                new ArrayList<>(Arrays.asList("Black")),
                new ArrayList<>(Arrays.asList("Scar on right ear")),
                new ArrayList<>(Arrays.asList("photo1.jpg", "photo2.jpg")),
                "Golden",
                "Friendly, loves to play fetch.",
                "12.12.2024",
                "Minsk",
                "lost"
        );

        ArrayList<ReportError> errors = reportService.addReport(reportWithoutUser);

        assertFalse(errors.isEmpty());
        assertTrue(errors.contains(ReportError.MISSING_USER_ID));
        verify(reportDaoMock, never()).create(any(Report.class));
    }

    @Test
    void testAddReport_missingFoundDate() {
        User user1 = new User(1, "John Doe", "john.doe@example.com", "1234567890", "password123", "123 Main St");
        Report reportWithoutDate = new Report(
                1,
                user1,
                new ArrayList<>(Arrays.asList("Black")),
                new ArrayList<>(Arrays.asList("Scar on right ear")),
                new ArrayList<>(Arrays.asList("photo1.jpg", "photo2.jpg")),
                "Golden",
                "Friendly, loves to play fetch.",
                null,
                "Minsk",
                "lost"
        );

        ArrayList<ReportError> errors = reportService.addReport(reportWithoutDate);

        assertFalse(errors.isEmpty());
        assertTrue(errors.contains(ReportError.MISSING_FOUND_DATE));
        verify(reportDaoMock, never()).create(any(Report.class));
    }

    @Test
    void testAddReport_missingLocation() {
        User user1 = new User(1, "John Doe", "john.doe@example.com", "1234567890", "password123", "123 Main St");
        Report reportWithoutLocation = new Report(
                1,
                user1,
                new ArrayList<>(Arrays.asList("Black")),
                new ArrayList<>(Arrays.asList("Scar on right ear")),
                new ArrayList<>(Arrays.asList("photo1.jpg", "photo2.jpg")),
                "Golden",
                "Friendly, loves to play fetch.",
                "12.12.2024",
                null,
                "lost"
        );

        ArrayList<ReportError> errors = reportService.addReport(reportWithoutLocation);

        assertFalse(errors.isEmpty());
        assertTrue(errors.contains(ReportError.MISSING_LOCATION));
        verify(reportDaoMock, never()).create(any(Report.class));
    }

    @Test
    void testReportExists_reportExists() {
        int reportId = 1;
        User user1 = new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");

        Report report = new Report(1, user1 ,new ArrayList<>(Arrays.asList("Black", "White")),
                new ArrayList<>(Arrays.asList("Scar on right ear")),
                new ArrayList<>(Arrays.asList("photo1.jpg", "photo2.jpg")),
                "Golden", "Friendly, loves to play fetch.",
                "2023-10-27","minsk","Lost");
        when(reportDaoMock.getById(reportId)).thenReturn(report);

        boolean exists = reportService.reportExists(reportId);

        assertTrue(exists);
    }

    @Test
    void testReportExists_reportDoesNotExist() {
        int reportId = 1;
        when(reportDaoMock.getById(reportId)).thenReturn(null);

        boolean exists = reportService.reportExists(reportId);

        assertFalse(exists);
    }

    @Test
    void testGetReportById_validId() {
        int reportId = 1;
                User user1 = new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");

        Report mockReport = new Report(1, user1 ,new ArrayList<>(Arrays.asList("Black", "White")),
                new ArrayList<>(Arrays.asList("Scar on right ear")),
                new ArrayList<>(Arrays.asList("photo1.jpg", "photo2.jpg")),
                "Golden", "Friendly, loves to play fetch.",
                "2023-10-27","minsk","Lost");
        when(reportDaoMock.getById(reportId)).thenReturn(mockReport);

        Report fetchedReport = reportService.getReportById(reportId);

        assertNotNull(fetchedReport);
    }

    @Test
    void testGetReportById_invalidId() {
        int reportId = 1;
        when(reportDaoMock.getById(reportId)).thenReturn(null);

        Report fetchedReport = reportService.getReportById(reportId);

        assertNull(fetchedReport);
    }

    @Test
    void testGetReportsByFilters() {
        ArrayList<Report> mockReports = new ArrayList<>();
        User user1 = new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");

        Report mockReport = new Report(1, user1 ,new ArrayList<>(Arrays.asList("Black", "White")),
                new ArrayList<>(Arrays.asList("Scar on right ear")),
                new ArrayList<>(Arrays.asList("photo1.jpg", "photo2.jpg")),
                "Golden", "Friendly, loves to play fetch.",
                "2023-10-27","minsk","Lost");
        mockReports.add(mockReport);

        when(reportDaoMock.getReportsByFilters(anyString(), anyString(), anyString(), anyString())).thenReturn(mockReports);

        ArrayList<Report> reports = reportService.getReportsByFilters("Black", "Scar", "Minsk", "Golden");

        assertNotNull(reports);
        assertEquals(1, reports.size());
    }

    @Test
    void testDeleteReportById_success() {

        User user1 = new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");

        Report reportToDelete = new Report(1, user1 ,new ArrayList<>(Arrays.asList("Black", "White")),
                new ArrayList<>(Arrays.asList("Scar on right ear")),
                new ArrayList<>(Arrays.asList("photo1.jpg", "photo2.jpg")),
                "Golden", "Friendly, loves to play fetch.",
                "2023-10-27","minsk","Lost");
        reportToDelete.setId(1);

        when(reportDaoMock.delete(reportToDelete)).thenReturn(true);

        reportService.deleteReportById(reportToDelete);

        verify(reportDaoMock, times(1)).delete(reportToDelete);
    }

    @Test
    void testGetAllReports() {
        ArrayList<Object> mockReports = new ArrayList<>();
        User user1 = new User(1,"Test User","invalid-email",
                "1234567890", "Pewrvrvg#d135","Test Address");

        Report report = new Report(1, user1 ,new ArrayList<>(Arrays.asList("Black", "White")),
                new ArrayList<>(Arrays.asList("Scar on right ear")),
                new ArrayList<>(Arrays.asList("photo1.jpg", "photo2.jpg")),
                "Golden", "Friendly, loves to play fetch.",
                "2023-10-27","minsk","Lost");
        mockReports.add(report);

        when(reportDaoMock.readAll()).thenReturn(mockReports);

        ArrayList<Object> reports = reportService.getAllReports();

        assertNotNull(reports);
        assertEquals(1, reports.size());
    }
}
