package org.susu.sa.soc.vk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.perm.kate.api.User;
import com.perm.kate.api.WallMessage;
import org.susu.sa.soc.Post;
import org.susu.sa.soc.PostComment;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class VKPost extends Post {

    public final static String LOG_KEY = "PostComment::VKёPost";

    private final VKSource source;
    private User user;
    private Bitmap bitmap;
    private WallMessage message;
    private Date date;

    public VKPost(VKSource source, WallMessage message) {
        this.message = message;
        this.source = source;
        this.date = new Date(message.date);
    }

    @Override
    public String getSender() {
        return user.photo + " " + user.last_name;
    }

    @Override
    public String getBody() {
        return message.text;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Bitmap getBitmap() {
        if (bitmap == null) {
            try {
                HttpURLConnection connection = (HttpURLConnection)
                        new URL(user.photo).openConnection();
                connection.setDoInput(true);
                connection.connect();
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            } catch (Exception e) {
                Log.e(LOG_KEY, "Unable to get bitmap: " + e.getCause().toString());
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    public void reply(String body) throws Exception {
        getSource().reply(this, body);
    }

    @Override
    public ArrayList<PostComment> getComments(int count, int offset) throws Exception {
        return getSource().getComments(this, count, offset);
    }

    public VKSource getSource() {
        return source;
    }

    public long getPostId() {
        return message.id;
    }
}
