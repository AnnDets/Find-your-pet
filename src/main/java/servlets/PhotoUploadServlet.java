package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/uploadPhoto")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class PhotoUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Получение пути для сохранения
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

        // Создаем директорию, если ее нет
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uploadedFilePath = null;

        try {
            for (Part part : request.getParts()) {
                String fileName = getFileName(part);
                if (fileName != null && !fileName.isEmpty()) {
                    String filePath = uploadPath + File.separator + fileName;
                    uploadedFilePath = UPLOAD_DIR + "/" + fileName; // Относительный путь
                    try (FileOutputStream fos = new FileOutputStream(filePath);
                         InputStream is = part.getInputStream()) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }

            if (uploadedFilePath != null) {
                // Возвращаем JSON с путем до загруженного фото
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Фото успешно загружено!\", \"url\": \"" + uploadedFilePath + "\"}");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Файл не был загружен.");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при загрузке файла.");
            e.printStackTrace();
        }
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return null;
    }
}
