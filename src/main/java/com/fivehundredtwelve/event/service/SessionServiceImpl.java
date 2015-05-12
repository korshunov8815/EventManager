package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.dao.SessionDao;
import com.fivehundredtwelve.event.dao.TaskDao;
import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.model.Session;
import com.fivehundredtwelve.event.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by korshunov on 07.05.15.
 */
@Service("sessionService")
public class SessionServiceImpl implements SessionService {
    @Autowired
    private SessionDao dao;

    @Override
    @Transactional
    public Session saveSession(Session session) {
        return dao.saveSession(session);
    }

    @Override
    public List<Session> getAllSessions() {
        return dao.getAllSessions();
    }

    @Override
    public boolean ifSessionExist(String sessionId) {
        return dao.ifSessionExist(sessionId);
    }

    @Override
    public Participant getParticipantBySession(String sessionId) {return dao.getParticipantBySession(sessionId);}


}
