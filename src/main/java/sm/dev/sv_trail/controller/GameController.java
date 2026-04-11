package sm.dev.sv_trail.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.model.dto.request.CreateGameRequest;
import sm.dev.sv_trail.model.dto.response.GameStateResponse;
import sm.dev.sv_trail.model.entity.User;
import sm.dev.sv_trail.service.GameService;

@Tag(name = "Game", description = "Create and inspect game sessions. Requires a valid JWT token.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @Operation(summary = "Create a new game session",
        description = "Starts a new game. Choose a role:\n" +
            "- `BEGINNER_DEV` — $1000 cash, 80 morale, 10 hype, 30 coffee\n" +
            "- `INTERMEDIATE_DEV` — $3000 cash, 50 morale, 20 hype, 20 coffee\n" +
            "- `ADVANCED_DEV` — $5000 cash, 30 morale, 30 hype, 10 coffee")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Game session created"),
        @ApiResponse(responseCode = "400", description = "Invalid role or request")
    })
    @PostMapping()
    public ResponseEntity<GameStateResponse> createGame(
                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                        @ExampleObject(name = "Beginner", value = "{\"role\":\"BEGINNER_DEV\"}"),
                        @ExampleObject(name = "Intermediate", value = "{\"role\":\"INTERMEDIATE_DEV\"}"),
                        @ExampleObject(name = "Advanced", value = "{\"role\":\"ADVANCED_DEV\"}")
                    }))
                @RequestBody @Valid CreateGameRequest request,
                @AuthenticationPrincipal User user) {
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(gameService.createGame(user.getId(), request));
    }

    @Operation(summary = "Get the current state of a game session",
        description = "Returns the full current state of the game. Use this to resume a saved session.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Game state returned"),
        @ApiResponse(responseCode = "400", description = "Game session not found or does not belong to the authenticated user")
    })
    @GetMapping("/{gameId}/state")
    public ResponseEntity<GameStateResponse> getGameState(
                @PathVariable UUID gameId,
                @AuthenticationPrincipal User user) {
        
        return ResponseEntity.ok(gameService.getCurrentState(gameId, user.getId()));
    }
    // list all games
    @Operation(summary = "List all game sessions for the authenticated user",
        description = "Returns a list of all game sessions associated with the authenticated user, including their current state and progress.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of game sessions returned"),
        @ApiResponse(responseCode = "400", description = "Error retrieving game sessions")
    })
    @GetMapping("/all")
    public ResponseEntity<List<GameStateResponse>> listAllGames(
        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(gameService.listAllGames(user.getId()));
    }
}
