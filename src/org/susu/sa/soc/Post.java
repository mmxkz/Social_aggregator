package org.susu.sa.soc;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Post extends PostComment implements Serializable {

    public Post(String sender, String body, Long date, Bitmap bitmap, Long cid, Long owner_id) {
        super(sender, body, date, bitmap, cid, owner_id);
    }

    public abstract void reply(String body, Long cid) throws Exception;
    public abstract ArrayList<PostComment> getComments(int count, int body) throws Exception;

}
