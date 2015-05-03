package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.dao.ParticipantDao;
import com.fivehundredtwelve.event.model.Participant;
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
}

