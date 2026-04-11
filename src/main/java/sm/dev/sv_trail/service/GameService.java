package sm.dev.sv_trail.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.model.dto.request.CreateGameRequest;
import sm.dev.sv_trail.model.dto.response.EventChoiceResponse;
import sm.dev.sv_trail.model.dto.response.GameStateResponse;
import sm.dev.sv_trail.model.dto.response.PendingEventResponse;
import sm.dev.sv_trail.model.entity.Event;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.entity.User;
import sm.dev.sv_trail.model.entity.Location;
import sm.dev.sv_trail.model.enums.GameState;
import sm.dev.sv_trail.repository.EventChoiceRepository;
import sm.dev.sv_trail.repository.GameSessionRepository;
import sm.dev.sv_trail.repository.LocationRepository;
import sm.dev.sv_trail.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameSessionRepository gameRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final EventChoiceRepository eventChoiceRepository;
    
    @Transactional
    public GameStateResponse createGame(UUID userId, CreateGameRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Location startingLocation = locationRepository.findByOrderIndex(1)
            .orElseThrow(() -> new IllegalArgumentException("Starting location not found")); // Assuming the first location has orderIndex 0

        // Create a new Game Session with initial values based on the selected role
        GameSession gameSession = GameSession.builder()
            .user(user)
            .currentLocation(startingLocation) // Start at the first location
            .role(request.role())
            .state(GameState.ACTIVE)
            .lossReason(null) // No loss reason at the start of the game
            .dayNumber(1) // Start on day 1
            .cash(request.role().getStartingCash())
            .morale(request.role().getStartingMorale())
            .hype(request.role().getStartingHype())
            .bugCount(request.role().getStartingBugCount()) 
            .coffeeSupply(request.role().getStartingCoffee())
            .build();

        // Save session to generate UUID and persist initial state
        GameSession savedGameSession = gameRepository.save(gameSession);

        // Build a GameStateResponse based on the saved session to return to the client
        return mapToGameStateResponse(savedGameSession);

    }

    @Transactional(readOnly = true)
    public GameStateResponse getCurrentState(UUID gameId, UUID userId) {
        GameSession gameSession = gameRepository.findByIdAndUserId(gameId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Game session not found for the given user"));
        return mapToGameStateResponse(gameSession);
    }

    // List all games for a user
    @Transactional(readOnly = true)
    public List<GameStateResponse> listAllGames(UUID userId) {
        List<GameSession> gameSessions = gameRepository.findAllByUserId(userId)
            .orElse(List.of());
        
        return gameSessions.stream()
            .map(this::mapToGameStateResponse)
            .toList();
    }

    // helper method to convert GameSession to GameStateResponse
    private GameStateResponse mapToGameStateResponse(GameSession gameSession) {
        String lossReason = gameSession.getLossReason() != null
            ? gameSession.getLossReason().name() : null;

        PendingEventResponse pendingEvent = null;
        if (gameSession.getPendingEvent() != null) {
            Event event = gameSession.getPendingEvent();
            List<EventChoiceResponse> choices = eventChoiceRepository.findByEventId(event.getId())
                .orElse(List.of())
                .stream()
                .map(c -> EventChoiceResponse.builder()
                    .id(c.getId())
                    .description(c.getDescription())
                    .build())
                .toList();
            pendingEvent = PendingEventResponse.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .choices(choices)
                .build();
        }

        return GameStateResponse.builder()
            .id(gameSession.getId())
            .state(gameSession.getState().name())
            .role(gameSession.getRole().name())
            .lossReason(lossReason)
            .cash(gameSession.getCash())
            .morale(gameSession.getMorale())
            .hype(gameSession.getHype())
            .bugCount(gameSession.getBugCount())
            .coffeeSupply(gameSession.getCoffeeSupply())
            .dayNumber(gameSession.getDayNumber())
            .currentCity(gameSession.getCurrentLocation().getCity())
            .currentState(gameSession.getCurrentLocation().getState())
            .currentTechCompany(gameSession.getCurrentLocation().getTechCompany())
            .pendingEvent(pendingEvent)
            .build();
    }
}
