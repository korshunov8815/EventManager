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
import org.springframework.web.bind.annotation.*;

/**
 * @author anna
 */
@RestController
@RequestMapping
public class EventController {

    private static final Logger logger = Logger.getLogger(EventController.class);
    private static ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
    private static EventService eService = (EventService)context.getBean("eventService");
    private static ParticipantService pService = (ParticipantService)context.getBean("participantService");
    private static TaskService tService = (TaskService)context.getBean("taskService");



    @RequestMapping("/test")
    public void seeEvents() {
        Event event1 = eService.saveEvent(new Event("first", "so good", 1));
        Event event2 = eService.saveEvent(new Event("second", "not so good", 2));
        Event event3 = eService.saveEvent(new Event("third", "boring", 1));
        Participant participant1 = new Participant("vanya","vanya@mail.ru");
        Participant participant2 = new Participant("petya", "petya@mail.ru");
        Participant participant3 = new Participant("anya","anya@mail.ru");
        Task task1 = new Task("buy cake");
        Task task2 = new Task("get musik");
        Task task3 = new Task("inflate baloons");
        eService.addParticipantToEvent(participant1, event1);
        eService.addParticipantToEvent(participant2, event2);
        eService.addParticipantToEvent(participant3, event3);
        eService.addParticipantToEvent(participant1, event3);
        eService.addParticipantToEvent(participant2, event3);
        eService.addTaskToEvent(task1, event1);
        eService.addTaskToEvent(task2, event3);
        eService.addTaskToEvent(task3, event3);
        pService.addTaskToParticipant(task1, participant1);
        pService.addTaskToParticipant(task3, participant1);
        pService.addTaskToParticipant(task2, participant2);
    }


    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public String getAllEvent() {
        return eService.getAllEvents().toString();
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public String createEvent(@RequestParam(value = "title") final String t, @RequestParam(value = "description") final String d, @RequestParam(value = "id") final String id) {
        System.out.println("cought");
        String res = "can't create event, participant with such id doesn't exist";
        try {
            int intId = Integer.parseInt(id);
            if (pService.getParticipantById(intId)!=null) {
                Event event = eService.saveEvent(new Event(t, d, intId));
                res = event.toString();
            }
        }
        catch (NumberFormatException e){}
        return res;
    }
    @RequestMapping(value="/events/{eventId}", method = RequestMethod.GET)
    public String getEventById(@PathVariable final String eventId) {
        String result = "not found";
        try {
            int id = Integer.parseInt(eventId);
            Event event = eService.getEventById(id);
            if (event!=null) result = event.toString();
        }
        catch (NumberFormatException e){}
        return result;
    }
    @RequestMapping(value = "/events/{eventId}", method = RequestMethod.POST)
    public String editEvent(@PathVariable final String eventId, @RequestParam(value = "title") final String t, @RequestParam(value = "description") final String d, @RequestParam(value = "id") final String partId) {
        String res = "no such participant";
        try {
            int eId = Integer.parseInt(eventId);
            int pId = Integer.parseInt(partId);
            Event event = eService.getEventById(eId);
            Participant participant = pService.getParticipantById(pId);
            if (participant!=null) {
                if (event != null) {
                    if (event.getEventCreatorId() == pId) {
                        res = eService.editEvent(eId, t, d).toString();
                    } else {
                        res = "no rights to edit event";
                    }
                } else {
                    res = "event not found";
                }
            }
        }
        catch (NumberFormatException e){}
        return res;
    }
}
