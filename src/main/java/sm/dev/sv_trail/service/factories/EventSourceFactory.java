package sm.dev.sv_trail.service.factories;

import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.service.sources.EventSourceResult;
import sm.dev.sv_trail.service.sources.EventSourceStrategy;

@Component
@RequiredArgsConstructor
public class EventSourceFactory {
    // Spring injects all EventSourceStrategy beans automatically
    private final List<EventSourceStrategy> strategies;

    /**
     * Picks a random strategy and runs it. If the chosen strategy produces
     * nothing (e.g. weather API is down, or no seeded events for this location)
     * it tries each remaining strategy in order so TRAVEL always shows an event
     * when one is available.
     */
    public EventSourceResult getRandom(GameSession gameSession) {
        List<EventSourceStrategy> shuffled = new java.util.ArrayList<>(strategies);
        java.util.Collections.shuffle(shuffled, new Random());
        for (EventSourceStrategy strategy : shuffled) {
            EventSourceResult result = strategy.apply(gameSession);
            if (!isEmpty(result)) return result;
        }
        return EventSourceResult.empty();
    }

    private boolean isEmpty(EventSourceResult result) {
        return result.eventTitle() == null
            && result.weatherDescription() == null
            && result.pendingChoices() == null;
    }
}
