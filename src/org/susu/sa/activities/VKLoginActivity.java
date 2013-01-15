package org.susu.sa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.webkit.WebView;
import android.widget.Toast;
import org.susu.sa.R;
import org.susu.sa.soc.vk.VKSource;
import org.susu.sa.soc.vk.VKWebViewClient;

/**
 * User: is
 * Date: 1/14/13
 * Time: 10:19 PM
 */
public class VKLoginActivity extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vklogin_webview);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        VKSource.callWebView(this, (WebView) findViewById(R.id.webView), new VKLogin(this));
    }

    public class VKLogin extends VKWebViewClient {


        private final Context context;

        public VKLogin(Context context) {
            this.context = context;
        }

        @Override
        public void onAuthSuccess(String accessToken, long userId) {
            SharedPreferences prefs = context.getSharedPreferences(
                    "org.susu.sa", MODE_PRIVATE);

            prefs.edit().putLong("user_id", userId);
            prefs.edit().putString("access_token", accessToken);
            prefs.edit().commit();
            Intent intent=new Intent();

            intent.putExtra("token", accessToken);
            intent.putExtra("user_id", userId);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        @Override
        public void onAuthFailure(Exception e) {
            Toast.makeText(context, "Authorization failed", 3000).show();
        }
    }

}
