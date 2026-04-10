package sm.dev.sv_trail.model.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PendingEventResponse(
    UUID eventId,
    String title,
    String description,
    List<EventChoiceResponse> choices
) {}
