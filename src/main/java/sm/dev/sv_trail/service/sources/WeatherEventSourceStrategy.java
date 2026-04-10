package sm.dev.sv_trail.service.sources;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.common.client.WeatherClient;
import sm.dev.sv_trail.common.client.WeatherData;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.enums.WeatherCondition;

@Component
@RequiredArgsConstructor
public class WeatherEventSourceStrategy implements EventSourceStrategy {
    private final WeatherClient weatherClient;

    @Override
    public EventSourceResult apply(GameSession gameSession) {
        WeatherData weather = weatherClient.getWeather(
            gameSession.getCurrentLocation().getLatitude(),
            gameSession.getCurrentLocation().getLongitude());
        if (weather == null) return EventSourceResult.empty();

        WeatherCondition condition = weather.condition();
        applyConditionDeltas(gameSession, condition);
        applyTemperatureModifier(gameSession, weather.current().temperature2m());

        return new EventSourceResult(
            "New Weather Forecast!",
            "Weather conditions are affecting your journey: " + condition.getDescription(),
            condition.getDescription(),
            weather.current().temperature2m(),
            null);
    }

    private void applyConditionDeltas(GameSession gs, WeatherCondition condition) {
        gs.setCash(gs.getCash() + condition.getCashDelta());
        gs.setMorale(Math.max(0, Math.min(100, gs.getMorale() + condition.getMoraleDelta())));
        gs.setHype(Math.max(0, Math.min(100, gs.getHype() + condition.getHypeDelta())));
        gs.setBugCount(Math.max(0, gs.getBugCount() + condition.getBugCountDelta()));
        gs.setCoffeeSupply(Math.max(0, gs.getCoffeeSupply() + condition.getCoffeeSupplyDelta()));
    }

    private void applyTemperatureModifier(GameSession gs, double temp) {
        if (temp >= 95) {
            gs.setCoffeeSupply(Math.max(0, gs.getCoffeeSupply() - 2));
            gs.setMorale(Math.max(0, gs.getMorale() - 10));
        } else if (temp >= 80) {
            gs.setCoffeeSupply(Math.max(0, gs.getCoffeeSupply() - 1));
            gs.setMorale(Math.max(0, gs.getMorale() - 5));
        } else if (temp <= 32) {
            gs.setMorale(Math.max(0, gs.getMorale() - 10));
            gs.setBugCount(gs.getBugCount() + 3);
        } else if (temp <= 50) {
            gs.setMorale(Math.max(0, gs.getMorale() - 5));
        }
        // 51–79 °F: comfortable, no modifier
    }
}
