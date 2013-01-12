package org.susu.sa;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postactivity_layout);

        ListView list = (ListView) findViewById(R.id.PostsView);
        list.setClickable(true);

        List<Post> posts = new ArrayList<Post>();
        posts.add(new Post("Ilya", "OK LET'S DO IT"));
        posts.add(new Post("Kosmaks", "LEEEEEEEEROY JEEEEEEENKINS"));
        posts.add(new Post("Andrey", "U menya nedopusk :("));
        posts.add(new Post("Ovcharik", "A YA V GOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "VNE"));

        PostAdapter adapter = new PostAdapter(this, posts);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

        list.setAdapter(adapter);
    }


}
