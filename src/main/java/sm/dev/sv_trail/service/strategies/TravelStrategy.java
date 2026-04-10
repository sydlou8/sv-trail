package sm.dev.sv_trail.service.strategies;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.entity.Location;
import sm.dev.sv_trail.model.enums.ActionType;
import sm.dev.sv_trail.repository.LocationRepository;

@Service
@RequiredArgsConstructor
public class TravelStrategy implements ActionStrategy {
    private final LocationRepository locationRepository;

    @Override
    public TurnResponse executeAction(GameSession gameSession) {
        Location currentLocation = gameSession.getCurrentLocation();
        Location nextLocation = locationRepository
            .findFirstByOrderIndexGreaterThan(currentLocation.getOrderIndex())
            .orElseThrow(() -> new IllegalStateException("No location found in the database!"));
        // Reduce cash by 300 for both travel and lodging
        final int CASH_DELTA = -300;
        gameSession.setCash(gameSession.getCash() + CASH_DELTA);
        gameSession.setCurrentLocation(nextLocation);
        gameSession.setDayNumber(gameSession.getDayNumber() + 1); // Increment day number on travel
        
        return TurnResponse.builder()
            .actionType(getActionType())
            .description(String.format("You traveled to %s, %s. Travel and lodging cost $%d.", nextLocation.getCity(), nextLocation.getState(), Math.abs(CASH_DELTA)))
            .gameSessionId(gameSession.getId())
            .userId(gameSession.getUser().getId())
            .cashDelta(CASH_DELTA)
            .cash(gameSession.getCash())
            .morale(gameSession.getMorale())
            .hype(gameSession.getHype())
            .bugCount(gameSession.getBugCount())
            .coffeeSupply(gameSession.getCoffeeSupply())
            .dayNumber(gameSession.getDayNumber())
            .currentCity(nextLocation.getCity())
            .currentState(nextLocation.getState())
            .state(gameSession.getState().name())
            .lossReason(gameSession.getLossReason() != null ? gameSession.getLossReason().name() : null)    
            .build();
    }
    @Override
    public ActionType getActionType(){
        return ActionType.TRAVEL;
    }
    @Override
    public boolean triggersEvent(GameSession gameSession) {
        return true;
    }
}
