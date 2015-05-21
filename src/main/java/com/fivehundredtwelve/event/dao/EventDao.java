package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import com.fivehundredtwelve.event.service.ParticipantService;

import java.util.List;
import java.util.Set;

/**
 * Created by anna on 05.04.15.
 */
public interface EventDao {
    public Event saveEvent(Event event);
    public List<Event> getAllEvents();
    public void addParticipantToEvent(Participant participant, Event e);
    public Event getEventById(int id);
    public Task addTaskToEvent(Task t, Event e);
    public Event editEvent(int id, String title, String description);
    public Event deleteEvent(int id, ParticipantService ps);
}
