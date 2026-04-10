package sm.dev.sv_trail.service.sources;

import sm.dev.sv_trail.model.entity.GameSession;

public interface EventSourceStrategy {
    /**
     * Applies this source's effect to the game session and returns the result
     * so TurnService can include it in the response.
     */
    EventSourceResult apply(GameSession gameSession);
}
