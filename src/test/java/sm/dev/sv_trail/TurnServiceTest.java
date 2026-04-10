package sm.dev.sv_trail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sm.dev.sv_trail.model.dto.request.TurnRequest;
import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.entity.Location;
import sm.dev.sv_trail.model.entity.User;
import sm.dev.sv_trail.model.enums.ActionType;
import sm.dev.sv_trail.model.enums.GameState;
import sm.dev.sv_trail.model.enums.LossReason;
import sm.dev.sv_trail.repository.EventChoiceRepository;
import sm.dev.sv_trail.repository.GameSessionRepository;
import sm.dev.sv_trail.repository.LocationRepository;
import sm.dev.sv_trail.service.TurnService;
import sm.dev.sv_trail.service.factories.ActionStrategyFactory;
import sm.dev.sv_trail.service.factories.EventSourceFactory;
import sm.dev.sv_trail.service.strategies.ActionStrategy;

@ExtendWith(MockitoExtension.class)
class TurnServiceTest {

    @Mock LocationRepository locationRepository;
    @Mock GameSessionRepository gameSessionRepository;
    @Mock EventChoiceRepository eventChoiceRepository;
    @Mock ActionStrategyFactory actionStrategyFactory;
    @Mock EventSourceFactory eventSourceFactory;
    @InjectMocks TurnService turnService;

    // Helper method to create a mock GameSession with specified cash and morale values
    private GameSession buildSession(int cash, int morale) {
        User user = mock(User.class);
        when(user.getId()).thenReturn(UUID.randomUUID());
        Location location = mock(Location.class);
        when(location.getCity()).thenReturn("San Jose");
        when(location.getState()).thenReturn("CA");
        when(location.getOrderIndex()).thenReturn(5);
        return GameSession.builder()
            .user(user).currentLocation(location)
            .cash(cash).morale(morale).hype(50).bugCount(0).coffeeSupply(5)
            .dayNumber(3).state(GameState.ACTIVE).build();
    }

    // Test case for winning the game by reaching the last location with no pending event
    @Test
    void processTurn_setsWonState_whenLastLocationAndNoPendingEvent() {
        // Arrange & Act
        GameSession session = buildSession(1000, 50);
        UUID sessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ActionStrategy mockStrategy = mock(ActionStrategy.class);
        when(mockStrategy.executeAction(session)).thenReturn(
            TurnResponse.builder().actionType(ActionType.REST).build());
        when(mockStrategy.triggersEvent(session)).thenReturn(false);
        when(actionStrategyFactory.getStrategy(any())).thenReturn(mockStrategy);
        when(gameSessionRepository.findByIdAndUserId(sessionId, userId))
            .thenReturn(Optional.of(session));
        when(locationRepository.findFirstByOrderIndexGreaterThan(5))
            .thenReturn(Optional.empty()); // last location

        TurnResponse result = turnService.processTurn(sessionId, userId, new TurnRequest(ActionType.REST));
        
        // Assert
        assertThat(session.getState()).isEqualTo(GameState.WON);
        assertThat(result.state()).isEqualTo("WON");
    }

    // Test case for losing the game due to bankruptcy when cash is zero
    @Test
    void processTurn_setsLostAndBankruptcy_whenCashIsZero() {
        // Arrange & Act
        GameSession session = buildSession(0, 50); // starts at cash=0
        UUID sessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ActionStrategy mockStrategy = mock(ActionStrategy.class);
        when(mockStrategy.executeAction(session)).thenReturn(
            TurnResponse.builder().actionType(ActionType.REST).build());
        when(mockStrategy.triggersEvent(session)).thenReturn(false);
        when(actionStrategyFactory.getStrategy(any())).thenReturn(mockStrategy);
        when(gameSessionRepository.findByIdAndUserId(sessionId, userId))
            .thenReturn(Optional.of(session));

        TurnResponse result = turnService.processTurn(sessionId, userId, new TurnRequest(ActionType.REST));

        // Assert
        assertThat(session.getState()).isEqualTo(GameState.LOST);
        assertThat(session.getLossReason()).isEqualTo(LossReason.BANKRUPTCY);
        assertThat(result.state()).isEqualTo("LOST");
    }
}
