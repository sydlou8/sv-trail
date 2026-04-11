package sm.dev.sv_trail.service;

import java.util.UUID;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.repository.EventChoiceRepository;
import sm.dev.sv_trail.repository.GameSessionRepository;
import sm.dev.sv_trail.repository.LocationRepository;
import sm.dev.sv_trail.service.factories.ActionStrategyFactory;
import sm.dev.sv_trail.service.factories.EventSourceFactory;
import sm.dev.sv_trail.service.sources.EventSourceResult;
import sm.dev.sv_trail.service.strategies.ActionStrategy;
import sm.dev.sv_trail.model.dto.request.EventChoiceRequest;
import sm.dev.sv_trail.model.dto.request.TurnRequest;
import sm.dev.sv_trail.model.dto.response.EventChoiceResponse;
import sm.dev.sv_trail.model.dto.response.PendingEventResponse;
import sm.dev.sv_trail.model.dto.response.TurnResponse;
import sm.dev.sv_trail.model.entity.Event;
import sm.dev.sv_trail.model.entity.EventChoice;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.model.enums.GameState;
import sm.dev.sv_trail.model.enums.LossReason;
import sm.dev.sv_trail.exception.PendingEventException;

@Service
@RequiredArgsConstructor
public class TurnService {
    private final LocationRepository locationRepository;
    private final GameSessionRepository gameSessionRepository;
    private final EventChoiceRepository eventChoiceRepository;
    private final ActionStrategyFactory actionStrategyFactory;
    private final EventSourceFactory eventSourceFactory;

