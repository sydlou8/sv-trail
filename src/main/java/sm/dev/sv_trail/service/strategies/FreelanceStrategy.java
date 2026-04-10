package sm.dev.sv_trail.service.strategies;

import org.springframework.stereotype.Service;

import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.enums.ActionType;

@Service
public class FreelanceStrategy implements ActionStrategy {
    @Override
    public TurnResponse executeAction(GameSession gameSession) {
        // Set constants for freelance action. Increase cash based on role and morale, but has no effect on hype or bug count.
        final int CASH_DELTA = gameSession.getRole().getFreelancePay(); // Base cash increase from role, e.g. $100 for beginner, $200 for intermediate, $300 for advanced
        // Calculate cash increase based on morale and player role
        int cashIncrease = CASH_DELTA + (gameSession.getMorale() / 10) * 20; // Increase cash based on morale, e.g. every 10 morale gives an additional $50
        gameSession.setCash(gameSession.getCash() + cashIncrease);
        String description = String.format("Freelance gig complete! You earned $%d.", cashIncrease);
        
        return TurnResponse.builder()
            .actionType(getActionType())
            .description(description)
            .gameSessionId(gameSession.getId())
            .userId(gameSession.getUser().getId())
            .cashDelta(cashIncrease)
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
        return ActionType.FREELANCE;
    }
    
}
