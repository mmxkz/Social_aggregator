package org.susu.sa.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.*;
import com.perm.kate.api.Api;
import org.susu.sa.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.vk.VKSource;

public class NewMessageActivity extends Activity {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String API_ID = "3354665";
    Button postButton;
    CheckBox CheckVK;
    CheckBox CheckFB;
    ToggleButton reTwit;
    ToggleButton statusVK;
    EditText messageEditText;
    String services = "";
    Api api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newmessageactivity_layout);
        setupUI();
    }

    private void setupUI() {
        postButton = (Button)findViewById(R.id.write_message_button);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        CheckVK = (CheckBox)findViewById(R.id.vk_checkbox);
        CheckFB = (CheckBox)findViewById(R.id.fb_checkbox);
        reTwit = (ToggleButton)findViewById(R.id.reTwit);
        statusVK = (ToggleButton)findViewById(R.id.statusVK);
        postButton.setOnClickListener(postClick);
        }

    private OnClickListener postClick = new OnClickListener(){
        @Override
        public void onClick(View v) {
            postToWall();
        }
    };
    public void checkedVK(View v){
        if (CheckVK.isChecked()){

            reTwit.setEnabled(true);
            statusVK.setEnabled(true);
            }
        else {
            reTwit.setChecked(false);
            reTwit.setEnabled(false);
            statusVK.setChecked(false);
            statusVK.setEnabled(false);
        }
    }


     private void postToWall() {
         //Общение с сервером в отдельном потоке чтобы не блокировать UI поток
         if (CheckVK.isChecked()& !CheckFB.isChecked()){
             final String text = messageEditText.getText().toString();
             new Thread(){
                 @Override
                 public void run(){

                     if (reTwit.isChecked()) {
                         services = "twitter";
                     }
                    try {
                        SharedPreferences settings = getApplicationContext().getSharedPreferences(
                                APP_PREFERENCES, Context.MODE_PRIVATE);

                        VKSource myApi = new VKSource(settings.getLong("user_id", 0), settings.getString("access_token", null));
                        //myApi.newPost(text);
                        ISource IApiVk = myApi;
                        Log.i("text:", text + "\n");
                        Log.i("Services:", services + "\n");
                        IApiVk.newPost(text,services);

                        if(statusVK.isChecked()){
                            IApiVk.setStatus(text);
                        }
                        //Показать сообщение в UI потоке
                        runOnUiThread(successRunnable);
                    } catch (Exception e){
                        e.printStackTrace();
                        runOnUiThread(failedRunnable);
                    }
                 }
             }.start();
             messageEditText.setText("");
             messageEditText.clearFocus();
             services = "";
         }
         if (CheckFB.isChecked()){
             Toast.makeText(getApplicationContext(), "Не доступно", Toast.LENGTH_LONG).show();
         }
         if (!CheckFB.isChecked() & !CheckVK.isChecked()){
             Toast.makeText(getApplicationContext(), "Выберите социальный сервис", Toast.LENGTH_LONG).show();
         }
    }

    Runnable successRunnable = new Runnable(){
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), "Запись успешно добавлена", Toast.LENGTH_LONG).show();
        }
    };
    Runnable failedRunnable = new Runnable() {
        @Override
        public void run() {
            //To change body of implemented methods use File | Settings | File Templates.
            Toast.makeText(getApplicationContext(), "Ошибка при добавлении записи", Toast.LENGTH_LONG).show();
        }
    };
}

