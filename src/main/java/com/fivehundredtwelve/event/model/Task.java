package com.fivehundredtwelve.event.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
    private Boolean isTaken;



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

    @Column(name = "isTaken", nullable = false)
    public Boolean getIsTaken() {return isTaken;}
    public void setIsTaken(Boolean isTaken) {this.isTaken = isTaken;}


    @ManyToOne
    @JsonBackReference("task-event")
    @JoinColumn(name = "event_id")
    public Event getTaskEventKeeper() {
        return taskEventKeeper;
    }
    public void setTaskEventKeeper(Event taskEventKeeper) {
        this.taskEventKeeper = taskEventKeeper;
    }

    @ManyToOne
    @JsonBackReference("task-participant")
    @JoinColumn(name = "participant_id")
    public Participant getTaskKeeper() {
        return taskKeeper;
    }
    public void setTaskKeeper(Participant taskKeeper) {
        this.taskKeeper = taskKeeper;
    }

    public Task(String content) {
        this.isDone = false;
        this.content = content;
        this.isTaken = false;
    }

    public Task(String content, Event taskEventKeeper) {
        this.content=content;
        this.taskEventKeeper=taskEventKeeper;
        this.isDone=false;
        this.isTaken=false;
    }


    public Task() {
        this.isDone=false;
        this.isTaken=false;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"content\":\"" + content + '\"' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (content != null ? !content.equals(task.content) : task.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}