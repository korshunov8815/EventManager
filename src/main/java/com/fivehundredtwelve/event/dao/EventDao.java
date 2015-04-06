package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Event;

import java.util.List;

/**
 * Created by anna on 05.04.15.
 */
public interface EventDao {
    public void saveEvent(Event event);
    public List<Event> getAllEvents();
}
