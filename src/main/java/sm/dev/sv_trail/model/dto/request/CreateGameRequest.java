package sm.dev.sv_trail.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import sm.dev.sv_trail.model.enums.PlayerRole;

public record CreateGameRequest(
    @NotNull(message = "Player role is required")
    @Schema(
        description = "Starting role that determines your initial stats:\n" +
            "BEGINNER_DEV: $1000 cash, 80 morale, 10 hype, 30 coffee.\n" +
            "INTERMEDIATE_DEV: $3000 cash, 50 morale, 20 hype, 20 coffee.\n" +
            "ADVANCED_DEV: $5000 cash, 30 morale, 30 hype, 10 coffee.",
        allowableValues = {"BEGINNER_DEV", "INTERMEDIATE_DEV", "ADVANCED_DEV"},
        example = "BEGINNER_DEV"
    )
    PlayerRole role
) {}