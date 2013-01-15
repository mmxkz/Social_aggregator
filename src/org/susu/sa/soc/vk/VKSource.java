package org.susu.sa.soc.vk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import com.perm.kate.api.*;
import org.json.JSONException;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.Post;
import org.susu.sa.soc.PostComment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class VKSource implements ISource {

    public static final String LOG_KEY = "ISource::VKSource";
    public static final String API_ID = "3354665";

    private long userId;
    private String accessToken;
    private Api api;

    private VKUserCache cache;

    public VKSource(long userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.api = new Api(this.accessToken, API_ID);
        cache = new VKUserCache(api);
    }

    public static void callWebView(Context context, WebView webView, VKWebViewClient client) {
        // Set settings
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(client);

        // Control cookies
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        // Load
        webView.loadUrl(Auth.getUrl(API_ID, Auth.getSettings()));
    }

    @Override
    public ArrayList<Post> getPosts(int count, int offset) throws Exception {
        ArrayList<Post> posts = new ArrayList<Post>();
        // Getting messages
        ArrayList<WallMessage> wallMessages = api.getWallMessages(userId, count, offset);
        for (WallMessage message : wallMessages) cache.add(message.from_id);
        cache.update();

        // Settings messages
        for (WallMessage message : wallMessages) {
            User user = cache.get(message.from_id);
            posts.add(
                    new VKPost(this,
                            message.id, nameByUser(user),
                            message.text,
                            new Date(message.date),
                            getUserPictureById(user.uid)
                    )
            );
        }

        return posts;
    }

    @Override
    public void newPost(String body) throws Exception {
        api.createWallPost(userId, body, null, null, false, false, false, null, null, null, null);
    }

    protected void reply(VKPost post, String body) throws KException, IOException, JSONException {
        api.createWallComment(null, post.getPostId(), body, null, null, null);
    }

    protected ArrayList<PostComment> getComments(VKPost post, int count, int offset) throws Exception {
        ArrayList<Comment> comments = api.getWallComments(null, post.getPostId(), offset, count).comments;
        ArrayList<PostComment> postComments = new ArrayList<PostComment>();

        for (Comment comment : comments) cache.add(comment.from_id);
        cache.update();

        for (Comment comment : comments) {
            User user = cache.get(comment.from_id);
            postComments.add(
                    new PostComment(
                            nameByUser(user),
                            comment.message,
                            new Date(comment.date),
                            getUserPictureById(user.uid)
                    )
            );
        }

        return postComments;
    }

    protected Bitmap getUserPictureById(long id) {
        try {
            HttpURLConnection connection = (HttpURLConnection)
                    new URL(cache.get(id).photo).openConnection();
            connection.setDoInput(true);
            connection.connect();
            return BitmapFactory.decodeStream(connection.getInputStream());
        } catch (Exception e) {
            Log.e(LOG_KEY, "Unable to get bitmap: " + e.getCause().toString());
            e.printStackTrace();
            return null;
        }
    }

    private static String nameByUser(User user) {
        return user.first_name + " " + user.last_name;
    }
}
