package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.models.OpenAPI;

import java.io.IOException;
import java.util.Set;

@WebServlet("/openapi.json")
public class OpenApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            // Настройка Swagger Configuration
            SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration()
                    .scannerClass("io.swagger.v3.jaxrs2.integration.JaxrsAnnotationScanner")
                    .prettyPrint(true)
                    .readAllResources(true)
                    .resourcePackages(Set.of("servlets")); // Укажите ваш пакет с REST-ресурсами

            // Использование JaxrsOpenApiContextBuilder для создания контекста
            OpenApiContext context = new JaxrsOpenApiContextBuilder()
                    .openApiConfiguration(swaggerConfiguration)
                    .buildContext(true);

            OpenAPI openAPI = context.read();
            System.out.println("OpenAPI Info: " + context.read().getInfo());

            // Генерация JSON спецификации OpenAPI
            resp.getWriter().write(Json.pretty(openAPI));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка генерации OpenAPI спецификации");
        }
    }
}
