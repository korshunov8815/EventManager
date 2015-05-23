package com.fivehundredtwelve.event.utils;

import com.fivehundredtwelve.event.model.Participant;

import java.util.Set;


/**
 * Created by korshunov on 23.05.15.
 */
public class pWrapper {

    private Set<Participant> participants;

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    public pWrapper(Set<Participant> participants) {
        this.setParticipants(participants);
    }
}
