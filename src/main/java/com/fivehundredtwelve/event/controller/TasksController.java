package com.fivehundredtwelve.event.controller;
import com.fivehundredtwelve.event.auth.AuthorizationUtils;
import com.fivehundredtwelve.event.model.Event;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Task;
import com.fivehundredtwelve.event.service.EventService;
import com.fivehundredtwelve.event.service.ParticipantService;
import com.fivehundredtwelve.event.service.SessionService;
import com.fivehundredtwelve.event.service.TaskService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author anna
 */
@RestController
@RequestMapping(value = "/api/tasks", produces = "application/json;charset=UTF-8")
public class TasksController {

    private static ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
    private static EventService eService = (EventService)context.getBean("eventService");
    private static ParticipantService pService = (ParticipantService)context.getBean("participantService");
    private static TaskService tService = (TaskService)context.getBean("taskService");
    private static SessionService sService = (SessionService)context.getBean("sessionService");

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Task> addTask(@RequestBody Task task) {
        try {
            final Event event = eService.getEventById(task.getTaskEventKeeper().getId());
            if (event==null) throw new Exception("no participant in event");
            tService.saveTask(task);
            return new ResponseEntity<Task>(task, HttpStatus.OK);
        }
        catch (final Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value ="{taskId}", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Task> deleteTask(HttpServletRequest request, HttpServletResponse response, @PathVariable String taskId) {
        try {
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                response.sendError(401);
                throw new Exception("no session defined");
            }
            int tId = Integer.parseInt(taskId);
            Task task = tService.getTaskById(tId);
            if (!(participant.getId()==task.getTaskEventKeeper().getEventCreator().getId())) {
                response.sendError(403);
                throw new Exception("you have no rights");
            }
            tService.deleteTask(tId, pService, eService);
            return new ResponseEntity<Task>(HttpStatus.OK);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value ="{taskId}", method = RequestMethod.PATCH)
    public @ResponseBody ResponseEntity<Task> takeTask(HttpServletRequest request, HttpServletResponse response, @PathVariable String taskId) {
        try {
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                response.sendError(401);
                throw new Exception("no session defined");
            }
            int tId = Integer.parseInt(taskId);
            Task task = tService.getTaskById(tId);
            Event event = eService.getEventById(task.getTaskEventKeeper().getId());
            if (!event.getParticipants().contains(participant)) {
                response.sendError(403);
                throw new Exception("you have no rights");
            }
            tService.editTask(tId, task.getContent(), participant.getId());
            task.setIsTaken(true);
            return new ResponseEntity<Task>(task,HttpStatus.OK);
        }
        catch (final Exception e) {
           return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value ="{taskId}", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Task> makeTaskDone(HttpServletRequest request, HttpServletResponse response, @PathVariable String taskId) {
        try {
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                response.sendError(401);
                throw new Exception("no session defined");
            }
            int tId = Integer.parseInt(taskId);
            Task task = tService.getTaskById(tId);
            if (!(participant.getId()==task.getTaskKeeper().getId()) ) {
                response.sendError(403);
                throw new Exception("you have no rights");
            }
            task.setTaskKeeper(participant);
            tService.makeDone(tId);
            task.setIsDone(true);
            return new ResponseEntity<Task>(task, HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value ="{taskId}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Task> editTask(HttpServletRequest request, HttpServletResponse response, @PathVariable String taskId, @RequestBody Task task) {
        try {
            Participant participant = AuthorizationUtils.authorize(request, sService);
            if (participant == null) {
                response.sendError(401);
                throw new Exception("no session defined");
            }
            int tId = Integer.parseInt(taskId);
            int ownerId = tService.getTaskById(tId).getTaskKeeper().getId();
            if (!(participant.getId()==ownerId || participant.getId()==task.getTaskEventKeeper().getEventCreator().getId())) {
                response.sendError(403);
                throw new Exception("you have no rights");
            }
            task = tService.editTask(tId, task.getContent(), task.getTaskKeeper().getId());
            return new ResponseEntity<Task>(task, HttpStatus.OK);
        }
        catch (final Exception e) {
            return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        }
    }



}
