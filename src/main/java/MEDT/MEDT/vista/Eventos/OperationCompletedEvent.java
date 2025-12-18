package MEDT.MEDT.vista.Eventos;

import javafx.event.Event;
import javafx.event.EventType;

public class OperationCompletedEvent extends Event {
    public static final EventType<OperationCompletedEvent> OPERATION_COMPLETED_REQUEST = new EventType<>(Event.ANY, "OPERATION_COMPLETED_REQUEST");

    public OperationCompletedEvent() {
        super(OPERATION_COMPLETED_REQUEST);
    }
}
