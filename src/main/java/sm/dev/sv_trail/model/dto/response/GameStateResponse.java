package sm.dev.sv_trail.model.dto.response;

import java.util.UUID;
import lombok.Builder;

@Builder // Using @Builder to easily construct this response in the service layer
public record GameStateResponse(
    UUID id,
    String state,       // ACTIVE, WON, or LOST
    String role,        // BEGINNER_DEV, INTERMEDIATE_DEV, or EXPERT_DEV
    String lossReason,  // Null if game is active or won, otherwise contains reason for loss
    // User Stats
    int cash,
    int morale,
    int hype,
    int bugCount,
    int coffeeSupply,
    // Game Progress
    int dayNumber,
    String currentCity,
    String currentState,
    String currentTechCompany) {
} 