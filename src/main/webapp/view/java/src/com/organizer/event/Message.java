package com.organizer.event;

/**
 * Created by aina on 10/4/14.
 */
public class Message {

    private final String name;

    private final String content;

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public Message(String name, String content, int message_id) {

        this.name = name;
        this.content = content;
    }
}
