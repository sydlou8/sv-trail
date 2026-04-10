package sm.dev.sv_trail.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.model.dto.request.EventChoiceRequest;
import sm.dev.sv_trail.model.dto.request.TurnRequest;
import sm.dev.sv_trail.model.dto.response.EventChoiceResponse;
import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.User;
import sm.dev.sv_trail.service.TurnService;

@Tag(name = "Turns", description = "Execute game turns and resolve event choices. Requires a valid JWT token.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class TurnController {
    private final TurnService turnService;

    @Operation(summary = "Execute a turn action",
        description = "Perform one of the following actions each turn:\n\n" +
            "| Action | Effect | Cost |\n" +
            "|---|---|---|\n" +
            "| `TRAVEL` | Move to the next city. Triggers a random event. | -$300 |\n" +
            "| `REST` | Recover morale. | -5 hype |\n" +
            "| `SUPPLY` | Buy 3 coffees. | -$50 |\n" +
            "| `FREELANCE` | Earn cash based on your role and morale. | — |\n" +
            "| `HACKATHON` | 15% chance: +$1000, +20 morale, +10 hype. Otherwise: -5 morale, -5 hype. | -1 coffee |\n" +
            "| `DEBUG` | Fix 5 bugs. | -1 coffee |\n\n" +
            "If `pendingEventId` is set in the response, you must resolve it via `/event-choice` before taking another turn.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Turn executed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid action or game state (e.g. no coffee for HACKATHON/DEBUG)"),
        @ApiResponse(responseCode = "403", description = "Game session does not belong to the authenticated user")
    })
    @PostMapping("/{gameId}/turn")
    public ResponseEntity<TurnResponse> executeTurn(
                @PathVariable UUID gameId,
                // We have to use @io.swagger.v3.oas.annotations.parameters.RequestBody since we don't have import alias
                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                        @ExampleObject(name = "Travel",    value = "{\"actionType\":\"TRAVEL\"}"),
                        @ExampleObject(name = "Rest",      value = "{\"actionType\":\"REST\"}"),
                        @ExampleObject(name = "Supply",    value = "{\"actionType\":\"SUPPLY\"}"),
                        @ExampleObject(name = "Freelance", value = "{\"actionType\":\"FREELANCE\"}"),
                        @ExampleObject(name = "Hackathon", value = "{\"actionType\":\"HACKATHON\"}"),
                        @ExampleObject(name = "Debug",     value = "{\"actionType\":\"DEBUG\"}")
                    }))
                @RequestBody @Valid TurnRequest turnRequest,
                @AuthenticationPrincipal User user) {
        TurnResponse response = turnService.processTurn(gameId, user.getId(), turnRequest);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Resolve a pending event choice",
        description = "When a turn response contains a `pendingEventId`, the event's choices are returned in `eventChoices`. " +
            "Pick one by submitting its `id` here. Stat changes are applied immediately. " +
            "You cannot take another turn until the pending event is resolved.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event choice resolved"),
        @ApiResponse(responseCode = "400", description = "No pending event, or choice does not belong to the pending event")
    })
    @PostMapping("/{gameId}/event-choice")
    public ResponseEntity<EventChoiceResponse> resolveEventChoice(
                @PathVariable UUID gameId,
                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = "{\"choiceId\":\"<uuid-from-eventChoices>\"}"))) 
                @RequestBody @Valid EventChoiceRequest choiceRequest,
                @AuthenticationPrincipal User user) {
        EventChoiceResponse response = turnService.resolveEventChoice(gameId, user.getId(), choiceRequest);
        return ResponseEntity.ok(response);
    }
}
