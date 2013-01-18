package org.susu.sa.soc.facebook;

import android.app.Activity;
import android.content.Intent;
import com.facebook.Session;

public class FacebookActivity extends Activity {

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
}
