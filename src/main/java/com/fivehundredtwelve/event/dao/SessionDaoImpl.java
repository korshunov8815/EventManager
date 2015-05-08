package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Session;
import com.fivehundredtwelve.event.model.Task;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by korshunov on 07.05.15.
 */
@Repository
public class SessionDaoImpl implements SessionDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Session saveSession(Session session) {
        em.persist(session);
        return em.find(Session.class, session.getSessionID());
    }

    @Override
    public List<Session> getAllSessions() {
        return em.createQuery("from session", Session.class).getResultList();
    }

}