package sm.dev.sv_trail.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "app.clients.weather")
@Getter
@Setter
public class WeatherConfig {
    private String apiUrl;

    @Bean
    public RestClient forcastRestClient() {
        return RestClient.builder().baseUrl(apiUrl).build();
    }
}
