package com.fivehundredtwelve.event.controller;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;
import com.fivehundredtwelve.event.service.SessionService;
import com.fivehundredtwelve.event.service.TaskService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;
import java.io.*;

/**
 * @author anna
 */
@RestController
@RequestMapping(value = "/events", produces = "text/plain;charset=UTF-8")
public class EventController {

    private static final Logger logger = Logger.getLogger(EventController.class);
    private static ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
    private static EventService eService = (EventService)context.getBean("eventService");
    private static ParticipantService pService = (ParticipantService)context.getBean("participantService");
    private static TaskService tService = (TaskService)context.getBean("taskService");
    private static SessionService sService = (SessionService)context.getBean("sessionService");

    @RequestMapping(method = RequestMethod.GET)
    public String getAllEvent() {
        logger.info("/events");
        return eService.getAllEvents().toString();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createEvent(@RequestBody String s) {
        String res = "can't create event, participant with such id doesn't exist";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode newEventJson = mapper.readTree(s);
            String newEventTitle = newEventJson.get("title").toString().replaceAll("^\"|\"$", "");;
            String newEventDescr = newEventJson.get("description").toString().replaceAll("^\"|\"$", "");;
            int newEventuserID = Integer.parseInt(newEventJson.get("userId").toString().replaceAll("^\"|\"$", ""));
            if (pService.getParticipantById(newEventuserID)!=null) {
                Event event = eService.saveEvent(new Event(newEventTitle, newEventDescr, newEventuserID));
                res = event.toString();
            }
        }
        catch (NumberFormatException e){
            res = "NumberFormatException (probably userId is not a number)";
        }
        catch (JsonParseException e) {
            res = "Json parse error";
        }
        catch (IOException e) {
            res = "IOException";
        }
        System.out.println(res);
        //баги - если вызвать NumberFormatException, а потом попробовать нормально доабвить ивент
        //то какого-то хрена эксепшн будет включен в json, но на работу это не влияет
        return res;
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
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
