package org.susu.sa.soc;

import android.graphics.Bitmap;
import com.perm.kate.api.KException;
import org.json.JSONException;
import org.susu.sa.soc.vk.VKMessage;
import org.susu.sa.soc.vk.VKPost;
import com.perm.kate.api.*;

import java.io.IOException;
import java.util.ArrayList;

public interface ISource {
    ArrayList<Post> getPosts(int count, int offset) throws Exception;
    ArrayList<PostComment> getComments(long postId ,int count, int offset) throws Exception;
    void newPost(String body, String services) throws IOException, JSONException, KException;
    void setStatus(String text) throws IOException, JSONException, KException;
    VkStatus getStatus() throws IOException, JSONException, KException;
    void reply(VKPost post, String body, Long cid) throws KException, IOException, JSONException;
    void deleteComment(Long cid) throws KException, IOException, JSONException;
    ArrayList<MessageABS> getDialogs(int count, int offset) throws Exception;
    //Bitmap getUserImage(Long id) throws Exception;
}
