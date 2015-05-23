package com.fivehundredtwelve.event.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

/**
 * Created by anna on 09.04.15.
 */
@Entity
@Table(name = "task")
public class Task {
    private int id;
    private String content;
    private Participant taskKeeper;
    private Event taskEventKeeper;
    private Boolean isDone;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Column(name = "content", length = 500, nullable = false)
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }


    @Column(name = "isDone", nullable = false)
    public Boolean getIsDone() { return isDone; }
    public void setIsDone(Boolean isDone) { this.isDone = isDone; }


    @ManyToOne
    @JoinColumn(name = "event_id")
    public Event getTaskEventKeeper() {
        return taskEventKeeper;
    }

    public void setTaskEventKeeper(Event taskEventKeeper) {
        this.taskEventKeeper = taskEventKeeper;
    }

    @ManyToOne
    @JoinColumn(name = "participant_id")
    public Participant getTaskKeeper() {
        return taskKeeper;
    }

    public void setTaskKeeper(Participant taskKeeper) {
        this.taskKeeper = taskKeeper;
    }

    public Task(String content) {
        isDone = false;
        this.content = content;
    }

    public Task(String content, Participant taskKeeper, Event taskEventKeeper) {
        this.content=content;
        this.taskEventKeeper=taskEventKeeper;
        this.taskKeeper=taskKeeper;
        this.isDone=false;
    }

    public Task() {
        isDone=false;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"content\":\"" + content + '\"' +
                '}';
    }
}