package sm.dev.sv_trail.exception;

import sm.dev.sv_trail.model.dto.response.PendingEventResponse;

public class PendingEventException extends RuntimeException {
    private final PendingEventResponse pendingEvent;

    public PendingEventException(PendingEventResponse pendingEvent) {
        super("You have a pending event that must be resolved before taking another turn.");
        this.pendingEvent = pendingEvent;
    }

    public PendingEventResponse getPendingEvent() {
        return pendingEvent;
    }
}
