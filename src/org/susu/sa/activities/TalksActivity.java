package org.susu.sa.activities;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.perm.kate.api.Message;
import org.susu.sa.R;
import org.susu.sa.listview.TalkAdapter;
import org.susu.sa.soc.MessageABS;
import org.susu.sa.soc.PostMessage;
import org.susu.sa.soc.vk.VKSource;
import android.support.v4.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Andrey on 17.05.14.
 */
public class TalksActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
        String LOGKEY = "Talk: ";
        private final static int LOAD_STEP = 20;
        private int offsetLoad = 0;
        private List<PostMessage> talks = new ArrayList<PostMessage>();
        private TalkAdapter talkAdapter;
        private String access_token;
        private Long user_id;
        private Long position;

        private ArrayList<MessageABS> message;
        SwipeRefreshLayout mSwipeRefreshLayout;
        ProgressBar prb;
        ListView talksList;
        boolean loading = false;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.talk_activity);
            SharedPreferences settings = getApplicationContext().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
            access_token = settings.getString("access_token", null);
            user_id = settings.getLong("user_id", 0);
            position = getIntent().getExtras().getLong("position",0);
            talksList = (ListView) findViewById(R.id.TalksView);
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.talk_list);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            if (talkAdapter == null) {
                talksList = initList(talksList);
                talkAdapter = new TalkAdapter(this, talks);
                // делаем повеселее
                talksList.setAdapter(talkAdapter);

                listOnClickListener(talksList);
                Log.e(LOGKEY,"message adapter is null");
            }
            refreshDialogs();
            talksList.setOnScrollListener(new AbsListView.OnScrollListener() {
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // Log.d(LOG_TAG, "scrollState = " + scrollState);
                }

                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount - 1;
                    if (loading) return;
                    //loading = true;
                    if (loadMore) {
//                        refreshDialogs();
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
            Toast.makeText(TalksActivity.this, "refresh...", Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    // говорим о том, что собираемся закончить
                    addDialogs();
                }
            }, 500);
        }
    private void listOnClickListener(ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
//                Intent comment = new Intent(getApplicationContext(), CommentActivity.class);
//                long tmp = ((VKPost) posts.get(position - 1)).getPostId();
//                String text = ((VKPost) posts.get(position - 1)).getBody();
//                comment.putExtra("position", tmp);
//                comment.putExtra("text", text);
//                startActivity(comment);
//                //startActivityForResult(comment, RCode);
            }
        });
    }
    private ListView initList(ListView list) {
        list.setClickable(true);
        list.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        View footerView =  ((LayoutInflater)getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_message_view, null, false);
        //pr = (ProgressBar)findViewById(R.id.progressReceive);
        footerView.setEnabled(false);

        list.addFooterView(footerView);

        prb = (ProgressBar)findViewById(R.id.progressReceive);
        prb.setVisibility(View.VISIBLE);

        return list;
    }

    public void refreshDialogs() {
        // TODO clear list
        talks.clear();
        offsetLoad = 0;
        AsyncReceive mt = new AsyncReceive();
        mt.execute();
    }
    public void addDialogs(){
        //clear();
        AsyncReceive mt = new AsyncReceive();
        mt.execute();

    }

    class AsyncReceive  extends AsyncTask<Void, MessageABS, Void> {
        private Integer i = 0;
        @Override
        protected Void doInBackground(Void... voids) {
            final VKSource source = new VKSource(user_id, access_token);
            try {
                message = source.getMessages(position, Long.valueOf(0) ,offsetLoad,LOAD_STEP);
                Log.e(LOGKEY, "OffsetLoad " + offsetLoad);
                Log.e(LOGKEY, "LoadStep " + LOAD_STEP);
            } catch (Exception e) {
                Log.e(LOGKEY, Log.getStackTraceString(e));  //To change body of catch statement use File | Settings | File Templates.
            }
            for ( int i = 0; i < message.size(); i++) { //
                Log.e(LOGKEY, "on Post Execute: " + message.get(i).getBody().toString());
                this.publishProgress(message.get(i));
                try {
                    Thread.sleep(50);//simulate a network call
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onPreExecute() {
            prb.setVisibility(View.VISIBLE);
            super.onPreExecute();
            loading = true;
            Toast.makeText(TalksActivity.this, "loading...", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProgressUpdate(MessageABS... messageTMP) {
            talkAdapter.addItem(0, messageTMP[0]);//добавляем сообщения в обратном хронологическому порядке
            i++;

        }
        @Override
        protected void onPostExecute(Void result) {
            Log.e(LOGKEY, "i know tis string");
//            for ( int i = 0; i < LOAD_STEP; i++) { //
//                Log.e(LOGKEY, "on Post Execute: " + message.get(i).getBody().toString());
//                talkAdapter.addItem(i, message.get((LOAD_STEP-1) - i));//добавляем сообщения в обратном хронологическому порядке
//                onProgressUpdate();
//            }
            Toast.makeText(TalksActivity.this, "done", Toast.LENGTH_SHORT).show();
            prb.setVisibility(View.INVISIBLE);
            loading = false;
//            Log.e(LOGKEY, "add dialogs, offsetLoad value 1 :" + offsetLoad );
            offsetLoad += LOAD_STEP;
//            Log.e(LOGKEY, "add dialogs, offsetLoad value 2 :" + offsetLoad );
            super.onPostExecute(result);
        }

    }
}

