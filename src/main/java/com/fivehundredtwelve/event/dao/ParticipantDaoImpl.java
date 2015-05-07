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
 * Created by anna on 07.04.15.
 */
@Repository
public class ParticipantDaoImpl implements ParticipantDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Participant saveParticipant(Participant participant) {
        em.persist(participant);
        return participant;
    }

    @Override
    public List<Participant> getAllParticipants() {
        return em.createQuery("from Participant", Participant.class).getResultList();
    }

    @Override
    public boolean ifParticipantExistByEmail(String mail) {
        if (em.createQuery("SELECT p FROM Participant p WHERE p.email LIKE :email").setParameter("email", mail).getResultList().size() > 0)
            return true;
        else return false;
    }

    @Override
    public void addTaskToParticipant(Task t, Participant p) {
        Participant participant = em.find(Participant.class, p.getId());
        if (t.getId() == 0) {
            em.persist(t);
        }
        Task task = em.find(Task.class, t.getId());
        participant.getTasks().add(task);
        task.setTaskKeeper(participant);
    }

    @Override
    public Participant getParticipantById(int id) {
        return em.find(Participant.class, id);
    }

    @Override
    public Participant getParticipantByEmail(String email) {
        Query q1 = em.createQuery("SELECT p FROM Participant p WHERE p.email LIKE :email").setParameter("email",email);
        Participant participant = (Participant)q1.getSingleResult();
        return participant;
    }

    @Override
    public void addSessionToParticipant (Session s, Participant p){
        Participant participant = em.find(Participant.class, p.getId());
        em.persist(s);
        Session session = em.find(Session.class, s.getSessionID());
        participant.getSessions().add(session);
        session.setSessionOwner(participant);
    }
}
