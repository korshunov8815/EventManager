package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;

import java.util.List;

/**
 * Created by anna on 09.04.15.
 */
public interface TaskService {
    public Task saveTask(Task task);
    public List<Task> getAllTasks();
    public void addTaskToParticipant(Participant participant, Task task);
}
