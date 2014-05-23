package org.susu.sa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.susu.sa.R;
import org.susu.sa.listview.MessageAdapter;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.MessageABS;
import org.susu.sa.soc.PostMessage;
import org.susu.sa.soc.vk.VKSource;
import android.support.v4.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 06.05.14.
 */
public class DialogsActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    String LOGKEY = "Dialog: ";
    private final static int LOAD_STEP = 10;
    private int offsetLoad = 0;
    private List<PostMessage> messages = new ArrayList<PostMessage>();
    private MessageAdapter messageAdapter;
    private ArrayList<ISource> sources = new ArrayList<ISource>();
    private String access_token;
    private Long user_id;
    private ArrayList<MessageABS> message;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar prb;
    ListView messagesList;
    boolean loading = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        access_token = settings.getString("access_token", null);
        user_id = settings.getLong("user_id", 0);
        messagesList = (ListView) findViewById(R.id.MessagesView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.dialog_list);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        if (messageAdapter == null) {
            messagesList = initList(messagesList);
            messageAdapter = new MessageAdapter(this, messages);
            // делаем повеселее
            messagesList.setAdapter(messageAdapter);
            listOnClickListener(messagesList);
            Log.e(LOGKEY,"message adapter is null");
        }
        messagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Log.d(LOG_TAG, "scrollState = " + scrollState);
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean loadMore =  firstVisibleItem + visibleItemCount >= totalItemCount-1;
                if (loading) return;
                //loading = true;
                if (loadMore) {
                    addDialogs();
                }
            }
        });

        //pd = new ProgressDialog(this);
//        offsetLoad = 0;
//        AsyncReceive mt = new AsyncReceive();
//        mt.execute();
    }
    @Override
    public void onRefresh() {
        // говорим о том, что собираемся начать
//        Toast.makeText(this, R.string.refresh_started, Toast.LENGTH_SHORT).show();
        // начинаем показывать прогресс
        mSwipeRefreshLayout.setRefreshing(true);
        // ждем 3 секунды и прячем прогресс
        Toast.makeText(DialogsActivity.this, "refresh...", Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                // говорим о том, что собираемся закончить

                refreshDialogs();
            }
        }, 500);
    }
    private void listOnClickListener(ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
                Intent talk = new Intent(getApplicationContext(), TalksActivity.class);
                Log.d(LOGKEY,"Position:" + position);
                Log.d(LOGKEY,"Size: " + messages.size());
                long tmp = ((MessageABS) messages.get(position)).getOwner();
                talk.putExtra("position", tmp);
                startActivity(talk);
                //startActivityForResult(comment, RCode);
            }
        });
    }
    private ListView initList(ListView list) {
        list.setClickable(true);
        list.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        View footerView =  ((LayoutInflater)getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_message_view, null, false);
        footerView.setEnabled(false);

        list.addFooterView(footerView);

        prb = (ProgressBar)findViewById(R.id.progressReceive);
        prb.setVisibility(View.VISIBLE);

        return list;
    }
    public void refreshDialogs() {
        // TODO clear list
        messages.clear();
        offsetLoad = 0;
        AsyncReceive mt = new AsyncReceive();
        mt.execute();
    }
    public void addDialogs(){
        AsyncReceive mt = new AsyncReceive();
        mt.execute();

    }
    class AsyncReceive  extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            final VKSource source = new VKSource(user_id, access_token);
            try {
                message = source.getDialogs(offsetLoad,LOAD_STEP);
                Log.e(LOGKEY, "OffsetLoad " + offsetLoad);
                Log.e(LOGKEY, "LoadStep " + LOAD_STEP);
            } catch (Exception e) {
                Log.e(LOGKEY, Log.getStackTraceString(e));  //To change body of catch statement use File | Settings | File Templates.
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            prb.setVisibility(View.VISIBLE);
            super.onPreExecute();
            loading = true;
            Toast.makeText(DialogsActivity.this, "loading...", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.e(LOGKEY, "i know tis string");
            for ( int i = 0; i < 10; i++) { //
                Log.e(LOGKEY, "on Post Execute: " + message.get(i).getBody().toString());
                messageAdapter.addItem(message.get(i));
                onProgressUpdate();
            }
            Toast.makeText(DialogsActivity.this, "done", Toast.LENGTH_SHORT).show();
            prb.setVisibility(View.INVISIBLE);
            loading = false;
            Log.e(LOGKEY, "add dialogs, offsetLoad value 1 :" + offsetLoad );
            offsetLoad += 10;
            Log.e(LOGKEY, "add dialogs, offsetLoad value 2 :" + offsetLoad );
            super.onPostExecute(result);
        }

    }
}
