package org.susu.sa.soc;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public abstract class Post extends PostComment implements Serializable {

    public Post(String sender, String body, Date date, Bitmap bitmap) {
        super(sender, body, date, bitmap);
    }

    public abstract void reply(String body) throws Exception;
    public abstract ArrayList<PostComment> getComments(int count, int body) throws Exception;

}
