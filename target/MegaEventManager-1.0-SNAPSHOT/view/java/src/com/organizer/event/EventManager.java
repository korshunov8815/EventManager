package com.organizer.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aina on 10/4/14.
 */
public class EventManager {

    private final Map<String, Event> events;

    public EventManager() {
        this.events = new HashMap<>();
    }

    void writeEvent(Event event) {
        if (event != null) {
            events.put(event.getName(), event);
        }
    }
}
