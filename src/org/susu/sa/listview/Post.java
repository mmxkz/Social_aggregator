package org.susu.sa.listview;

import java.io.Serializable;
import java.util.Date;

/**
 * User: is
 * Date: 1/12/13
 * Time: 7:48 PM
 */
public class Post implements Serializable {

    private String message;
    private String sender;
    private Date date;

    public Post(String sender, String message, Date date) {
        setSender(sender);
        setMessage(message);
        setDate(date);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
