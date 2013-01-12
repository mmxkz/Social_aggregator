package org.susu.sa;

/**
 * User: is
 * Date: 1/12/13
 * Time: 7:48 PM
 */
public class Post {

    private String message;
    private String name;

    public Post(String name, String message) {
        setName(name);
        setMessage(message);
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
}
