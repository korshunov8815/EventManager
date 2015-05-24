package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.dao.TaskDao;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by anna on 09.04.15.
 */

@Service("taskService")
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskDao dao;

    @Transactional
    @Override
    public Task saveTask(Task task) {
        return dao.saveTask(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return dao.getAllTasks();
    }

    @Override
    public Task getTaskById(int id) { return dao.getTaskById(id);}

    @Transactional
    @Override
    public void deleteTask(int id, ParticipantService ps, EventService es) { dao.deleteTask(id,ps,es);};

    @Transactional
    @Override
    public void makeDone(int id) { dao.makeDone(id);}

    @Transactional
    @Override
    public Task editTask(int tId, String content, int pId) {return dao.editTask(tId, content,pId);}

}
