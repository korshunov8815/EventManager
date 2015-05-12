package com.fivehundredtwelve.event.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by anna on 06.04.15.
 */
@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "event")
public class Event {

    private int id;
    private String title;
    private String description;
    private Set<Task> tasks;
    private int eventCreatorId;

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

    @Column(name = "title",length = 100, nullable = false)
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description",length = 500, nullable = true)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "eventCreatorId", nullable = false)
    public int getEventCreatorId() {
        return eventCreatorId;
    }
    public void setEventCreatorId(int eventCreatorId) {
        this.eventCreatorId = eventCreatorId;
    }

    public Event(String title, String description, int id) {
        this.title = title;
        this.description = description;
        this.eventCreatorId = id;
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

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER, mappedBy = "taskEventKeeper")
    @JsonManagedReference(value="task-EventKeeper")
    @OnDelete(action= OnDeleteAction.CASCADE)
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
