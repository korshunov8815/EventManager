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
        Event event1 = eService.saveEvent(new Event("first book", "so good"));

        eService.addParticipantToEvent(event1, new Participant("vanya","vanya@mail.ru"));
        eService.addParticipantToEvent(event1, new Participant("petya","petya@mail.ru"));
        eService.addParticipantToEvent(event1, new Participant("anya","anya@mail.ru"));

        StringBuilder sb = new StringBuilder();
        logger.info("list of events and participants");
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

}