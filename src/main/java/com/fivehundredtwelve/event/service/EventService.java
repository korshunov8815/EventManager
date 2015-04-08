package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;

import java.util.List;

/**
 * Created by anna on 06.04.15.
 */
public interface EventService {

    public Event saveEvent(Event event);
    public List<Event> getAllEvents();
    public void addParticipantToEvent(Event e, Participant participant);
    public Event getEventById(int id);

}
