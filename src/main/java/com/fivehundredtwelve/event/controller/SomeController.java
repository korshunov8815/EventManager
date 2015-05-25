package com.fivehundredtwelve.event.controller;
import com.fivehundredtwelve.event.auth.AuthorizationUtils;
import com.fivehundredtwelve.event.model.Example;
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
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            } else throw new Exception("such user exists");
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
                    Cookie sessionCookie = new Cookie ("sessionId", session.getSessionID());
                    sessionCookie.setMaxAge(999999999); // oO
                    response.addCookie(sessionCookie);
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
    public void logout(HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            Participant participant = AuthorizationUtils.authorize(request, sService);
            Cookie[] cookie = request.getCookies();
            String currentSessionId = "";
            for (Cookie c : cookie) {
                if (c.getName().equalsIgnoreCase("sessionID")) currentSessionId = c.getValue();
            }
            pService.deleteSession(participant.getId(), currentSessionId);
            response.addCookie(new Cookie("sessionId", ""));
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        catch (final Exception e){
            e.printStackTrace();
        }
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
    public Participant confirmation(HttpServletResponse response, @PathVariable final String regId) throws IOException {
        Participant participantDB = null;
        try {
            participantDB = pService.getParticipantByregId(regId);
            if (participantDB==null) {
                throw new Exception("participant not found");
            }
            pService.activate(participantDB.getId());
            Session session = new Session();
            String sessionID = String.valueOf(System.currentTimeMillis()) + participantDB.getEmail();
            sessionID = AuthorizationUtils.encodeMD5(sessionID);
            session.setSessionID(sessionID);
            pService.addSessionToParticipant(session, participantDB);
            Cookie sessionCookie = new Cookie ("sessionId", session.getSessionID());
            sessionCookie.setPath("/api");
            sessionCookie.setMaxAge(999999999); // oO
            response.addCookie(sessionCookie);
            response.sendRedirect("/");
        }
        catch (final Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return participantDB;
    }

    @RequestMapping(value="/antoshka_zalivaet_fotki", method = RequestMethod.POST)
    public void getDate(@RequestParam Blob b){
        try {
            System.out.println(b.length());
        }
        catch (final Exception e) {}
    }
}
