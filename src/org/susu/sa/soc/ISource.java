package org.susu.sa.soc;

import java.util.ArrayList;

public interface ISource {
    ArrayList<Post> getPosts(int count, int offset) throws Exception;
    void newPost(String body) throws Exception;
}
