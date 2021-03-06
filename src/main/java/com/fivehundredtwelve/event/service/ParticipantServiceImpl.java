package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.dao.ParticipantDao;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Session;
import com.fivehundredtwelve.event.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by anna on 07.04.15.
 */

@Service("participantService")
public class ParticipantServiceImpl implements ParticipantService {

    @Autowired
    private ParticipantDao dao;

    @Override
    public boolean ifParticipantExistByEmail(String email) {
        return dao.ifParticipantExistByEmail(email);
    }

    @Transactional
    @Override
    public Participant saveParticipant(Participant participant) {
        return dao.saveParticipant(participant);
    }

    @Override
    public List<Participant> getAllParticipants() {
        return dao.getAllParticipants();
    }

    @Transactional
    @Override
    public void addTaskToParticipant(Task task, Participant participant) {
        dao.addTaskToParticipant(task, participant);
    }

    @Override
    public Participant getParticipantById(int id) {
        return dao.getParticipantById(id);
    }

    @Override
    public Participant getParticipantByEmail(String email) {return dao.getParticipantByEmail(email);}

    @Transactional
    @Override
    public void addSessionToParticipant (Session session, Participant participant){
        dao.addSessionToParticipant(session,participant);
    }

    @Transactional
    @Override
    public void editParticipantName (int id, String name) {
        dao.editParticipantName(id, name);
    }

    @Override
    public Participant getParticipantByregId(String regId) {return dao.getParticipantByregId(regId);}

    @Transactional
    @Override
    public void activate(int id) {dao.activate(id);}

    @Transactional
    @Override
    public void deleteSession(int pId, String sId) { dao.deleteSession(pId, sId);}

    @Transactional
    @Override
    public void freeTask(int pId, int tId, TaskService ts) {dao.freeTask(pId, tId, ts);}
}

