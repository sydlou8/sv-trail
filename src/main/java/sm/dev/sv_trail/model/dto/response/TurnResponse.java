package sm.dev.sv_trail.model.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import sm.dev.sv_trail.model.enums.ActionType;

@Builder
public record TurnResponse(
    ActionType actionType,
    UUID gameSessionId,
    UUID userId,

    // Action description
    String description,

    // Stat Changes
    int cashDelta,
    int moraleDelta,
    int hypeDelta,
    int bugCountDelta,
    int coffeeSupplyDelta,

    // Calculated stats after applying changes from this turn
    int cash,
    int morale,
    int hype,
    int bugCount,
    int coffeeSupply,

    int finalScore,

    // Game State
    int dayNumber,
    String currentCity,
    String currentState,
    String state,
    String lossReason,

    // Event Details
    UUID pendingEventId, // Null if no event is pending, otherwise contains the ID of the event that the frontend should fetch details for
    String eventTitle,
    String eventDescription,
    List<EventChoiceResponse> eventChoices,

    // External API modifiers (set when event source is WEATHER)
    String weatherDescription,
    double temperature
) {}