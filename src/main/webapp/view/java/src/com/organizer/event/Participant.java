package com.organizer.event;

/**
 * Created by aina on 10/4/14.
 */
public class Participant {

    private final int id;

    private final String mail;

    private final String name;

    public String getName() {
        return name;
    }

    public Participant(int id, String mail, String name, String password) {

        this.id = id;
        this.mail = mail;
        this.name = name;
        this.password = password;
    }

    private final String password;

}
