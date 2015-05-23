package com.fivehundredtwelve.event.controller;
import com.fivehundredtwelve.event.auth.AuthorizationUtils;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Session;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;
import com.fivehundredtwelve.event.service.SessionService;
import com.fivehundredtwelve.event.service.TaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private EventService eService;
    private static ParticipantService pService = (ParticipantService)context.getBean("participantService");
    private static TaskService tService = (TaskService)context.getBean("taskService");
    private static SessionService sService = (SessionService)context.getBean("sessionService");


    //create an user
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Participant> registerUser(@RequestBody Participant participant) {
        try {
            Participant newParticipant = null;
            if (!pService.ifParticipantExistByEmail(participant.getEmail())) {
                String password = AuthorizationUtils.encodeMD5(participant.getPassword());
                String regId = AuthorizationUtils.encodeMD5(java.util.UUID.randomUUID().toString() + participant.getPassword());
                AuthorizationUtils.registration(participant.getEmail(), regId);
                newParticipant = pService.saveParticipant(new Participant(participant.getEmail(), password, regId));
            }
            return new ResponseEntity<Participant>(newParticipant,HttpStatus.OK);

        }
        catch (final Exception e) {
            return new ResponseEntity<Participant>(HttpStatus.BAD_REQUEST);
        }
    }

    //auth an user
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public Participant authUser(HttpServletResponse response, @RequestBody Participant participant) throws IOException {
        Participant participantDB = null;
        try {
            Session session = new Session();
            if (pService.ifParticipantExistByEmail(participant.getEmail())) {
                //participantDB is the real existing participant. participant - is object for a guy trying to log in
                participantDB = pService.getParticipantByEmail(participant.getEmail());
                String truePass = participantDB.getPassword();
                String checkPass = AuthorizationUtils.encodeMD5(participant.getPassword());
                if (truePass.equals(checkPass) && participantDB.getIsActive()) {
                    String sessionID = String.valueOf(System.currentTimeMillis()) + participant.getEmail();
                    sessionID = AuthorizationUtils.encodeMD5(sessionID);
                    session.setSessionID(sessionID);
                    pService.addSessionToParticipant(session, participantDB);
                    response.addCookie(new Cookie("sessionId", session.getSessionID()));
                }
                else throw new Exception("authorization failed");
            }
        }
        catch (final Exception e) {
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

    @RequestMapping(value = "/auth", method = RequestMethod.DELETE)
    public void logout(HttpServletResponse response) throws IOException {
        response.addCookie(new Cookie("sessionId", ""));
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @RequestMapping(value = "/auth", method = RequestMethod.PUT)
    public ResponseEntity update(HttpServletResponse response, @RequestBody Participant participant) throws IOException {
        try {
            Participant participantBD = pService.getParticipantById(participant.getId());
            if (participantBD==null) throw new Exception("participant not found");
            pService.editParticipantName(participantBD.getId(), participant.getName());
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (final Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/confirm/{regId}", method = RequestMethod.GET)
    public ResponseEntity confirmation(HttpServletResponse response, @PathVariable final String regId) throws IOException {
        try {
            Participant participantBD = pService.getParticipantByregId(regId);
            if (participantBD==null) {
                throw new Exception("participant not found");
            }
            pService.activate(participantBD.getId());
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (final Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

}