    @Transactional
    public TurnResponse processTurn(UUID gameSessionId, UUID userId, TurnRequest turnRequest) {
        GameSession gameSession = gameSessionRepository.findByIdAndUserId(gameSessionId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Game session not found for the given ID and user ID!"));

        if (!gameSession.getState().equals(GameState.ACTIVE)) {
            throw new IllegalStateException("Game session is not active! Current state: " + gameSession.getState());
        }

        if (gameSession.getPendingEvent() != null) {
            Event event = gameSession.getPendingEvent();
            List<EventChoiceResponse> choices = eventChoiceRepository.findByEventId(event.getId())
                .orElse(List.of())
                .stream()
                .map(c -> EventChoiceResponse.builder()
                    .id(c.getId())
                    .description(c.getDescription())
                    .build())
                .toList();
            throw new PendingEventException(
                PendingEventResponse.builder()
                    .eventId(event.getId())
                    .title(event.getTitle())
                    .description(event.getDescription())
                    .choices(choices)
                    .build());
        }

        ActionStrategy strategy = actionStrategyFactory.getStrategy(turnRequest.actionType());
        TurnResponse turnResponse = strategy.executeAction(gameSession);

        // Events only trigger on TRAVEL (TravelStrategy.triggersEvent() returns true).
        // A random source is chosen: seeded story event, weather forecast, or trending GitHub repo.
        EventSourceResult eventResult = EventSourceResult.empty();
        if (strategy.triggersEvent(gameSession)) {
            eventResult = eventSourceFactory.getRandom(gameSession);
        }

        turnResponse = buildResponse(gameSession, turnResponse, eventResult);

        // Check lose condition: cash or morale at zero
        if (gameSession.getCash() <= 0 || gameSession.getMorale() <= 0) {
            turnResponse = checkLoseCondition(gameSession, turnResponse, eventResult);
        } else {
            // Win condition: reached the last location with no pending event
            boolean isLastLocation = locationRepository
                .findFirstByOrderIndexGreaterThan(gameSession.getCurrentLocation().getOrderIndex())
                .isEmpty();
            if (isLastLocation && gameSession.getPendingEvent() == null) {
                gameSession.setState(GameState.WON);
                turnResponse = buildResponse(gameSession, turnResponse, eventResult);
            }
        }

        gameSessionRepository.save(gameSession);
        return turnResponse;
    }

    @Transactional
    public EventChoiceResponse resolveEventChoice(UUID gameSessionId, UUID userId, EventChoiceRequest choiceRequest) {
        GameSession gameSession = gameSessionRepository.findByIdAndUserId(gameSessionId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Game session not found for the given ID and user ID!"));
        if (!gameSession.getState().equals(GameState.ACTIVE)) {
            throw new IllegalStateException("Game session is not active! Current state: " + gameSession.getState());
        }
        if (gameSession.getPendingEvent() == null) {
            throw new IllegalStateException("No pending event to resolve!");
        }

        EventChoice eventChoice = eventChoiceRepository.findById(choiceRequest.choiceId())
            .orElseThrow(() -> new IllegalArgumentException("Event choice not found for the given ID!"));
        if (!eventChoice.getEvent().getId().equals(gameSession.getPendingEvent().getId())) {
            throw new IllegalArgumentException("The selected choice does not belong to the pending event!");
        }

        gameSession.setCash(gameSession.getCash() + eventChoice.getCashDelta());
        gameSession.setMorale(gameSession.getMorale() + eventChoice.getMoraleDelta());
        gameSession.setHype(gameSession.getHype() + eventChoice.getHypeDelta());
        gameSession.setBugCount(gameSession.getBugCount() + eventChoice.getBugCountDelta());
        gameSession.setCoffeeSupply(gameSession.getCoffeeSupply() + eventChoice.getCoffeeSupplyDelta());

        if (gameSession.getCash() <= 0 || gameSession.getMorale() <= 0) {
            checkLoseCondition(gameSession, null, EventSourceResult.empty());
        }

        gameSession.setPendingEvent(null);
        gameSessionRepository.save(gameSession);

        return EventChoiceResponse.builder()
            .id(eventChoice.getId())
            .description(eventChoice.getDescription())
            .build();
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private TurnResponse checkLoseCondition(GameSession gameSession, TurnResponse turnResponse, EventSourceResult result) {
        if (gameSession.getCash() <= 0 || gameSession.getMorale() <= 0) {
            gameSession.setState(GameState.LOST);
            gameSession.setLossReason(
                gameSession.getCash() <= 0 && gameSession.getMorale() <= 0
                    ? LossReason.BANKRUPTCY_AND_LOW_MORALE
                    : (gameSession.getCash() <= 0 ? LossReason.BANKRUPTCY : LossReason.LOW_MORALE));
            return buildResponse(gameSession, turnResponse, result);
        }
        return turnResponse;
    }

    private TurnResponse buildResponse(GameSession gameSession, TurnResponse turnResponse, EventSourceResult result) {
        return TurnResponse.builder()
            .actionType(turnResponse != null ? turnResponse.actionType() : null)
            .description(turnResponse != null ? turnResponse.description() : null)
            .gameSessionId(gameSession.getId())
            .userId(gameSession.getUser().getId())
            .cashDelta(turnResponse != null ? turnResponse.cashDelta() : 0)
            .moraleDelta(turnResponse != null ? turnResponse.moraleDelta() : 0)
            .hypeDelta(turnResponse != null ? turnResponse.hypeDelta() : 0)
            .bugCountDelta(turnResponse != null ? turnResponse.bugCountDelta() : 0)
            .coffeeSupplyDelta(turnResponse != null ? turnResponse.coffeeSupplyDelta() : 0)
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
            .pendingEventId(gameSession.getPendingEvent() != null ? gameSession.getPendingEvent().getId() : null)
            .eventTitle(gameSession.getPendingEvent() != null ? gameSession.getPendingEvent().getTitle() : result.eventTitle())
            .eventDescription(gameSession.getPendingEvent() != null ? gameSession.getPendingEvent().getDescription() : result.eventDescription())
            .eventChoices(result.pendingChoices())
            .weatherDescription(result.weatherDescription())
            .temperature(result.temperature())
            .build();
    }
}
