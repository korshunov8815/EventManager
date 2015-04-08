package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Participant;

import java.util.List;

/**
 * Created by anna on 07.04.15.
 */
public interface ParticipantDao {
    public void saveParticipant(Participant participant);
    public List<Participant> getAllParticipants();
    public boolean ifParticipantExistByEmail(String email);
}
