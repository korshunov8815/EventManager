package com.fivehundredtwelve.event.utils;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;

import java.util.List;
import java.util.Set;

/**
 * Created by korshunov on 24.05.15.
 */
public class eWrapper {


    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public eWrapper(List<Event> events) {
        this.setEvents(events);
    }

}
