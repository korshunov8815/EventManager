package com.fivehundredtwelve.event.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by anna on 06.04.15.
 */
@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope = Participant.class)
@Table(name = "participant")
public class Participant {

    private int id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private Set<Event> events = new HashSet<Event>();
    private Set<Task> tasks = new HashSet<Task>();
    private Set<Session> sessions = new HashSet<Session>();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

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

    public Participant(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Participant(String email, String password) {
        this.email = email;
        this.name = "unnamed jerk";
        this.password = password;
    }

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
    @JsonManagedReference(value="task-keeper")
    public Set<Task> getTasks() {return tasks;}
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }



    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "sessionOwner")
    @JsonManagedReference(value="session-keeper")
    public Set<Session> getSessions() {
        return sessions;
    }
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
}
