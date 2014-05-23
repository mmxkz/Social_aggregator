package org.susu.sa;

/**
 * Created by Andrey on 20.03.14.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import org.susu.sa.activities.PostsActivity;


public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    private String registrationId;

    public String getRegistrationId() {
        return registrationId;
    }

    public GCMIntentService() {
        super("986691428673");
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "GCM: Device registered");
        // Здесь мы должны отправить registrationId на наш сервер, чтобы он смог на него отправлять уведомления
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "GCM: Device unregistered with " + registrationId);
        this.registrationId = registrationId;
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "GCM: Received new message" + intent.getExtras().toString());

        Bundle extras = intent.getExtras();
        String message = "NoMessage";
        if (extras!=null){
            message = extras.getString("text","No Message");
        }
        Intent pushIntent = new Intent(context, PostsActivity.class);
        pushIntent.putExtras(intent);

        PendingIntent pi = PendingIntent.getActivity(context, 0, pushIntent, 0);

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = new Notification(R.drawable.vk_logo_big, message, System.currentTimeMillis());
        n.setLatestEventInfo(context, message, "", pi);
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        nm.notify(1, n);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "GCM: Received deleted messages notification");
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "GCM: Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.i(TAG, "GCM: Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }
    //GSM testing
    // Делаем проверки

}
