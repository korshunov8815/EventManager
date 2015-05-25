package com.fivehundredtwelve.event.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by korshunov on 25.05.15.
 */
@Entity
public class Example {

    @Id
    @Column(name="datetime")
    private Date date;

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}

    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
