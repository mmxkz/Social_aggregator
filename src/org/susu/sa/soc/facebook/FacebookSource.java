package org.susu.sa.soc.facebook;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.facebook.*;
import com.facebook.model.GraphUser;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.Post;

import java.util.ArrayList;

public class FacebookSource implements ISource, Request.Callback {

    private Session facebookSession;
    private Activity activity;
    private final IFacebookCallback facebookCallback;
    private GraphUser me;
    private ISource source;
    private Response response;

    public FacebookSource(FacebookActivity activity, IFacebookCallback callback) {
        this.activity = activity;
        this.facebookCallback = callback;
        this.source = this;

        // start Facebook Login
        Session.openActiveSession(activity, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session s, SessionState state, Exception exception) {
                if (s.isOpened()) {
                    facebookSession = s;
                    Request.executeMeRequestAsync(s, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            me = user;
                            facebookCallback.onSessionStored(source);
                        }
                    });
                }
            }
        });
    }

    @Override
    public ArrayList<Post> getPosts(int count, int offset) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void newPost(String body) throws Exception {
        Bundle bundle = new Bundle();
        bundle.putString("access_token", facebookSession.getAccessToken());
        bundle.putString("message", body);
        new Request(facebookSession, "me/feed", bundle, HttpMethod.POST, this).executeAndWait();
        Log.e("facebook-new-post", response.toString());
    }

    @Override
    public void onCompleted(Response response) {
        this.response = response;
    }
}
