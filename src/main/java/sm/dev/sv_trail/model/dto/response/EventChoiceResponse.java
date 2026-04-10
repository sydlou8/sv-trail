package sm.dev.sv_trail.model.dto.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record EventChoiceResponse(
    UUID id,
    String description) {
} 
