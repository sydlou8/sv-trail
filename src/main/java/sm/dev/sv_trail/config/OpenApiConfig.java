package sm.dev.sv_trail.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SV Trail API")
                .version("1.0")
                .description(
                    "Backend for SV Trail — a text-based RPG where developers travel between tech cities, " +
                    "take on freelance gigs, enter hackathons, and survive the startup grind.\n\n" +
                    "**How to use:**\n" +
                    "1. Register or login via the **Authentication** endpoints.\n" +
                    "2. Click the **Authorize** button above and paste your token (without `Bearer `).\n" +
                    "3. Create a game session via **POST /api/game**.\n" +
                    "4. Execute turns via **POST /api/game/{gameId}/turn**.\n" +
                    "5. If a turn returns a `pendingEventId`, resolve it via **POST /api/game/{gameId}/event-choice** before continuing."
                ))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Paste your JWT token here (without the 'Bearer ' prefix).")
                ));
    }
}
