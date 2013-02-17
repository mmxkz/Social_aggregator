package org.susu.sa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.susu.sa.R;
import org.susu.sa.listview.PostAdapter;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.Post;
import org.susu.sa.soc.vk.VKSource;

import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends Activity {

    private List<Post> posts = new ArrayList<Post>();
    private ArrayList<ISource> sources = new ArrayList<>();
    private final int REQUEST_LOGIN = 1;
    private PostAdapter adapter;

    public final static int LOAD_STEP = 30;
    private int countLoaded = LOAD_STEP;

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
                comment.putExtra("post", posts.get(position - 1 >= 0 ? position - 1 : position));
                startActivity(comment);
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
                Intent intent = new Intent(getApplicationContext(), VKLoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
                return true;
            case R.id.login_to_fb:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void receivePosts(int count, int offset) {
        for (ISource source : sources) {
            try {
                for (Post post : source.getPosts(count, offset)) { // LAL
                    adapter.addItem(post);
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        countLoaded = count + offset;
    }

    private void refreshPosts() {
        // TODO clear list
        receivePosts(countLoaded, 0);
    }
}