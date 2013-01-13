package org.susu.sa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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


        posts.add(new Post("Ilya", "OK LET'S DO IT", new Date()));
        posts.add(new Post("Kosmaks", "LEEEEEEEEROY JEEEEEEENKINS", new Date()));
        posts.add(new Post("Andrey", "U menya nedopusk :(", new Date()));
        posts.add(new Post("Ovcharik", "A YA V GOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
            "VNE" +
                "", new Date()));



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

    }
}