package org.susu.sa.soc;

import java.util.ArrayList;

public abstract class Post extends PostComment {

    public abstract void reply(String body) throws Exception;
    public abstract ArrayList<PostComment> getComments(int count, int body) throws Exception;

}
