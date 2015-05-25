package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;
import com.fivehundredtwelve.event.service.TaskService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by anna on 06.04.15.
 */
@Repository
public class EventDaoImpl implements EventDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Event saveEvent(Event event) {
        em.persist(event);
        return getEventById(event.getId());
    }

    @Override
    public List<Event> getAllEvents() {
        return em.createQuery("from Event", Event.class).getResultList();
    }

    @Override
    public Event getEventById(int id) {
        return em.find(Event.class, id);
    }

    @Override
    public void addParticipantToEvent(Participant p, Event e) {
        Event event = em.find(Event.class, e.getId());
        if (p.getId() == 0) {
            em.persist(p);
        }
        Participant participant = em.find(Participant.class, p.getId());
        event.getParticipants().add(participant);
        participant.getEvents().add(event);
    }

    @Override
    public Task addTaskToEvent(Task t, Event e) {
        Event event = em.find(Event.class, e.getId());
        if (t.getId() == 0) {
            em.persist(t);
        }
        Task task = em.find(Task.class, t.getId());
        event.getTasks().add(task);
        task.setTaskEventKeeper(event);
        return task;
    }

    @Override
    public Event editEvent(int id, String title, String description) {
        Event event = em.find(Event.class, id);
        event.setDescription(description);
        event.setTitle(title);
        em.flush();
        return em.find(Event.class, id);
    }

    @Override
    public Event deleteEvent(int id, ParticipantService ps){
        Event event = em.find(Event.class, id);
        List<Participant> participants = ps.getAllParticipants();
        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).getEvents().remove(event);
        }
        em.flush();
        em.remove(event);
        em.flush();
        return event;
    }

    @Override
    public void deleteParticipantFromEvent (int eId, int pId, ParticipantService ps){
        Event event = em.find(Event.class, eId);
        Participant participant = ps.getParticipantById(pId);
        participant.getEvents().remove(event);
        event.getParticipants().remove(participant);
        List<Task> tasks = new ArrayList<Task>();
        tasks.addAll(participant.getTasks());
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setIsTaken(false);
            tasks.get(i).setIsDone(false);
        }
        em.flush();
    }

}
