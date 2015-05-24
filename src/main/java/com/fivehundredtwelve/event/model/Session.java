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
    @JsonBackReference(value="session-keeper")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (sessionID != null ? !sessionID.equals(session.sessionID) : session.sessionID != null) return false;
        if (sessionOwner != null ? !sessionOwner.equals(session.sessionOwner) : session.sessionOwner != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sessionID != null ? sessionID.hashCode() : 0;
        result = 31 * result + (sessionOwner != null ? sessionOwner.hashCode() : 0);
        return result;
    }
}
