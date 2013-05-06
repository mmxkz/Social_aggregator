package org.susu.sa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.susu.sa.R;
import org.susu.sa.soc.vk.VKSource;
import org.susu.sa.soc.vk.VKWebViewClient;
import com.perm.kate.api.Api;


public class VKLoginActivity extends Activity {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String APP_PREFERENCES = "mysettings";
    Api api;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String key = getIntent().getExtras().getString("key_str");
        SharedPreferences settings = getApplicationContext().getSharedPreferences(
                APP_PREFERENCES, Context.MODE_PRIVATE);

        String access_token = settings.getString("access_token", null);
        long expires = settings.getLong("access_expires", 0);

        switch (key){
            case "login_vk":{
                if (access_token != null){
                    Intent intent = new Intent();
                    intent.putExtra(KEY_ACCESS_TOKEN, settings.getString("access_token", null));
                    intent.putExtra(KEY_USER_ID, settings.getLong("user_id", 0));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                else {
                    setContentView(R.layout.vklogin_webview);
                    VKSource.callWebView(this, (WebView) findViewById(R.id.webView), new VKLogin(this));
                }
//
                break;
            }
            case "logout_vk":{
                if (access_token != null){
                    SharedPreferences.Editor edit = settings.edit();
                    edit.remove(KEY_USER_ID);
                    edit.remove(KEY_ACCESS_TOKEN);
                    edit.commit();
                    Intent intent = new Intent();
                    intent.putExtra(KEY_ACCESS_TOKEN, 0);
                    intent.putExtra(KEY_USER_ID, 0);
                    Toast.makeText(this, "logout of VK", Toast.LENGTH_LONG).show();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                else
                    Toast.makeText(this, "You are not login to VK", Toast.LENGTH_LONG).show();
                    //vkState.setEnabled(false);
                break;
            }
            default:
                Toast.makeText(this, key, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public class VKLogin extends VKWebViewClient {


        private final Context context;

        public VKLogin(Context context) {
            this.context = context;
        }

        @Override
        public void onAuthSuccess(String accessToken, long userId) {
            SharedPreferences settings = context.getSharedPreferences(
                    APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = settings.edit();
            edit.putLong(KEY_USER_ID, userId);
            edit.putString(KEY_ACCESS_TOKEN, accessToken);
            edit.commit();

            Intent intent = new Intent();

            intent.putExtra(KEY_ACCESS_TOKEN, accessToken);
            intent.putExtra(KEY_USER_ID, userId);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        @Override
        public void onAuthFailure(Exception e) {
            Toast.makeText(context, "Authorization failed", 3000).show();
        }
    }

}
