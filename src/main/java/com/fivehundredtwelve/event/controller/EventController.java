package com.fivehundredtwelve.event.controller;
import com.fivehundredtwelve.event.auth.AuthorizationUtils;
import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;
import com.fivehundredtwelve.event.service.SessionService;
import com.fivehundredtwelve.event.service.TaskService;
import com.fivehundredtwelve.event.utils.pWrapper;
import com.fivehundredtwelve.event.utils.tWrapper;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author anna
 */
@RestController

@RequestMapping(value = "/api/events", produces = "application/json;charset=UTF-8")
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
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Event> createEvent(HttpServletRequest request, @RequestBody Event event) {
        try {
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                throw new Exception("no session defined");
            }
            event.setEventCreator(participant);
            eService.saveEvent(event);
            eService.addParticipantToEvent(participant,event);
            return new ResponseEntity<Event>(event, HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
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



    @RequestMapping(value = "/{eventId}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Event> editEvent(HttpServletRequest request, @PathVariable final String eventId, @RequestBody Event event){
        try {
            int eId = event.getId();
            Participant owner = event.getEventCreator();
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                throw new Exception("no session defined");
            }
            if (owner.getId() == participant.getId()) {
                eService.editEvent(event.getId(), event.getTitle(), event.getDescription());
            }
            return new ResponseEntity<Event>(event, HttpStatus.OK);

        }
        catch (final Exception e) {
            return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
        }

    }


    @RequestMapping(value = "/{eventId}", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Event> deleteEvent(HttpServletRequest request, @PathVariable final String eventId) {
       try {
            int eId = Integer.parseInt(eventId);
            Event event = eService.getEventById(eId);
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                throw new Exception("no session defined");
            }
            if (event != null) {
                if (event.getEventCreator().getId() == participant.getId()) {
                    eService.deleteEvent(eId, pService);
                }
            }
            return new ResponseEntity<Event>(event, HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
        }

    }


    @RequestMapping(value = "/{eventId}/participants", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<pWrapper> showParticipants(HttpServletRequest request, HttpServletResponse response, @PathVariable final String eventId) {
       try {
           int eId = Integer.parseInt(eventId);
           Event event = eService.getEventById(eId);
           if (event == null) {
               throw new Exception("event not found");
           }
           pWrapper wrapper = new pWrapper(event.getParticipants());
           return new ResponseEntity<pWrapper>(wrapper, HttpStatus.OK);
       }
       catch (final Exception e) {
            return new ResponseEntity<pWrapper>(HttpStatus.BAD_REQUEST);
       }
    }

    @RequestMapping(value = "/{eventId}/tasks", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<tWrapper> showTasks(HttpServletRequest request, HttpServletResponse response, @PathVariable final String eventId) {
        try {
            int eId = Integer.parseInt(eventId);
            Event event = eService.getEventById(eId);
            if (event == null) {
                throw new Exception("event not found");
            }
            tWrapper wrapper = new tWrapper(event.getTasks());
            return new ResponseEntity<tWrapper>(wrapper, HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity<tWrapper>(HttpStatus.BAD_REQUEST);
        }
    }



}
