package org.susu.sa.listview;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.susu.sa.R;
import org.susu.sa.soc.Post;
import java.util.List;


public class PostAdapter extends BaseAdapter  {

    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        setContext(context);
        setPosts(posts);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Post entry = posts.get(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.post_layout, null);
        }

        TextView name = (TextView) view.findViewById(R.id.sender);
        name.setText(entry.getSender());

        TextView message = (TextView) view.findViewById(R.id.message);
        message.setText(entry.getBody());

        TextView date = (TextView) view.findViewById(R.id.date);
        //new out date
        date.setText(DateUtils.formatDateTime(context, entry.getDateMsg()*1000,
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE));

        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        image.setImageBitmap(entry.getBitmap());

        return view;
    }
    public void addItem(Post post) {
        posts.add(post);
        notifyDataSetChanged();
        Log.d("Count posts: ", "" + getCount() + "");
    }
}
