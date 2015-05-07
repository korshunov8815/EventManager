package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.model.Session;
import com.fivehundredtwelve.event.model.Task;

import java.util.List;

/**
 * Created by korshunov on 07.05.15.
 */
public interface SessionService {
    public Session saveSession(Session session);
    public List<Session> getAllSessions();
}
