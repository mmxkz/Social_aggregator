package org.susu.sa;

import java.io.Serializable;
import java.util.Date;

/**
 * User: is
 * Date: 1/12/13
 * Time: 7:48 PM
 */
public class Post implements Serializable {

    private String message;
    private String name;
    private Date date;

    public Post(String name, String message, Date date) {
        setName(name);
        setMessage(message);
        setDate(date);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
