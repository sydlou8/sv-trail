package sm.dev.sv_trail.common.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import sm.dev.sv_trail.model.enums.WeatherCondition;

/**
 * Maps the Open-Meteo /forecast response.
 * Example request: /forecast?latitude=37.33&longitude=-121.89&current=temperature_2m,weather_code
 */
public record WeatherData(
    @JsonProperty("current") Current current
) {
    public record Current(
        @JsonProperty("temperature_2m") double temperature2m,
        @JsonProperty("weather_code")  int weatherCode
    ) {}

    /** Convenience method — resolves WMO weather code to a game condition. */
    public WeatherCondition condition() {
        return WeatherCondition.fromCode(current.weatherCode());
    }
}
