package com.fivehundredtwelve.event.controller;
import com.fivehundredtwelve.event.auth.AuthorizationUtils;
import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;
import com.fivehundredtwelve.event.service.SessionService;
import com.fivehundredtwelve.event.service.TaskService;
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
import java.util.ArrayList;
import java.util.Date;
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


    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<Event>> getAllEvent() {
        try {
            return new ResponseEntity<List<Event>>(eService.getAllEvents(), HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<List<Event>>(HttpStatus.BAD_REQUEST);
        }
    }

    //Влад, тут мы боремся за утят
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Event> createEvent(HttpServletRequest request, HttpServletResponse response, @RequestBody Event event) {
        try {
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                throw new Exception("no session defined");
            }
            event.setEventCreator(participant);
            Date date = new Date();
            if (event.getDatetime().compareTo(date)<0) {
                response.sendError(405);
                throw new Exception("dude that's too late go get a sleep");
            }
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
    public @ResponseBody ResponseEntity<Event> editEvent(HttpServletRequest request, HttpServletResponse response, @PathVariable final String eventId, @RequestBody Event event){
        try {
            int eId = event.getId();
            Participant owner = event.getEventCreator();
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                throw new Exception("no session defined");
            }
            Date date = new Date();
            if (event.getDatetime().compareTo(date)<0) {
                response.sendError(405);
                throw new Exception("dude that's too late go get a sleep");
            }
            if (owner.getId() == participant.getId()) {
                eService.editEvent(event.getId(), event.getTitle(), event.getDescription(), event.getDatetime());
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

    @RequestMapping(value = "/{eventId}", method = RequestMethod.PATCH)
    public @ResponseBody ResponseEntity takePart(HttpServletRequest request, HttpServletResponse response, @PathVariable final String eventId) {
        try {
            int eId = Integer.parseInt(eventId);
            Event event = eService.getEventById(eId);
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                response.sendError(401);
                throw new Exception("no session defined");
            }
            if (event != null) {
                eService.addParticipantToEvent(participant,event);
            }
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }



    @RequestMapping(value = "/{eventId}/participants", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<Participant>> showEventParticipants(HttpServletRequest request, HttpServletResponse response, @PathVariable final String eventId) {
       try {
           int eId = Integer.parseInt(eventId);
           Event event = eService.getEventById(eId);
           if (event == null) {
               throw new Exception("event not found");
           }
           List<Participant> list = new ArrayList<Participant>();
           list.addAll(event.getParticipants());
           return new ResponseEntity<List<Participant>>(list, HttpStatus.OK);
       }
       catch (final Exception e) {
            return new ResponseEntity<List<Participant>>(HttpStatus.BAD_REQUEST);
       }
    }

    @RequestMapping(value = "/{eventId}/tasks", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<Task>> showEventTasks(HttpServletRequest request, HttpServletResponse response, @PathVariable final String eventId) {
        try {
            int eId = Integer.parseInt(eventId);
            Event event = eService.getEventById(eId);
            if (event == null) {
                throw new Exception("event not found");
            }
            List<Task> list = new ArrayList<Task>();
            list.addAll(event.getTasks());
            return new ResponseEntity<List<Task>>(list, HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity<List<Task>>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{eventId}/participants/{participantId}", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity deleteParticipant(HttpServletRequest request, HttpServletResponse response, @PathVariable final String eventId, @PathVariable final String participantId) {
        try {
            int eId = Integer.parseInt(eventId);
            Event event = eService.getEventById(eId);
            int pId = Integer.parseInt(participantId);
            Participant participant = pService.getParticipantById(pId);
            Participant owner = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                response.sendError(401);
                throw new Exception("no session defined");
            }
            if (event == null) {
                throw new Exception("event not found");
            }
            if (owner.getId()!=event.getEventCreator().getId()) {
                response.sendError(403);
                throw new Exception("you have no rights");
            }
            eService.deleteParticipantFromEvent(eId, pId, pService,tService);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{eventId}/leave", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity leaveEvent(HttpServletRequest request, HttpServletResponse response, @PathVariable final String eventId) {
        try {
            int eId = Integer.parseInt(eventId);
            Event event = eService.getEventById(eId);
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                response.sendError(401);
                throw new Exception("no session defined");
            }
            if (event == null) {
                throw new Exception("event not found");
            }
            eService.deleteParticipantFromEvent(eId,participant.getId(),pService, tService);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }





}
