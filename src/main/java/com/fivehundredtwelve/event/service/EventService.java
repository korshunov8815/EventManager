package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;

import java.util.List;
import java.util.Set;

/**
 * Created by anna on 06.04.15.
 */
public interface EventService {

    public Event saveEvent(Event event);
    public List<Event> getAllEvents();
    public void addParticipantToEvent(Participant p, Event e);
    public Event getEventById(int id);
    public Task addTaskToEvent(Task t, Event e);
    public Event editEvent(int id, String title, String description);
    public Event deleteEvent(int id, ParticipantService ps);
    public void deleteParticipantFromEvent (int eId, int pId, ParticipantService ps,TaskService ts);
    public Set<Task> eventTaskOwner(int eId, int pId);

}
