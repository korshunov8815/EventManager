package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;

import java.util.List;

/**
 * Created by anna on 07.04.15.
 */
public interface ParticipantService {

    public void saveParticipant(Participant participant);
    public List<Participant> getAllParticipants();
    public boolean ifParticipantExistByEmail(String email);
    public void addTaskToParticipant(Task task, Participant participant);
    public Participant getParticipantById(int id);

}
