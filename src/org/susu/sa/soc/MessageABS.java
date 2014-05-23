package org.susu.sa.soc;

/**
 * Created by Andrey on 07.05.14.
 */

import android.graphics.Bitmap;
import com.perm.kate.api.Message;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class MessageABS extends PostMessage implements Serializable {

    public MessageABS(String sender, String body, Long date, Bitmap bitmap, Long cid, Long owner_id) {
        super(sender, body, date, bitmap, cid, owner_id);
    }

    public abstract void reply(String body, Long cid) throws Exception;
    public abstract ArrayList<MessageABS> getDialogs(int count, int body) throws Exception;

}
