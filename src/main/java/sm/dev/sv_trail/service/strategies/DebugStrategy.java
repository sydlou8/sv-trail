package sm.dev.sv_trail.service.strategies;

import org.springframework.stereotype.Service;

import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.enums.ActionType;

@Service
public class DebugStrategy implements ActionStrategy {
    @Override
    public TurnResponse executeAction(GameSession gameSession) {
        // Set constants for Debug action. Reduce bug count and reduce coffee supply as tradeoff
        // you cannot debug if you have no coffee supply, so reduce coffee supply by 1, and reduce bug count by 5
        final int BUG_DELTA = -5;
        final int COFFEE_DELTA = -1;
        if(gameSession.getCoffeeSupply() <= 0) {
            throw new IllegalStateException("Cannot debug without coffee supply! Please get supplies first.");
        }
        gameSession.setBugCount(gameSession.getBugCount() + BUG_DELTA);
        gameSession.setCoffeeSupply(gameSession.getCoffeeSupply() + COFFEE_DELTA);

        return TurnResponse.builder()
            .actionType(getActionType())
            .description(String.format("You squashed %d bugs. Used 1 coffee.", Math.abs(BUG_DELTA)))
            .gameSessionId(gameSession.getId())
            .userId(gameSession.getUser().getId())
            .bugCountDelta(BUG_DELTA)
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
        return ActionType.DEBUG;
    }

}
