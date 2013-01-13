package org.susu.sa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: is
 * Date: 1/13/13
 * Time: 2:26 AM
 */
public class CommentActivity extends Activity {

    private Context context = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);

        ListView list = (ListView) findViewById(R.id.PostsView);
        list.setClickable(true);


        View footerView =  ((LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view, null, false);
        list.addFooterView(footerView);

        List<Post> posts = new ArrayList<Post>();
        posts.add((Post) getIntent().getSerializableExtra("post"));
        posts.add(new Post("Ilya", "OK LET'S DO IT", new Date()));
        posts.add(new Post("Kosmaks", "LEEEEEEEEROY JEEEEEEENKINS", new Date()));
        posts.add(new Post("Andrey", "U menya nedopusk :(", new Date()));
        posts.add(new Post("Masha", "YA TP :(", new Date()));
        posts.add(new Post("Masha", "YA TP :(", new Date()));
        posts.add(new Post("Masha", "YA TP :(", new Date()));
        posts.add(new Post("Masha", "YA TP :(", new Date()));
        posts.add(new Post("Masha", "YA TP :(", new Date()));
        posts.add(new Post("Masha", "YA TP :(", new Date()));
        posts.add(new Post("Masha", "YA TP :(", new Date()));

        PostAdapter adapter = new PostAdapter(this, posts);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
                Log.e("ISOLO", "CLICKED");
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });


    }
}
