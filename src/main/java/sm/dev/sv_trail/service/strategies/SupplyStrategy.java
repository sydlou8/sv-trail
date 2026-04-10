package sm.dev.sv_trail.service.strategies;

import org.springframework.stereotype.Service;

import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.enums.ActionType;

@Service
public class SupplyStrategy implements ActionStrategy {
    @Override
    public TurnResponse executeAction(GameSession gameSession) {
        // Set constants for supply action --> Increase coffee supply and reduce cash
        final int COFFEE_DELTA = 3;
        final int CASH_DELTA = -50;
        // Update game session state based on supply action
        gameSession.setCoffeeSupply(gameSession.getCoffeeSupply() + COFFEE_DELTA);
        gameSession.setCash(gameSession.getCash() + CASH_DELTA);

        return TurnResponse.builder()
            .actionType(getActionType())
            .description(String.format("You stocked up on %d coffees for $%d.", COFFEE_DELTA, Math.abs(CASH_DELTA)))
            .gameSessionId(gameSession.getId())
            .userId(gameSession.getUser().getId())
            .cashDelta(CASH_DELTA)
            .coffeeSupplyDelta(COFFEE_DELTA)
            .cash(gameSession.getCash())
            .morale(gameSession.getMorale())
            .hype(gameSession.getHype())
            .bugCount(gameSession.getBugCount())
            .coffeeSupply(gameSession.getCoffeeSupply())
            .dayNumber(gameSession.getDayNumber())
            .currentCity(gameSession.getCurrentLocation().getCity())
            .currentState(gameSession.getCurrentLocation().getState())
            .state(gameSession.getState().name())
            .lossReason(gameSession.getLossReason() != null ? gameSession.getLossReason().name() : null)    
            .build();

    }
    @Override
    public ActionType getActionType(){
        return ActionType.SUPPLY;
    }
    
}
