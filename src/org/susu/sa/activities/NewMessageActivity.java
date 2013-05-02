package org.susu.sa.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import com.perm.kate.api.Api;
import org.susu.sa.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.vk.VKSource;

public class NewMessageActivity extends Activity {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String API_ID = "3354665";
    Button postButton;
    CheckBox CheckVK;
    CheckBox CheckFB;
    EditText messageEditText;
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
        postButton.setOnClickListener(postClick);

    }

    private OnClickListener postClick = new OnClickListener(){
        @Override
        public void onClick(View v) {
            postToWall();
        }
    };


     private void postToWall() {
         //Общение с сервером в отдельном потоке чтобы не блокировать UI поток
         if (CheckVK.isChecked()& !CheckFB.isChecked()){
             new Thread(){
                 @Override
                 public void run(){
                    try {
                        SharedPreferences settings = getApplicationContext().getSharedPreferences(
                                APP_PREFERENCES, Context.MODE_PRIVATE);
                        String text = messageEditText.getText().toString();
                        VKSource myApi = new VKSource(settings.getLong("user_id", 0), settings.getString("access_token", null));
                        //myApi.newPost(text);
                        ISource IApiVk = myApi;
                        IApiVk.newPost(text);
                        //Показать сообщение в UI потоке
                        runOnUiThread(successRunnable);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                 }
             }.start();
             messageEditText.setText("");
             messageEditText.clearFocus();
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
}

