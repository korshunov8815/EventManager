package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
    public void addParticipantToEvent(Event e, Participant p) {
        Event event = em.find(Event.class, e.getId());
        if (p.getId() == 0) {
            em.persist(p);
        }
        Participant participant = em.find(Participant.class, p.getId());
        event.getParticipants().add(participant);
        participant.getEvents().add(event);
    }
}
