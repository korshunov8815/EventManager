package com.fivehundredtwelve.event.controller;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;
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
        Participant participant1 = getParticipant();
        Event event1 = getEvent();
        HashSet<Participant> participants = new HashSet<Participant>();
        participants.add(participant1);


        //TODO relations :(
        event1.setParticipants(participants);
        HashSet<Event> events = new HashSet<Event>();
        events.add(event1);
        participant1.setEvents(events);


        //eService.saveEvent(event1);
        pService.saveParticipant(participant1);
        StringBuilder sb = new StringBuilder();
        logger.info("list of events");
        for (Event event: eService.getAllEvents()) {
            sb.append(event.toString()+"\n");
            logger.info(event);
        }
        for (Participant participant : pService.getAllParticipants()) {
            logger.info(participant);
            sb.append(participant.toString()+"\n");
        }

        return sb.toString();
    }

    public static Event getEvent() {
        Event event = new Event();
        event.setTitle("first book");
        event.setDescription("so good");
        return event;
    }

    public static Participant getParticipant() {
        Participant participant = new Participant();
        participant.setName("vanya");
        participant.setEmail("vanya@mail.ru");
        return participant;
    }

}