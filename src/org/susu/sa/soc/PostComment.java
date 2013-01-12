package org.susu.sa.soc;

import android.graphics.Bitmap;

import java.util.Date;

public abstract class PostComment {

    public abstract String getSender();
    public abstract String getBody();
    public abstract Date getDate();
    public abstract Bitmap getBitmap();

}
