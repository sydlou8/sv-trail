package sm.dev.sv_trail.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Available turn actions")
public enum ActionType {
    @Schema(description = "Move to the next city (-$300). Triggers a random event.")
    TRAVEL,
    @Schema(description = "Take a break to recover morale (-5 hype).")
    REST,
    @Schema(description = "Buy 3 coffee supplies (-$50).")
    SUPPLY,
    @Schema(description = "Earn cash with low-risk freelance work. Amount depends on your role and morale.")
    FREELANCE,
    @Schema(description = "Enter a hackathon. 15% chance: +$1000, +20 morale, +10 hype. Otherwise: -5 morale, -5 hype. Costs 1 coffee.")
    HACKATHON,
    @Schema(description = "Fix 5 bugs. Costs 1 coffee. You must have at least 1 bug.")
    DEBUG
}
