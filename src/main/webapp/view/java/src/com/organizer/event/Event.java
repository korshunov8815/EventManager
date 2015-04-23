package com.organizer.event;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aina
 */
public class Event {

    private final String name;

    private final String description;

    private final List<Participant> participants;

    private final List<Message> chat;

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
        this.participants = new ArrayList<>();
        this.chat = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public List<Message> getChat() {
        return chat;
    }
}
