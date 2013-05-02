package org.susu.sa.soc;

import com.perm.kate.api.KException;
import org.json.JSONException;
import org.susu.sa.soc.vk.VKPost;
import com.perm.kate.api.Comment;

import java.io.IOException;
import java.util.ArrayList;

public interface ISource {
    ArrayList<Post> getPosts(int count, int offset) throws Exception;
    ArrayList<PostComment> getComments(long postId ,int count, int offset) throws Exception;
    void newPost(String body) throws Exception;
    void reply(VKPost post, String body, Long cid) throws KException, IOException, JSONException;
    void deleteComment(Long cid) throws KException, IOException, JSONException;
}
