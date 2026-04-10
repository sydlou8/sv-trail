package sm.dev.sv_trail.service.strategies;

import org.springframework.stereotype.Service;
import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.enums.ActionType;


@Service
public class RestStrategy implements ActionStrategy {
    @Override
    public TurnResponse executeAction(GameSession gameSession) {
        // Set constants for rest action --> increase morale and reduce hype
        final int MORALE_DELTA = 10;
        final int HYPE_DELTA = -5;
        // Update game session state based on rest action
        gameSession.setMorale(gameSession.getMorale() + MORALE_DELTA);
        gameSession.setHype((gameSession.getHype() + HYPE_DELTA) < 0 ? 0 : gameSession.getHype() + HYPE_DELTA);
        // Build and return TurnResponse with updated game session state
        return TurnResponse.builder()
            .actionType(getActionType())
            .description(String.format("You took a rest. +%d morale, %d hype.", MORALE_DELTA, HYPE_DELTA))
            .gameSessionId(gameSession.getId())
            .userId(gameSession.getUser().getId())
            .moraleDelta(MORALE_DELTA)
            .hypeDelta(HYPE_DELTA)
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
        return ActionType.REST;
    }

}
