package org.susu.sa;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import com.facebook.Session;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.facebook.FacebookActivity;
import org.susu.sa.soc.facebook.FacebookSource;
import org.susu.sa.soc.facebook.IFacebookCallback;

import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends FacebookActivity {

    private List<Post> posts = new ArrayList<Post>();
    private Context context;
    private Session session;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postactivity_layout);
        context = this;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FacebookSource source = new FacebookSource(this, new IFacebookCallback() {
            @Override
            public void onSessionStored(ISource source) {
                try {
                    source.newPost("Hello, World!");
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }
}
