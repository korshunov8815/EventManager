package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by anna on 07.04.15.
 */
@Repository
public class ParticipantDaoImpl implements ParticipantDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveParticipant(Participant participant) {
        em.persist(participant);
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
}