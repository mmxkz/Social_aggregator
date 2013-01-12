package org.susu.sa.soc.vk;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import com.perm.kate.api.*;
import org.json.JSONException;
import org.susu.sa.soc.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class VKSource implements ISource {

    public static final String API_ID = "3354665";

    private long userId;
    private String accessToken;
    private Api api;

    private HashMap<Long, User> cachedUsers = new HashMap<>();

    public VKSource(long userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.api = new Api(this.accessToken, API_ID);
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
        ArrayList<Post> posts = new ArrayList<>();
        ArrayList<Long> uids = new ArrayList<>();

        // Getting messages
        for (WallMessage message : api.getWallMessages(userId, count, offset)) {
            if (!cachedUsers.containsKey(message.from_id))
                uids.add(message.from_id);
            posts.add(new VKPost(this, message));
        }

        // Getting users
        for (User profile: api.getProfiles(uids, null, null, null)) {
            cachedUsers.put(Long.valueOf(profile.uid), profile);
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
       return null;
    }

    protected User getUserById(long id) {
        if (!cachedUsers.containsKey(id)) return null;
        return cachedUsers.get(id);
    }
}
