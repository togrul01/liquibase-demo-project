package org.company.springliquibase.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cardManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Card Management System API")
                        .description("API documentation for Card Management System")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Your Company")
                                .email("contact@yourcompany.com")
                                .url("https://yourcompany.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ));
    }

    @Bean
    public OpenApiCustomizer globalHeader() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                operation.addParametersItem(new Parameter()
                        .in("header")
                        .name("Accept-Language")
                        .description("Language for response messages (en, az, ru)")
                        .required(false)
                        .schema(new StringSchema()
                                .addEnumItem("en")
                                .addEnumItem("az")
                                .addEnumItem("ru")));
            }));
        };
    }
}
