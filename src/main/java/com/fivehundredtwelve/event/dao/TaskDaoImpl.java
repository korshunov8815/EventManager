package com.fivehundredtwelve.event.dao;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
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
    public void addTaskToParticipant(Participant p, Task t) {
        Participant participant = em.find(Participant.class, p.getId());
        if (t.getId() == 0) {
            em.persist(t);
        }
        Task task = em.find(Task.class, t.getId());
        participant.getTasks().add(task);
        task.setTaskKeeper(participant);
    }
}
