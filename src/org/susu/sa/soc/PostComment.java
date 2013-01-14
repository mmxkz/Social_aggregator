package org.susu.sa.soc;

import android.graphics.Bitmap;

import java.util.Date;

public class PostComment {

    private String sender;
    private String body;
    private Date date;
    private Bitmap bitmap;

    public PostComment(String sender, String body, Date date, Bitmap bitmap) {
        this.sender = sender;
        this.body = body;
        this.date = date;
        this.bitmap = bitmap;
    }

    public String getSender() {
        return sender;
    }

    public String getBody() {
        return body;
    }

    public Date getDate() {
        return date;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
