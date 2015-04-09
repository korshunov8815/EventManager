package com.fivehundredtwelve.event.model;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    public Event getTaskEventKeeper() {
        return taskEventKeeper;
    }

    public void setTaskEventKeeper(Event taskEventKeeper) {
        this.taskEventKeeper = taskEventKeeper;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "participant_id")
    public Participant getTaskKeeper() {
        return taskKeeper;
    }

    public void setTaskKeeper(Participant taskKeeper) {
        this.taskKeeper = taskKeeper;
    }

    public Task(String content) {
        this.content = content;
    }

    public Task() {
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}