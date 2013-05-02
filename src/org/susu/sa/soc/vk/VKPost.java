package org.susu.sa.soc.vk;

import android.graphics.Bitmap;
import org.susu.sa.soc.Post;
import org.susu.sa.soc.PostComment;

import java.util.ArrayList;
import java.util.Date;

public class VKPost extends Post {

    public final static String LOG_KEY = "PostComment::VKPost";

    private final VKSource source;
    private long id;

    public VKPost(VKSource source, long id, String sender, String message, Long date, Bitmap bitmap) {
        super(sender, message, date, bitmap, null);
        this.source = source;
        this.id = id;
    }

    @Override
    public void reply(String body, Long cid) throws Exception {
        getSource().reply(this, body, cid);
    }

    @Override
    public ArrayList<PostComment> getComments(int count, int offset) throws Exception {
        return getSource().getComments(getPostId(), count, offset);
    }

    public VKSource getSource() {
        return source;
    }

    public long getPostId() {
        return id;
    }

    public String toString() {
        return Long.toString(id);
    }
}
