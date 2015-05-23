package com.fivehundredtwelve.event.utils;

import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;

import java.util.Set;


/**
 * Created by korshunov on 23.05.15.
 */
public class tWrapper {

    private Set<Task> tasks;

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public tWrapper(Set<Task> tasks) {
        this.setTasks(tasks);
    }
}