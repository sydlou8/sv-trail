package sm.dev.sv_trail.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeatherCondition {
    CLEAR       ("Clear skies — the team is energized!",         0,  10,   5, 0,  0),
    CLOUDY      ("Overcast but manageable.",                     0,   0,   0, 0,  0),
    FOG         ("Dense fog is slowing everything down.",        0,  -5,   0, 0,  0),
    RAIN        ("Rainy day dampens the team's spirits.",        0, -10,  -5, 0, -1),
    SNOW        ("Snow makes everything harder.",                0, -15,  -5, 0, -2),
    THUNDERSTORM("Thunderstorm! Everyone hunkers down.",         0, -20, -10, 0, -3);

    private final String description;
    private final int cashDelta;
    private final int moraleDelta;
    private final int hypeDelta;
    private final int bugCountDelta;
    private final int coffeeSupplyDelta;

    /** Maps WMO weather interpretation codes to a WeatherCondition. */
    public static WeatherCondition fromCode(int code) {
        if (code == 0)                          return CLEAR;
        if (code <= 3)                          return CLOUDY;
        if (code == 45 || code == 48)           return FOG;
        if (code >= 51 && code <= 82)           return RAIN;
        if (code >= 71 && code <= 77)           return SNOW;
        if (code == 95 || code == 96 || code == 99) return THUNDERSTORM;
        return CLOUDY; // safe fallback for any other code
    }
}
