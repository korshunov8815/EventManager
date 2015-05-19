package com.fivehundredtwelve.event.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by anna on 06.04.15.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Event.class)
@Table(name = "event")
public class Event {

    private int id;
    private String title;
    private String description;
    private Set<Task> tasks;
    private Participant eventCreator;

    private Set<Participant> participants = new HashSet<Participant>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "title", length = 100, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", length = 500, nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public Participant getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(Participant eventCreator) {
        this.eventCreator = eventCreator;
    }

    public Event(String title, String description, Participant eventCreator) {
        this.title = title;
        this.description = description;
        this.eventCreator = eventCreator;
    }

    public Event() {
    }

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "events")

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taskEventKeeper")
    @JsonManagedReference(value = "task-EventKeeper")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }


    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"title\":\"" + title + '\"' +
                ", \"description\":\"" + description + '\"' +
                ", \"tasks\":" + tasks +
                '}';
    }

}
