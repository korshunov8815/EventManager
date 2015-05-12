package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Session;
import com.fivehundredtwelve.event.model.Task;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @Override
    public boolean ifSessionExist (String sessionId){
        if (em.createQuery("SELECT s FROM Session s WHERE s.sessionID LIKE :sessionID").setParameter("sessionID", sessionId).getResultList().size() > 0)
            return true;
        else return false;
    };

    @Override
    public Participant getParticipantBySession (String sessionId){
        Query q1 = em.createQuery("SELECT s FROM Session s WHERE s.sessionID LIKE :sessionID").setParameter("sessionID",sessionId);
        Session session = (Session)q1.getSingleResult();
        return session.getSessionOwner();
    };

}