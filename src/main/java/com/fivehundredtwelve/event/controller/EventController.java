package com.fivehundredtwelve.event.controller;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;
import com.fivehundredtwelve.event.service.TaskService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

/**
 * @author anna
 */
@RestController
@RequestMapping
public class EventController {

    private static final Logger logger = Logger.getLogger(EventController.class);

    @RequestMapping("/events")
    public String seeEvents() {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
        EventService eService = (EventService)context.getBean("eventService");
        ParticipantService pService = (ParticipantService)context.getBean("participantService");
        TaskService tService = (TaskService)context.getBean("taskService");
        Event event1 = eService.saveEvent(new Event("first", "so good"));
        Event event2 = eService.saveEvent(new Event("second", "not so good"));

        Participant participant1 = new Participant("vanya","vanya@mail.ru");
        eService.addParticipantToEvent(event1, participant1);
        eService.addParticipantToEvent(event1, new Participant("petya", "petya@mail.ru"));
        eService.addParticipantToEvent(event1, new Participant("anya","anya@mail.ru"));
        eService.addParticipantToEvent(event2,participant1);
        Task task1 = new Task("buy cake");
        Task task2 = new Task("get musik");
        eService.addTaskToEvent(task1, event1);
        eService.addTaskToEvent(task2, event1);
        pService.addTaskToParticipant(participant1, task1);
        pService.addTaskToParticipant(participant1, task2);

        StringBuilder sb = new StringBuilder();
        logger.info("list of events and participants");
        for (Event event: eService.getAllEvents()) {
            logger.info(event);
        }
        for (Participant participant : pService.getAllParticipants()) {
            logger.info(participant);
        }
        for (Task task : tService.getAllTasks()) {
            logger.info(task);
        }
        return sb.append(eService.getAllEvents()).append(pService.getAllParticipants()).append(tService.getAllTasks()).toString();
    }

}