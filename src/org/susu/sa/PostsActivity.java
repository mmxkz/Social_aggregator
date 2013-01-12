package org.susu.sa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import org.susu.sa.soc.Post;
import org.susu.sa.soc.PostComment;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.vk.VKSource;
import org.susu.sa.soc.vk.VKWebViewClient;

public class PostsActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vktest);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        VKSource.callWebView(this, (WebView) findViewById(R.id.webView), new WVClient(this));
    }

    class WVClient extends VKWebViewClient {

        private final Context context;

        public WVClient(Context context) {
            this.context = context;
        }

        @Override
        public void onAuthSuccess(String accessToken, long userId) {
            ISource source = new VKSource(userId, accessToken);
            try
            {
                for (PostComment post : source.getPosts(1, 0).get(0).getComments(10, 0)) {
                    Toast.makeText(context, post.getSender() + ": " + post.getBody(), 1000).show();
                }
            } catch (Exception e) {
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement el: e.getStackTrace()) {
                    builder.append(el.toString()).append("\n");
                }
                Log.e("Hello, World!", builder.toString());
            }
        }

        @Override
        public void onAuthFailure(Exception e) {
            Toast.makeText(context, "Authorization failed", 1000).show();
        }
    }
}
