package tests;

import DBControllers.DBController;
import models.Report;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import services.ReportController;
import utils.ReportError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportControllerTest {

    private ReportController reportController;
    private DBController dbControllerMock;

    @BeforeEach
    void setUp() {
        dbControllerMock = Mockito.mock(DBController.class);
        reportController = new ReportController(dbControllerMock);
    }

//Валидный ввод - проверяет добавление заявки в базу данных и возвращает пустой список ошибок
    @Test
    void testAddReportValidInput() {
        // Arrange
        int userId = 1;
        String[] colors = {"Black", "White"};
        String[] specialMarks = {"Scar on right ear"};
        String[] photos = {"photo1.jpg", "photo2.jpg"};
        String breed = "Golden Retriever";
        String description = "Friendly, loves to play fetch.";
        String foundDate = "2023-10-27";
        String location = "Central Park";
        String status = "Lost";

        // Act
        ArrayList<ReportError> errors = reportController.addReport(userId, colors, specialMarks, photos,
                breed, description, foundDate, location, status);

        // Assert
        assertTrue(errors.isEmpty());
        verify(dbControllerMock, times(1)).addReport(userId, breed, description, foundDate, location, status, colors, specialMarks, photos);
    }

//Отсутствует userId - проверяет, что метод возвращает ошибку MISSING_USER_ID
    @Test
    void TestAddReportMissingUserId() {
        // Arrange
        int userId = 0;
        String[] colors = {"Black", "White"};
        String[] specialMarks = {"Scar on right ear"};
        String[] photos = {"photo1.jpg", "photo2.jpg"};
        String breed = "Golden Retriever";
        String description = "Friendly, loves to play fetch.";
        String foundDate = "2023-10-27";
        String location = "Central Park";
        String status = "Lost";

        // Act
        ArrayList<ReportError> errors = reportController.addReport(userId, colors, specialMarks, photos,
                breed, description, foundDate, location, status);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(ReportError.MISSING_USER_ID));
    }
//Отсутствует дата - проверяет, что метод возвращает ошибку MISSING_FOUND_DATE
    @Test
    void testAddReportMissingDate() {
        // Arrange
        int userId = 1;
        String[] colors = {"Black", "White"};
        String[] specialMarks = {"Scar on right ear"};
        String[] photos = {"photo1.jpg", "photo2.jpg"};
        String breed = "Golden Retriever";
        String description = "Friendly, loves to play fetch.";
        String foundDate = "";
        String location = "Central Park";
        String status = "Lost";

        // Act
        ArrayList<ReportError> errors = reportController.addReport(userId, colors, specialMarks, photos,
                breed, description, foundDate, location, status);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(ReportError.MISSING_FOUND_DATE));
    }

    //Отсутствует местоположение - проверяет, что метод возвращает ошибку MISSING_LOCATION
    @Test
    void testAddReportMissingLocation() {
        // Arrange
        int userId = 1;
        String[] colors = {"Black", "White"};
        String[] specialMarks = {"Scar on right ear"};
        String[] photos = {"photo1.jpg", "photo2.jpg"};
        String breed = "Golden Retriever";
        String description = "Friendly, loves to play fetch.";
        String foundDate = "2023-10-27";
        String location = "";
        String status = "Lost";

        // Act
        ArrayList<ReportError> errors = reportController.addReport(userId, colors, specialMarks, photos,
                breed, description, foundDate, location, status);

        // Assert
        assertEquals(1, errors.size());
        assertTrue(errors.contains(ReportError.MISSING_LOCATION));
    }


//Заявка существует
    @Test
    void testReportExists() {
        // Arrange
        int reportId = 1;
        Report report = new Report(reportId, 1, "Golden Retriever", "Friendly", "2023-10-27", "Central Park", "Lost", new String[]{"Black", "White"}, new String[]{"Scar on right ear"}, new String[]{"photo1.jpg", "photo2.jpg"});
        when(dbControllerMock.getReportById(reportId)).thenReturn(report);

        // Act
        boolean exists = reportController.reportExists(reportId);

        // Assert
        assertTrue(exists);
    }

    //Заявка не существует
    @Test
    void testReportDoesNotExist() {
        // Arrange
        int reportId = 1;
        when(dbControllerMock.getReportById(reportId)).thenReturn(null);

        // Act
        boolean exists = reportController.reportExists(reportId);

        // Assert
        assertFalse(exists);
    }

