package devsinc.Instagram.clone.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Baqer Ali Mughal",
                        email = "baqer.ali@devsinc.com"
                ),
                description = "Instagram clone backend with full functionalities. " +
                        "This is the post project assignment for me in training of spring boot.",
                title = "Instagram clone by Baqer",
                version = "0.0.1-SNAPSHOT"
        ),
        servers = @Server(
                description = "This server is for my local environment!",
                url = "http://localhost:8080"
        ),
        security = {
                @SecurityRequirement(
                        name = "Bearer Authentication"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        description = "Jwt authentication by using authentication header with bearer token!",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class SwaggerApiConfig {

}
