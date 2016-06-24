package com.fivehundredtwelve.event.controller;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;
import com.fivehundredtwelve.event.service.SessionService;
import com.fivehundredtwelve.event.service.TaskService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by korshunov on 24.05.15.
 */
@RestController
@RequestMapping(value = "/api/participants", produces = "application/json;charset=UTF-8")
public class ParticipantController {

    private static final Logger logger = Logger.getLogger(EventController.class);
    private static ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
    private static EventService eService = (EventService)context.getBean("eventService");
    private static ParticipantService pService = (ParticipantService)context.getBean("participantService");
    private static TaskService tService = (TaskService)context.getBean("taskService");
    private static SessionService sService = (SessionService)context.getBean("sessionService");

    @RequestMapping(value = "/{participantId}/tasks", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<Task>> showParticipantTasks(@PathVariable final String participantId) {
        try {
            int uId = Integer.parseInt(participantId);
            Participant participant = pService.getParticipantById(uId);
            if (participant == null) {
                throw new Exception("participant not found");
            }
            List<Task> list = new ArrayList<Task>();
            list.addAll(participant.getTasks());
            return new ResponseEntity<List<Task>>(list, HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity<List<Task>>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{participantId}/events", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<Event>> showParticipantEvents(@PathVariable final String participantId) {
        try {
            int pId = Integer.parseInt(participantId);
            Participant participant = pService.getParticipantById(pId);
            if (participant == null) {
                throw new Exception("participant not found");
            }
            List<Event> list = new ArrayList<Event>();
            Set<Event> events = participant.getEvents();
            for (Event e: events){
               e.setTasks(eService.eventTaskOwner(e.getId(),pId));
            }
            list.addAll(events);
            return new ResponseEntity<List<Event>>(list, HttpStatus.OK);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<Event>>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{participantId}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Participant> showParticipant(@PathVariable final String participantId) {
        try {
            int pId = Integer.parseInt(participantId);
            Participant participant = pService.getParticipantById(pId);
            if (participant == null) {
                throw new Exception("participant not found");
            }
            return new ResponseEntity<Participant>(participant, HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity<Participant>(HttpStatus.BAD_REQUEST);
        }
    }


}
