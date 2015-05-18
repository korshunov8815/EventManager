package com.fivehundredtwelve.event.controller;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author anna
 */
@RestController
@RequestMapping(value = "/api", produces = "application/json;charset=UTF-8")
public class SomeController {

    private static final Logger logger = Logger.getLogger(SomeController.class);
    private static ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
    private static EventService eService = (EventService)context.getBean("eventService");
    private static ParticipantService pService = (ParticipantService)context.getBean("participantService");
    private static TaskService tService = (TaskService)context.getBean("taskService");
    private static SessionService sService = (SessionService)context.getBean("sessionService");

    @RequestMapping("/test")
    public void seeEvents() {
        Event event1 = eService.saveEvent(new Event("first", "so good рашан", 1));
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

    //create an user
    @RequestMapping(value = "/registration", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<Participant> registerUser(@RequestBody Participant participant) {
        boolean ifSuccessful = false;
        Participant newParticipant = new Participant();
        if (!pService.ifParticipantExistByEmail(participant.getEmail())) {
            String password = AuthorizationUtils.encodeMD5(participant.getPassword());
            newParticipant = pService.saveParticipant(new Participant(participant.getEmail(), password));
            ifSuccessful = true;
        }
        if (ifSuccessful) return new ResponseEntity<Participant>(newParticipant,HttpStatus.OK);
        else return new ResponseEntity<Participant>(HttpStatus.BAD_REQUEST);
    }

    //auth an user
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public Participant authUser(HttpServletResponse response, @RequestBody Participant participant) throws IOException {

        boolean isSuccessful = false;
        Session session = new Session();
        Participant participantDB = null;
        if (pService.ifParticipantExistByEmail(participant.getEmail())) {
            //participantDB is the real existing participant. participant - is object for a guy trying to log in
            participantDB = pService.getParticipantByEmail(participant.getEmail());
            String truePass = participantDB.getPassword();
            String checkPass = AuthorizationUtils.encodeMD5(participant.getPassword());
            if (truePass.equals(checkPass)) {
                String sessionID = String.valueOf(System.currentTimeMillis()) + participant.getEmail();
                sessionID = AuthorizationUtils.encodeMD5(sessionID);
                session.setSessionID(sessionID);
                pService.addSessionToParticipant(session, participantDB);
                isSuccessful = true;
            }
        }
        response.addCookie(new Cookie("sessionId", session.getSessionID()));
        if (!isSuccessful) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return participantDB;
    }


    //ifAuth
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public @ResponseBody Participant checkIfAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Participant participant = AuthorizationUtils.authorize(request, sService);
        if (participant == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return participant;
    }

}
