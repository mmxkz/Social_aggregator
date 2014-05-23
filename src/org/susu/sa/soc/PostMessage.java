package org.susu.sa.soc;

import android.graphics.Bitmap;

/**
 * Created by Andrey on 06.05.14.
 */
public class PostMessage{
    private String sender;
    private String body;
    private Long date;
    private Bitmap bitmap;
    private Long cid;
    private Long owner_id;

    public PostMessage(String sender, String body, Long date, Bitmap bitmap, Long cid, Long owner_id) {
        this.sender = sender;
        this.body = body;
        this.date = date;
        this.bitmap = bitmap;
        this.cid = cid;
        this.owner_id = owner_id;
    }

    public String getSender() { return sender; }
    public String getBody() { return body; }
    public Long getDateMsg() { return date; }
    public Long getCid(){ return cid; }
    public Long getOwner() {return owner_id; }
    public Bitmap getBitmap() { return bitmap; }
}
