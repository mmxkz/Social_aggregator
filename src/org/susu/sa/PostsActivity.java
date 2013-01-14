package org.susu.sa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostsActivity extends Activity {

    private List<Post> posts = new ArrayList<Post>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postactivity_layout);

        ListView list = (ListView) findViewById(R.id.PostsView);
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

        posts.add(new Post("Ilya", "OK LET'S DO IT", new Date()));
        posts.add(new Post("Kosmaks", "LEEEEEEEEROY JEEEEEEENKINS", new Date()));
        posts.add(new Post("Andrey", "U menya nedopusk :(", new Date()));
        posts.add(new Post("Ovcharik", "A YA V GOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
            "VNE", new Date()));



        PostAdapter adapter = new PostAdapter(this, posts);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
                Intent comment = new Intent(getApplicationContext(), CommentActivity.class);
                comment.putExtra("post", posts.get(position));

                startActivity(comment);
            }
        });

        Button postButton = (Button) findViewById(R.id.post_button);
        postButton.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2 - 40);
        postButton.setHeight((int) getWindowManager().getDefaultDisplay().getHeight() / 15);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent writeMessage = new Intent(getApplicationContext(), NewMessageActivity.class);

                startActivity(writeMessage);
            }
        });

        Button refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2 - 40);
        refreshButton.setHeight(getWindowManager().getDefaultDisplay().getHeight() / 15);

        Button showMoreButton = (Button) findViewById(R.id.show_button);
        showMoreButton.setHeight(100);

    }
}