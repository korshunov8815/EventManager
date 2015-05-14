package com.fivehundredtwelve.event.controller;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fivehundredtwelve.event.auth.AuthorizationUtils;
import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Session;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Set;

/**
 * @author anna
 */
@RestController
@RequestMapping(value = "/api/events", produces = "text/plain;charset=UTF-8")
public class EventController {

    private static final Logger logger = Logger.getLogger(EventController.class);
    private static ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
    private static EventService eService = (EventService)context.getBean("eventService");
    private static ParticipantService pService = (ParticipantService)context.getBean("participantService");
    private static TaskService tService = (TaskService)context.getBean("taskService");
    private static SessionService sService = (SessionService)context.getBean("sessionService");

    @RequestMapping(method = RequestMethod.GET, produces={"application/json;charset=UTF-8"})
    public @ResponseBody
    List<Event> getAllEvent() {
        logger.info("/events");
        return eService.getAllEvents();
    }

    //Влад, тут мы боремся за утят
    @RequestMapping(method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    public @ResponseBody ResponseEntity<Event> createEvent(HttpServletRequest request, @RequestBody Event event) {
        boolean isSuccessful=false;
        Event newEvent = new Event();
        Cookie[] cookie = request.getCookies();
        String currentSessionId="";
        Participant participant = new Participant();
        for (Cookie c : cookie) {
            if (c.getName().equalsIgnoreCase("sessionID")) currentSessionId = c.getValue();
        }
        if (sService.ifSessionExist(currentSessionId)) {
            participant = sService.getParticipantBySession(currentSessionId);
        }
        try {
                if (pService.getParticipantById(participant.getId())!=null) {
                newEvent = eService.saveEvent(new Event(event.getTitle(),event.getDescription(),participant.getId()));
                isSuccessful=true;
                }

        }
        catch (NumberFormatException e){}
        if (isSuccessful) return new ResponseEntity<Event>(newEvent,HttpStatus.OK);
        else return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET, produces={"application/json;charset=UTF-8"})
    public @ResponseBody ResponseEntity<Event> getEventById(@PathVariable final String eventId) {
        boolean isSuccessful = false;
        Event event = new Event();
        try {
            int id = Integer.parseInt(eventId);
            event = eService.getEventById(id);
            if (event!=null) isSuccessful=true;
        }
        catch (NumberFormatException e){}
        if (isSuccessful) return new ResponseEntity<Event>(event,HttpStatus.OK);
        else return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
    }


    //not done yet
    @RequestMapping(value = "/{eventId}", method = RequestMethod.POST)
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


    //not done yet
    @RequestMapping(value = "/{eventId}", method = RequestMethod.DELETE)
    public String deleteEvent(@PathVariable final String eventId, @RequestParam(value = "id") final String partId) {
        String res = "no such participant";
        try {
            int eId = Integer.parseInt(eventId);
            int pId = Integer.parseInt(partId);
            Event event = eService.getEventById(eId);
            Participant participant = pService.getParticipantById(pId);
            if (participant!=null) {
                if (event != null) {
                    if (event.getEventCreatorId() == pId) {
                        res = eService.deleteEvent(eId, pService).toString();
                    } else {
                        res = "no rights to delete event";
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
