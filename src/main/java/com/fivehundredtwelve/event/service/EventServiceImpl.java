package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.dao.EventDao;
import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by anna on 06.04.15.
 */
@Service("eventService")
public class EventServiceImpl implements EventService{

    @Autowired
    private EventDao dao;

    @Transactional
    @Override
    public Event saveEvent(Event event) {
        return dao.saveEvent(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return dao.getAllEvents();
    }

    @Override
    public Event getEventById(int id) {
        return dao.getEventById(id);
    }

    @Transactional
    @Override
    public void addParticipantToEvent(Participant participant, Event e) {
        dao.addParticipantToEvent(participant, e);
    }

    @Transactional
    @Override
    public Task addTaskToEvent(Task t, Event e) {
        return dao.addTaskToEvent(t,e);
    }

    @Transactional
    @Override
    public Event editEvent(int id, String title, String description) {
        return dao.editEvent(id, title, description);
    }

    @Transactional
    @Override
    public Event deleteEvent(int id, ParticipantService ps) {
       return dao.deleteEvent(id, ps);
    }

    @Transactional
    @Override
    public void deleteParticipantFromEvent (int eId, int pId, ParticipantService ps,TaskService ts) {dao.deleteParticipantFromEvent(eId, pId, ps,ts);}

    @Transactional
    @Override
    public Set<Task> eventTaskOwner(int eId, int pId){return dao.eventTaskOwner(eId, pId);}



}
