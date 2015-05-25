package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Session;
import com.fivehundredtwelve.event.model.Task;

import java.util.List;

/**
 * Created by anna on 07.04.15.
 */
public interface ParticipantDao {
    public Participant saveParticipant(Participant participant);
    public List<Participant> getAllParticipants();
    public boolean ifParticipantExistByEmail(String email);
    public void addTaskToParticipant(Task task, Participant participant);
    public Participant getParticipantById(int id);
    public Participant getParticipantByEmail (String mail);
    public void addSessionToParticipant (Session session, Participant participant);
    public void editParticipantName (int id, String name);
    public Participant getParticipantByregId(String regId);
    public void activate(int id);
    public void deleteSession(int pId, String sId);
}
