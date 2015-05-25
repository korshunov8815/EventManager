package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by anna on 09.04.15.
 */

@Repository
public class TaskDaoImpl implements TaskDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Task saveTask(Task task) {
        em.persist(task);
        return em.find(Task.class, task.getId());
    }

    @Override
    public List<Task> getAllTasks() {
        return em.createQuery("from Task", Task.class).getResultList();
    }

    @Override
    public Task getTaskById(int id) {return em.find(Task.class, id);}

    @Override
    public void deleteTask(int id, ParticipantService ps, EventService es) {
        Task task = em.find(Task.class, id);
        Event event = task.getTaskEventKeeper();
        if (task.getIsTaken()==true) {
            Participant participant = task.getTaskKeeper();
            participant.getTasks().remove(task);
        }
        event.getTasks().remove(task);
        em.flush();
        em.remove(task);
        em.flush();
    }

    @Override
    public void makeDone(int id){
        Task t = em.find(Task.class, id);
        t.setIsDone(true);
        em.flush();
    }

    @Override
    public void makeUndone(int id){
        Task t = em.find(Task.class, id);
        t.setIsDone(false);
        em.flush();
    }

    @Override
    public void makeUntook(int id){
        Task t = em.find(Task.class, id);
        t.setIsTaken(false);
        em.flush();
    }

    @Override
    public Task editTask(int tId, String content, int pId) {
        Task task = em.find(Task.class, tId);
        task.setContent(content);
        Participant participant = em.find(Participant.class, pId);
        task.setTaskKeeper(participant);
        task.setIsTaken(true);
        em.flush();
        return em.find(Task.class, tId);
    }




}
