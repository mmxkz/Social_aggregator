package org.susu.sa.soc;

import android.graphics.Bitmap;

import java.util.Date;

public class PostComment {

    private String sender;
    private String body;
    private Long date;
    private Bitmap bitmap;
    private Long cid;

    public PostComment(String sender, String body, Long date, Bitmap bitmap, Long cid) {
        this.sender = sender;
        this.body = body;
        this.date = date;
        this.bitmap = bitmap;
        this.cid = cid;
    }

    public String getSender() {
        return sender;
    }

    public String getBody() {
        return body;
    }

    public Long getDateMsg() {
        return date;
    }
     public Long getCid(){
         return cid;
     }


    public Bitmap getBitmap() {
        return bitmap;
    }
}
