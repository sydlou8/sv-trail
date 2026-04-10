package sm.dev.sv_trail.service.strategies;

import java.util.Random;

import org.springframework.stereotype.Service;

import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.enums.ActionType;

@Service
public class HackathonStrategy implements ActionStrategy {
    @Override
    public TurnResponse executeAction(GameSession gameSession) {
        // Set constants for Hackathon action. 
        // 15% chance to increase cash, morale, and hype, but also always reduce coffee supply as tradeoff
        // if you do not have coffee supply, you cannot do a hackathon, so reduce coffee supply by 1, 
        // if you lose the hackathon you gain no cash, morale, or hype, and you still lose the coffee supply 
        Random random = new Random();
        int cashDelta = 0;
        int moraleDelta = 0;
        int hypeDelta = 0;
        int coffeeDelta = -1; // Reduce coffee supply by 1 as tradeoff for attempting the hackathon
        if (gameSession.getCoffeeSupply() <= 0) {
            throw new IllegalStateException("Cannot participate in hackathon without coffee supply! Please get supplies first.");
        }
        String description;
        if (random.nextDouble() < 0.15) {
            cashDelta = 1000;
            moraleDelta = 20;
            hypeDelta = 10;

            gameSession.setCash(gameSession.getCash() + cashDelta);
            gameSession.setMorale(gameSession.getMorale() + moraleDelta);
            gameSession.setHype(gameSession.getHype() + hypeDelta);
            description = String.format("Congrats! Your team won the hackathon! You gained $%d, +%d morale, +%d hype.", cashDelta, moraleDelta, hypeDelta);
        } else {
            moraleDelta = -5; // Losing the hackathon reduces morale slightly
            hypeDelta = -5; // Losing the hackathon reduces hype slightly

            gameSession.setMorale(gameSession.getMorale() + moraleDelta);
            gameSession.setHype((gameSession.getHype() + hypeDelta) < 0 ? 0 : gameSession.getHype() + hypeDelta);
            description = String.format("Your team didn't place in the hackathon. %d morale, %d hype.", moraleDelta, hypeDelta);
        }
        gameSession.setCoffeeSupply(gameSession.getCoffeeSupply() + coffeeDelta); // Reduce coffee supply as tradeoff

        return TurnResponse.builder()
            .actionType(getActionType())
            .description(description)
            .gameSessionId(gameSession.getId())
            .userId(gameSession.getUser().getId())
            .cashDelta(cashDelta)
            .moraleDelta(moraleDelta)
            .hypeDelta(hypeDelta)
            .coffeeSupplyDelta(coffeeDelta)
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
        return ActionType.HACKATHON;
    }
    
}
