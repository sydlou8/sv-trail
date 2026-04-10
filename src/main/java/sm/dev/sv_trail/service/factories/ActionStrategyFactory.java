package sm.dev.sv_trail.service.factories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.model.enums.ActionType;
import sm.dev.sv_trail.service.strategies.ActionStrategy;

@Component
@RequiredArgsConstructor    // using lombok for consistency with other classes  
public class ActionStrategyFactory {
    private final List<ActionStrategy> actionStrategies;
    private Map<ActionType, ActionStrategy> strategyRegistry;

    @PostConstruct
    public void init() {
        strategyRegistry = new HashMap<>();
        actionStrategies.forEach(strategy -> 
            strategyRegistry.put(strategy.getActionType(), strategy));
    }

    public ActionStrategy getStrategy(ActionType actionType) {
        ActionStrategy strategy = strategyRegistry.get(actionType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for action type: " + actionType);
        }
        return strategy;
    }
}
