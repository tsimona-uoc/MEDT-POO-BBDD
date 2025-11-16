package MEDT.MEDT.vista.Eventos;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Tab;

public class TabCloseEvent extends Event {
    public static final EventType<TabCloseEvent> CLOSE_REQUEST = new EventType<>(Event.ANY, "TAB_CLOSE_REQUEST");

    public TabCloseEvent() {
        super(CLOSE_REQUEST);
    }
}
