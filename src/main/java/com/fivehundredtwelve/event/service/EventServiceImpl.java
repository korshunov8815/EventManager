package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.dao.EventDao;
import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public void addParticipantToEvent(Event e, Participant participant) {
        dao.addParticipantToEvent(e,participant);
    }

    @Transactional
    @Override
    public Task addTaskToEvent(Task t, Event e) {
        return dao.addTaskToEvent(t,e);
    }
}
