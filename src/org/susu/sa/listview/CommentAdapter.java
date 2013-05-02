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
import com.perm.kate.api.Comment;
import com.perm.kate.api.CommentList;
import org.susu.sa.R;
import org.susu.sa.soc.Post;
import org.susu.sa.soc.PostComment;

import java.util.ArrayList;
import java.util.List;


public class CommentAdapter extends BaseAdapter {

    private Context context;
    private List<PostComment> comments;

    public CommentAdapter(Context context, List<PostComment> comments) {
        setContext(context);
        setComments(comments);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<PostComment> getComments() {
        return comments;
    }

    public void setComments(List<PostComment> comments) {
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PostComment comment = comments.get(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.comment_layout, null);
        }

        TextView name = (TextView) view.findViewById(R.id.sender);
        name.setText(comment.getSender());

        TextView message = (TextView) view.findViewById(R.id.message);
        message.setText(comment.getBody());

        TextView date = (TextView) view.findViewById(R.id.date);
        //new out date
        date.setText(DateUtils.formatDateTime(context, comment.getDateMsg()*1000,
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE));

        return view;
    }

    public void addItem(PostComment comment) {
        comments.add(comment);
        notifyDataSetChanged();
        Log.w("Count comments: ", "" + getCount() + "");
    }
}
