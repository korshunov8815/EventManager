package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Participant;
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
}
