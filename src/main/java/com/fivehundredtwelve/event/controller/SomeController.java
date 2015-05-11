package com.fivehundredtwelve.event.controller;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.*;

/**
 * @author anna
 */
@RestController
@RequestMapping
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
    //assign this to registration page !!!
    @RequestMapping(value = "/registration", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> registerUser(@RequestBody String s) {
        boolean ifSuccessful = false;
        String res = "some error";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode newEventJson = mapper.readTree(s);
            String newUserEmail = newEventJson.get("mail").toString().replaceAll("^\"|\"$", "");
            String password = newEventJson.get("password").toString().replaceAll("^\"|\"$", "");
            password = AuthorizationUtils.encodeMD5(password);
            Participant participant = pService.saveParticipant(new Participant(newUserEmail, password));
            res = participant.toString();
            ifSuccessful = true;
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
        if (ifSuccessful) return new ResponseEntity<String>(res,HttpStatus.OK);
        else return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

    }

    //auth an user
    //assign this to registration page !!!
    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces={"application/json;charset=UTF-8"})
    public @ResponseBody String authUser(@RequestBody String s) {
        String res = "some error";
        boolean isSuccessful = false;
        Session session = new Session();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode newEventJson = mapper.readTree(s);
            String loginEmail = newEventJson.get("mail").toString().replaceAll("^\"|\"$", "");
            String password = newEventJson.get("password").toString().replaceAll("^\"|\"$", "");
            password = AuthorizationUtils.encodeMD5(password);
            if (!pService.ifParticipantExistByEmail(loginEmail)) res = "no such user"; else {
                Participant participant = pService.getParticipantByEmail(loginEmail);
                String truePass = participant.getPassword();
                if (truePass.equals(password))  {
                    String sessionID = String.valueOf(System.currentTimeMillis())+participant.getEmail();
                    sessionID = AuthorizationUtils.encodeMD5(sessionID);
                    session.setSessionID(sessionID);
                    pService.addSessionToParticipant(session, participant);
                    res = session.toString();
                    isSuccessful = true;
                }
                else res = "your password is shit";
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
        return res;

    }
}
