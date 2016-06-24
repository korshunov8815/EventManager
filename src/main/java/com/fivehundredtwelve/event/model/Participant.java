package com.fivehundredtwelve.event.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by anna on 06.04.15.
 */
@Entity
@Table(name = "participant")
public class Participant {

    private int id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private Set<Event> events = new HashSet<Event>();
    private Set<Task> tasks = new HashSet<Task>();
    @JsonIgnore
    private Set<Session> sessions = new HashSet<Session>();
    @JsonIgnore
    private String regId;
    @JsonIgnore
    private Boolean isActive;

    @Column(name="regId", nullable = false)
    @JsonIgnore
    public String getRegId() { return regId;}
    @JsonProperty
    public void setRegId(String regId) { this.regId = regId;}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Column(name="isActive", nullable =false)
    @JsonIgnore
    public Boolean getIsActive() {return isActive;}
    @JsonProperty
    public void setIsActive(Boolean isActive) {this.isActive = isActive;}

    @Column(name = "name",length = 100, nullable = false)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "email",length = 500, nullable = true)
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="password", nullable = true)
    @JsonIgnore
    public String getPassword() {return password;}
    @JsonProperty
    public void setPassword(String password) { this.password=password;}


    public Participant() {
    }


    public Participant(String email, String password, String regId) {
        this.email = email;
        this.name = "unnamed jerk";
        this.password = password;
        this.regId = regId;
        this.isActive = false;

    }

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "participant_event",
            joinColumns = {@JoinColumn(name = "participant_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    public Set<Event> getEvents() {
        return events;
    }
    public void setEvents(Set<Event> events) {
        this.events = events;
    }


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "taskKeeper")
    @JsonManagedReference("task-participant")
    public Set<Task> getTasks() {return tasks;}
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }



    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "sessionOwner")
    @JsonManagedReference(value="session-keeper")
    @JsonIgnore
    public Set<Session> getSessions() {
        return sessions;
    }
    @JsonProperty
    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }




    @Override
    public String toString() {
        return '{' +
                "\"id\":" + id +
                ", \"name\":\"" + name + '\"' +
                ", \"email\":\"" + email + '\"' +
                ", \"password\":\""+password +'\"' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        if (id != that.id) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }
}