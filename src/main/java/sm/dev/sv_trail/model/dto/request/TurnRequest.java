package sm.dev.sv_trail.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import sm.dev.sv_trail.model.enums.ActionType;

public record TurnRequest(
    @NotNull
    @Schema(
        description = "The action to perform this turn",
        allowableValues = {"TRAVEL", "REST", "SUPPLY", "FREELANCE", "HACKATHON", "DEBUG"},
        example = "HACKATHON"
    )
    ActionType actionType
) {}