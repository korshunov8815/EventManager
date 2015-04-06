package com.fivehundredtwelve.event.controller;

import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.service.EventService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author anna
 */
@RestController
@RequestMapping
public class EventController {

    private static final Logger logger = Logger.getLogger(EventController.class);
    @RequestMapping("/events")
    public String hw() {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
        EventService service = (EventService)context.getBean("storageService");
        service.saveEvent(getEvent());
        StringBuilder sb = new StringBuilder();
        logger.info("list of events");
        for (Event event: service.getAllEvents()) {
            sb.append(event.toString()+"\n");
            logger.info(event);
        }

        return sb.toString();
    }



    public static Event getEvent() {
        Event event = new Event();
        event.setTitle("first book");
        event.setDescription("so good");
        return event;
    }
}