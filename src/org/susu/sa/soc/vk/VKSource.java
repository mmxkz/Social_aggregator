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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.util.Log;

import java.util.Collection;
import java.util.Date;

public class VKSource implements ISource {

    public static final String LOG_KEY = "ISource::VKSource";
    public static final String API_ID = "3354665";

    public long userId;
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
                            message.id,
                            nameByUser(user),
                            message.text,
                            new Long(message.date),
                            getUserPictureById(user.uid)
                    )
            );
        }

        return posts;
    }

    public ArrayList<User> getProfiles(Collection<Long> uids, Collection<String> domains, String fields, String name_case) throws MalformedURLException, IOException, JSONException, KException{
        return api.getProfiles(uids, domains, fields, name_case);
    }

    @Override
    public void newPost(String body, String services) throws IOException, JSONException, KException {
        api.createWallPost(userId, body, null, services, false, false, false, null, null, null, null);
    }
    public void setStatus(String status_text) throws IOException, JSONException, KException{
        api.setStatus(status_text,null);
    }
    public VkStatus getStatus() throws IOException, JSONException, KException{
        return api.getStatus(null);

    }
    public void reply(VKPost post, String body, Long cid) throws KException, IOException, JSONException {
        api.createWallComment(null, post.getPostId(), body, cid, null, null);
    }
    public void deleteComment(Long cid) throws KException, IOException, JSONException {
        api.deleteWallComment(null,cid);
    }
    public void deletePost(long Post_Id)throws KException, IOException, JSONException {
        api.removeWallPost(Post_Id, 0);
    }
    public ArrayList<PostComment> getComments(long postId, int count, int offset) throws Exception {
        ArrayList<Comment> comments = api.getWallComments(null, postId, offset, count).comments;
        ArrayList<PostComment> postComments = new ArrayList<PostComment>();

        for (Comment comment : comments) cache.add(comment.from_id);
        cache.update();
        Log.w("qwe","qwe");
        for (Comment comment : comments) {
            User user = cache.get(comment.from_id);
            String name = nameByUser(user);
            Long cid = comment.cid;
            Log.e("reply_to_cid","" + comment.cid);
            postComments.add(new PostComment(name, comment.message,comment.date, null, cid));
        }
        return postComments;
    }

    public Bitmap getUserPictureById(long id) {
        try {
            HttpURLConnection connection = (HttpURLConnection)
                    new URL(cache.get(id).photo).openConnection();
            connection.setDoInput(true);
            connection.connect();
            return BitmapFactory.decodeStream(connection.getInputStream());
        } catch (Exception e) {
            //Log.e(LOG_KEY, "Unable to get bitmap: " + e.getCause().toString());
            e.printStackTrace();
            return null;
        }
    }
    public Bitmap getUserPictureById(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection)
                    new URL(url).openConnection();
            connection.setDoInput(true);
            connection.connect();
            return BitmapFactory.decodeStream(connection.getInputStream());
        } catch (Exception e) {
            //Log.e(LOG_KEY, "Unable to get bitmap: " + e.getCause().toString());
            e.printStackTrace();
            return null;
        }
    }
    public Bitmap getUserImage(long id) {
        return getUserPictureById(id);
    }
    public static String nameByUser(User user) {

        return user.first_name + " " + user.last_name;
    }
    public String getUserName(long id){
        User tmp  = cache.get(id);
//        Log.i("+_+_+_+",tmp.first_name + " " + tmp.last_name);
        return (tmp.first_name + " " + tmp.last_name);
    }
}
