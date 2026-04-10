package sm.dev.sv_trail.common.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherClient {
    private final RestClient forcastRestClient;

    /**
     * Fetches current weather for a location from Open-Meteo.
     * Returns null if the request fails so callers can treat it as a no-op.
     */
    public WeatherData getWeather(double latitude, double longitude) {
        try {
            String url = String.format(
                "/forecast?latitude=%.4f&longitude=%.4f&current=temperature_2m,weather_code&temperature_unit=fahrenheit",
                latitude, longitude);
            return forcastRestClient.get()
                .uri(url)
                .retrieve()
                .body(WeatherData.class);
        } catch (Exception e) {
            log.warn("Weather API call failed: {}", e.getMessage());
            return null;
        }
    }
}
