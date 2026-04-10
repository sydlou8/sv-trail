package sm.dev.sv_trail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.entity.Location;
import sm.dev.sv_trail.model.entity.User;
import sm.dev.sv_trail.model.enums.ActionType;
import sm.dev.sv_trail.repository.LocationRepository;
import sm.dev.sv_trail.service.strategies.TravelStrategy;

@ExtendWith(MockitoExtension.class)
public class TravelStrategyTest {
    @Mock LocationRepository locationRepository;
    @InjectMocks TravelStrategy travelStrategy;

    // Test if advancing location properly deducts cash, advances location, and increments day.
    @Test
    void executeAction_deductCash_advancesLocation_incrementsDay() {
        // Arrange & Act
        User user = mock(User.class);
        when(user.getId()).thenReturn(UUID.randomUUID());

        Location currentLocation = mock(Location.class);
        when(currentLocation.getOrderIndex()).thenReturn(1);

        Location nextLocation = mock(Location.class);
        when(nextLocation.getCity()).thenReturn("Sunnyvale");
        when(nextLocation.getState()).thenReturn("CA");

        GameSession gameSession = GameSession.builder()
            .user(user)
            .currentLocation(currentLocation)
            .cash(1000)
            .dayNumber(1)
            .build();

        when(locationRepository.findFirstByOrderIndexGreaterThan(1)).thenReturn(java.util.Optional.of(nextLocation));

        TurnResponse response = travelStrategy.executeAction(gameSession);
        // Assert
        assertThat(gameSession.getCash()).isEqualTo(700); // 1000 - 300 for travel
        assertThat(gameSession.getDayNumber()).isEqualTo(2); // Day should increment by 1
        assertThat(gameSession.getCurrentLocation()).isEqualTo(nextLocation); // Location should update
        assertThat(response.actionType()).isEqualTo(ActionType.TRAVEL);

    }

    // Test if triggersEvent() returns true for TravelStrategy, allowing events to trigger after traveling.
    @Test
    void triggersEvent_returnsTrue() {
        // triggersEvent should return true for TravelStrategy, allowing events to trigger after traveling.
        assertThat(travelStrategy.triggersEvent(mock(GameSession.class))).isTrue();
    }
}
