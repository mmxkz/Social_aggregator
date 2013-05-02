package org.susu.sa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.webkit.WebView;
import android.widget.Toast;
import com.perm.kate.api.Api;
import org.susu.sa.R;
import org.susu.sa.soc.vk.VKSource;
import org.susu.sa.soc.vk.VKWebViewClient;

/**
 * Created with IntelliJ IDEA.
 * User: Andrey
 * Date: 06.04.13
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
public class VKLogoutActivity extends Activity{
        public static final String KEY_USER_ID = "user_id";
        public static final String KEY_ACCESS_TOKEN = "access_token";
        public static final String APP_PREFERENCES = "mysettings";
        //Api api;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            Toast.makeText(this, "LOGOUT OF VK", 3000).show();
            Intent intent = new Intent();
            SharedPreferences settings = getApplicationContext().getSharedPreferences(
                    APP_PREFERENCES, Context.MODE_PRIVATE);

            //if (settings.contains("user_id") && settings.contains("access_token")) {
            SharedPreferences.Editor edit = settings.edit();
            edit.remove(KEY_USER_ID);
            edit.remove(KEY_ACCESS_TOKEN);
            edit.commit();
            setResult(Activity.RESULT_OK, intent);
            finish();
            }
        }



