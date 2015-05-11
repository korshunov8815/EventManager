package com.fivehundredtwelve.event.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;

import javax.persistence.*;

/**
 * Created by korshunov on 07.05.15.
 */
@Entity
@Table(name = "session")
public class Session {

    @Id
    @Column(name = "sessionID")
    private String sessionID;
    public String getSessionID() {
        return sessionID;
    }
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }


    public Session(String SessionID) {
        this.sessionID=SessionID;
    }

    public Session(){};

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "participant_id")
    private Participant sessionOwner;
    public Participant getSessionOwner() {
        return sessionOwner;
    }
    public void setSessionOwner(Participant sessionOwner) {
        this.sessionOwner = sessionOwner;
    }

    @Override
    public String toString() {
        return "{" +
                "\"sessionID\":\"" + sessionID + '\"' +
                '}';

    }
}
