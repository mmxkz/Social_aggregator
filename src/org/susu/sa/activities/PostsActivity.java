package org.susu.sa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONException;
import org.susu.sa.R;
import org.susu.sa.listview.PostAdapter;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.Post;
import org.susu.sa.soc.PostComment;
import org.susu.sa.soc.vk.VKPost;
import org.susu.sa.soc.vk.VKSource;
import com.perm.kate.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends Activity {

    private List<Post> posts = new ArrayList<Post>();
    private ArrayList<ISource> sources = new ArrayList<ISource>();
    private final int REQUEST_LOGIN = 1;
    private final int REQUEST_LOGOUT = 2;
    private final int RCode = 1;
    public static final String APP_PREFERENCES = "mysettings";


    private PostAdapter adapter;

    public final static int LOAD_STEP = 20;
    private int countLoaded = LOAD_STEP;
    Api api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postactivity_layout);

        ListView list = (ListView) findViewById(R.id.PostsView);

        if (adapter == null) {
            list = initList(list);
            adapter = new PostAdapter(this, posts);
            list.setAdapter(adapter);
            listOnClickListener(list);
        }

        refreshPosts();

        int windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        int windowHeight = getWindowManager().getDefaultDisplay().getHeight();

        Button postButton = (Button) findViewById(R.id.post_button);
        setButtonParams(postButton, windowWidth / 2 - 40, windowHeight / 15);

        Button refreshButton = (Button) findViewById(R.id.refresh_button);
        setButtonParams(refreshButton, windowWidth / 2 - 40, windowHeight / 15);

        Button showMoreButton = (Button) findViewById(R.id.show_button);
        setButtonParams(showMoreButton, windowWidth, windowHeight / 10);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent writeMessage = new Intent(getApplicationContext(), NewMessageActivity.class);
                startActivity(writeMessage);
            }
        });

        showMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                receivePosts(LOAD_STEP, countLoaded);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                refreshPosts();
            }
        });
        list.setOnCreateContextMenuListener(this);

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, "Удалить запись");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try{
            AdapterView.AdapterContextMenuInfo tmp = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            SharedPreferences settings = getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            VKSource myApi = new VKSource(settings.getLong("user_id", 0), settings.getString("access_token", null));
            long PostId = ((VKPost) posts.get(tmp.position-1)).getPostId();
            Log.d("post id:","" + PostId);
            myApi.deletePost(PostId);
        }
        catch (KException k) {;}
        catch (IOException i) {;}
        catch (JSONException j) {;}
        refreshPosts();

        Toast.makeText(getApplicationContext(),"запись удалена", Toast.LENGTH_LONG);
        return super.onContextItemSelected(item);
    }

    private ListView initList(ListView list) {
        list.setClickable(true);
        list.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);

        View footerView =  ((LayoutInflater)getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_message_view, null, false);
        footerView.setEnabled(false);
        list.addFooterView(footerView);

        View headerView =  ((LayoutInflater)getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.header_post_view, null, false);
        list.addHeaderView(headerView);

        return list;
    }

    private void listOnClickListener(ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
                Intent comment = new Intent(getApplicationContext(), CommentActivity.class);
                long tmp = ((VKPost) posts.get(position - 1)).getPostId();
                comment.putExtra("position", tmp);
                startActivity(comment);
                //startActivityForResult(comment, RCode);

            }
        });
    }

    private void setButtonParams(Button button, int width, int height) {
        button.setWidth(width);
        button.setHeight(height);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            sources.add(
                new VKSource(
                    intent.getLongExtra(VKLoginActivity.KEY_USER_ID, 0),
                    intent.getStringExtra(VKLoginActivity.KEY_ACCESS_TOKEN)
                )
            );
            refreshPosts();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_to_vk:
                Intent intentVKlogin = new Intent(getApplicationContext(), VKLoginActivity.class);
                intentVKlogin.putExtra("key_str","login_vk");
                startActivityForResult(intentVKlogin, REQUEST_LOGIN);
                return true;

            case R.id.logout_of_vk:
                Intent intentVKlogout = new Intent(getApplicationContext(), VKLoginActivity.class);
                intentVKlogout.putExtra("key_str","logout_vk");
                startActivityForResult(intentVKlogout, REQUEST_LOGOUT);
                return true;

            case R.id.login_to_fb:
                Toast.makeText(getApplicationContext(),"Не доступно", Toast.LENGTH_LONG);
                return true;

            case R.id.logout_of_fb:
                Toast.makeText(getApplicationContext(),"Не доступно", Toast.LENGTH_LONG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void receivePosts(int count, int offset) {
        for (ISource source : sources) {
            try {
                for (Post post : source.getPosts(count, offset)) { // lalka
                    adapter.addItem(post);
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        countLoaded = count + offset;
    }

    public void refreshPosts() {
        // TODO clear list
        posts.clear();
        receivePosts(countLoaded, 0);
    }
}