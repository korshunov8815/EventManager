package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.model.Event;

import java.util.List;

/**
 * Created by anna on 06.04.15.
 */
public interface EventService {

    public void saveEvent(Event event);
    public List<Event> getAllEvents();

}
