package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;

import java.util.List;

/**
 * Created by anna on 05.04.15.
 */
public interface EventDao {
    public Event saveEvent(Event event);
    public List<Event> getAllEvents();
    public void addParticipantToEvent(Event e, Participant participant);
    public Event getEventById(int id);
}
