package org.susu.sa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.perm.kate.api.Api;
import com.perm.kate.api.Comment;
import com.perm.kate.api.KException;
import org.json.JSONException;
import org.susu.sa.R;
import org.susu.sa.listview.CommentAdapter;
import org.susu.sa.listview.PostAdapter;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.Post;
import org.susu.sa.soc.PostComment;
import org.susu.sa.soc.vk.VKPost;
import org.susu.sa.soc.vk.VKSource;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Andrey
 * Date: 1/13/13
 * Time: 2:26 AM
 */
public class CommentActivity extends Activity {
    private static final int CM_DELETE_ID = 1;
    private Context context = null;
    public static final String APP_PREFERENCES = "mysettings";
    private List<PostComment> comment = new ArrayList<PostComment>();
    private CommentAdapter adapter;
    private Long Post_id;
    private Long Cid_to_Reply;
    EditText Reply;
    Button Send;
    Integer countLoaded;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        ListView list = (ListView) findViewById(R.id.CommentsView);
        list.setClickable(true);
        Log.d("post id comment:", "" + getIntent().getExtras().getLong("position"));
        Post_id = getIntent().getExtras().getLong("position");
        if (adapter == null) {
            list = initList(list);
            adapter = new CommentAdapter(this, comment);
            list.setAdapter(adapter);
            listOnClickListener(list);
        }
        receiveComments(getIntent().getExtras().getLong("position",0), 20, 0);

        Button showButton = (Button) findViewById(R.id.show_button);

        Reply = (EditText) findViewById(R.id.ReplyText);
        Send = (Button) findViewById(R.id.SendComment);
        Send.setOnClickListener(SendListener);
        Log.d("post id comment:", "" + getIntent().getExtras().getLong("position"));
        list.setOnCreateContextMenuListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
                Cid_to_Reply = comment.get(position).getCid();
                String[] nameMas = comment.get(position).getSender().split(" ");
                String name = nameMas[0];
                Reply.setText(name + ", ");
            }
        });
        showButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                refreshComments(getIntent().getExtras().getLong("position", 0));
            }
        });

    }
    private OnClickListener SendListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            ReplyToComment();
        }
    };
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, "Удалить запись");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try{
            AdapterView.AdapterContextMenuInfo tmp = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            PostComment itemComment = comment.get(tmp.position);
            SharedPreferences settings = getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            VKSource myApi = new VKSource(settings.getLong("user_id", 0), settings.getString("access_token", null));
            myApi.deleteComment(itemComment.getCid());
        }
        catch (KException k) {;}
        catch (IOException i) {;}
        catch (JSONException j) {;}
        refreshComments(Post_id);
        Toast.makeText(getApplicationContext(),"комментарий удален", Toast.LENGTH_LONG);
        return super.onContextItemSelected(item);

    }

    private void ReplyToComment(){
        Long post_id = getIntent().getExtras().getLong("position",0);
        try {
            SharedPreferences settings = getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            VKSource myApi = new VKSource(settings.getLong("user_id", 0), settings.getString("access_token", null));
            VKPost MyPost = new VKPost(myApi, post_id, null, "", null, null);
            Log.e("cid to reply:", "" + Cid_to_Reply);
            String text = Reply.getText().toString();
            MyPost.reply(text, Cid_to_Reply);
            Reply.setText("");
            Reply.clearFocus();
            refreshComments(Post_id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private ListView initList(ListView list) {
        View footerView =  ((LayoutInflater)getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_message_view, null, false);

        list.addFooterView(footerView);
        return list;
    }

    private void listOnClickListener(ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {

            }
        });
    }

    private void receiveComments(Long postId,int count, int offset) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(
                APP_PREFERENCES, Context.MODE_PRIVATE);
        String access_token = settings.getString("access_token", null);
        Long user_id = settings.getLong("user_id", 22457823);
        VKSource source = new VKSource(user_id, access_token);
            try {
                for (PostComment comment : source.getComments(postId, count, offset)) { // lalka
                    adapter.addItem(comment);
                    Log.w("ISource lalka:", "" + getIntent().getExtras().getLong("position"));
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        countLoaded = count + 20;
    }

    public void refreshComments(Long postId) {
        // TODO clear list
        comment.clear();
        receiveComments(postId, countLoaded, 0);
    }
}
