package com.fivehundredtwelve.event.service;

import com.fivehundredtwelve.event.dao.EventDao;
import com.fivehundredtwelve.event.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by anna on 06.04.15.
 */
@Service("storageService")
public class EventServiceImpl implements EventService{

    @Autowired
    private EventDao dao;

    @Transactional
    @Override
    public void saveEvent(Event event) {
        dao.saveEvent(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return dao.getAllEvents();
    }
}
