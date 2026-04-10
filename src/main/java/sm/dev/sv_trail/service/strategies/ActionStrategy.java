package sm.dev.sv_trail.service.strategies;

import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.enums.ActionType;

public interface ActionStrategy {
    public TurnResponse executeAction(GameSession gameSession);
    public ActionType getActionType();
    // Events only trigger on TRAVEL. TravelStrategy overrides this to return true.
    default boolean triggersEvent(GameSession gameSession) {
        return false;
    }
}
