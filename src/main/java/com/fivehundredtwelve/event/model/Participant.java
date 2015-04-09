package com.fivehundredtwelve.event.model;

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
    private Set<Event> events = new HashSet<Event>();
    private Set<Task> tasks = new HashSet<Task>();

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

    public Participant() {
    }

    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Participant(String email) {
        this.email = email;
        this.name = "no name";
    }

    @ManyToMany(cascade = {CascadeType.ALL})
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
    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
    @Override
    public String toString() {
        return '{' +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
