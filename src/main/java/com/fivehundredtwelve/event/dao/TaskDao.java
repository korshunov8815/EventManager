package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;

import java.util.List;

/**
 * Created by anna on 09.04.15.
 */
public interface TaskDao {
    public Task saveTask(Task task);
    public List<Task> getAllTasks();
    public Task getTaskById(int id);
    public void deleteTask(int id, ParticipantService ps, EventService es);
    public void makeDone(int id);
    public Task editTask(int tId, String content, int pId);
    public void makeUndone(int id);
    public void makeUntook(int id);
}
