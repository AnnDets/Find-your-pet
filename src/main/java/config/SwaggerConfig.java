package config;

import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.Set;

@WebListener
public class SwaggerConfig implements ServletContextListener {

    public void init() throws OpenApiConfigurationException {
        OpenApiContext context = new GenericOpenApiContextBuilder()
                .resourcePackages(Set.of("servlets")) // Укажите ваши пакеты
                .buildContext(true);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        return;
    }
}
