package com.fivehundredtwelve.event.controller;
import com.fivehundredtwelve.event.auth.AuthorizationUtils;
import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
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
import java.util.List;

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
    @RequestMapping(method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    public @ResponseBody ResponseEntity<Event> createEvent(HttpServletRequest request, @RequestBody Event event) {
        try {
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                throw new Exception("no session defined");
            }
            event.setEventCreator(participant);
            eService.saveEvent(event);
            return new ResponseEntity<Event>(event, HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
        }
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
    @RequestMapping(value = "/{eventId}", method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    public @ResponseBody ResponseEntity<Event> editEvent(HttpServletRequest request, @PathVariable final String eventId, @RequestBody Event event){
        // @RequestParam(value = "title") final String t, @RequestParam(value = "description") final String d, @RequestParam(value = "id") final String partId
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

        }
        catch (final Exception e) {
            return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Event>(event, HttpStatus.OK);
    }


    @RequestMapping(value = "/{eventId}", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Event> deleteEvent(HttpServletRequest request, @PathVariable final String eventId) {
        Event event = new Event();
        Participant participant = new Participant();
        try {
            int eId = Integer.parseInt(eventId);
            event = eService.getEventById(eId);
            participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                throw new Exception("no session defined");
            }
            if (event != null) {
                if (event.getEventCreator().getId() == participant.getId()) {
                    eService.deleteEvent(eId, pService);
                }
            }
        }
        catch (final Exception e) {
            return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Event>(event, HttpStatus.OK);
    }
}
