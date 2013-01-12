package org.susu.sa.soc.vk;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import com.perm.kate.api.Api;
import com.perm.kate.api.Auth;
import com.perm.kate.api.KException;
import com.perm.kate.api.WallMessage;
import org.json.JSONException;
import org.susu.sa.soc.IPost;
import org.susu.sa.soc.ISource;

import java.io.IOException;
import java.util.ArrayList;

public class VKSource implements ISource {

    public static final String API_ID = "3205931";

    private long userId;
    private String accessToken;
    private Api api;

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
    public ArrayList<IPost> getPosts(int count, int offset) throws Exception {
        ArrayList<IPost> posts = new ArrayList<>();
        for (WallMessage message : api.getWallMessages(userId, count, offset)) {
            posts.add(new VKPost(this, message));
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

    protected String getNameById(long id) {
        return String.valueOf(id);
    }
}
