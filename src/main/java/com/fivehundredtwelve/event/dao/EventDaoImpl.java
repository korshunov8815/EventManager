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
    public void saveEvent(Event event) {
        em.persist(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return em.createQuery("from Event", Event.class).getResultList();
    }

    @Override
    public void addParticipantToEvent(Event e, Participant p) {
        Event event = em.find(Event.class, e.getId());
        event.getParticipants().add(p);
        em.persist(event);
        Participant participant = em.find(Participant.class, p.getId());
        participant.getEvents().add(event);
        em.persist(participant);
    }
}