// Заявка существует - проверяет, что метод возвращает корректную заявку
    @Test
    void testGetReportByIdReportExists() {
        User user = new User(3,"Test User","test23@example.com",
                "invalid-phone", "Pewrvrvg#d135","Test Address")
        // Arrange
        int reportId = 1;
        Report report = new Report(reportId, user, new String[]{"Black","White"}, new String[]{"Scar on right ear"},new String[]{"photo1.jpg", "photo2.jpg"},"Golden Retriever", "Friendly", "2023-10-27", "Central Park", "Lost");
        when(dbControllerMock.getReportById(reportId)).thenReturn(report);

        // Act
        Report retrievedReport = reportController.getReportById(reportId);

        // Assert
        assertEquals(report, retrievedReport);
    }

    //Заявка не существует - проверяет, что метод возвращает null
    @Test
    void TestGetReportByIdReportDoesNotExist() {
        // Arrange
        int reportId = 1;
        when(dbControllerMock.getReportById(reportId)).thenReturn(null);

        // Act
        Report retrievedReport = reportController.getReportById(reportId);

        // Assert
        assertNull(retrievedReport);
    }
//Валидные фильтры - проверяет, что метод возвращает список заявок, соответствующих фильтрам
    @Test
    void testGetReportsByValidFilters() {
        // Arrange
        String color = "Black";
        String specialMark = "Scar";
        String location = "Central Park";
        String breed = "Golden Retriever";

        Report report1 = new Report(1, 1, "Golden Retriever", "Friendly", "2023-10-27", "Central Park", "Lost", new String[]{"Black", "White"}, new String[]{"Scar on right ear"}, new String[]{"photo1.jpg", "photo2.jpg"});

    Report report2 = new Report(2, 2, "Labrador", "Energetic", "2023-10-26", "Central Park", "Found", new String[]{"Black"}, new String[]{"Scar on left leg"}, new String[]{"photo3.jpg"});
    List<Report> expectedReports = Arrays.asList(report1, report2);
    when(dbControllerMock.getReportsByFilters(color, specialMark, location, breed)).thenReturn(new ArrayList<>(expectedReports));

    // Act
    List<Report> actualReports = reportController.getReportsByFilters(color, specialMark, location, breed);

    // Assert
    assertEquals(expectedReports, actualReports);}

//Нет совпадений по фильтрам - проверяет, что метод возвращает пустой список
@Test
void testGetReportsByFiltersNoMatchingReports() {
    // Arrange
    String color = "Brown";
    String specialMark = "Tail";
    String location = "Times Square";
    String breed = "Poodle";

    when(dbControllerMock.getReportsByFilters(color, specialMark, location, breed)).thenReturn(new ArrayList<>());

    // Act
    List<Report> actualReports = reportController.getReportsByFilters(color, specialMark, location, breed);

    // Assert
    assertTrue(actualReports.isEmpty());
}

//Заявка существует - проверяет, что метод удаляет заявку
@Test
void testDeleteReportById() {
    // Arrange
    int reportId = 1;
    when(reportController.deleteReport(reportId)).thenReturn(false);

    // Act
    reportController.deleteReportById(reportId);

    // Assert
    verify(dbControllerMock, times(1)).deleteReport(reportId);
}

@Test
void testReturnAllReports() {
    // Arrange
    List<Report> expectedReports = new ArrayList<>();
    when(dbControllerMock.getAllReports()).thenReturn(expectedReports);

    // Act
    List<Report> actualReports = reportController.getAllReports();

    // Assert
    assertEquals(expectedReports, actualReports);
}
}