package sm.dev.sv_trail.service.sources;

import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import sm.dev.sv_trail.model.dto.response.EventChoiceResponse;
import sm.dev.sv_trail.model.entity.Event;
import sm.dev.sv_trail.model.entity.GameSession;
import sm.dev.sv_trail.repository.EventChoiceRepository;
import sm.dev.sv_trail.repository.EventRepository;

@Component
@RequiredArgsConstructor
public class SeededEventSourceStrategy implements EventSourceStrategy {
    private final EventRepository eventRepository;
    private final EventChoiceRepository eventChoiceRepository;

    @Override
    public EventSourceResult apply(GameSession gameSession) {
        List<Event> events = eventRepository
            .findByLocationIdAndActiveTrue(gameSession.getCurrentLocation().getId())
            .orElse(List.of());
        if (events.isEmpty()) return EventSourceResult.empty();

        Event event = events.get(new Random().nextInt(events.size()));
        List<EventChoiceResponse> choices = eventChoiceRepository.findByEventId(event.getId())
            .orElse(List.of())
            .stream()
            .map(choice -> EventChoiceResponse.builder()
                .id(choice.getId())
                .description(choice.getDescription())
                .build())
            .toList();

        gameSession.setPendingEvent(event);
        // title/description are read from gameSession.getPendingEvent() in buildResponse
        return new EventSourceResult(null, null, null, 0.0, choices);
    }
}
