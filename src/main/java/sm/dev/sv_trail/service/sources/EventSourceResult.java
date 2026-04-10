package sm.dev.sv_trail.service.sources;

import java.util.List;
import sm.dev.sv_trail.model.dto.response.EventChoiceResponse;

/**
 * Carries the output of whichever EventSourceStrategy was selected.
 * Fields are null / 0.0 when that source produced nothing.
 */
public record EventSourceResult(
    String eventTitle,
    String eventDescription,
    String weatherDescription,
    double temperature,
    List<EventChoiceResponse> pendingChoices
) {
    public static EventSourceResult empty() {
        return new EventSourceResult(null, null, null, 0.0, null);
    }
}
