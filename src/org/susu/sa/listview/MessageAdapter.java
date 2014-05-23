package org.susu.sa.listview;

/**
 * Created by Andrey on 06.05.14.
 */

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
import java.util.List;

import org.susu.sa.soc.MessageABS;
import org.susu.sa.soc.PostMessage;



public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<PostMessage> messages;

    public MessageAdapter(Context context, List<PostMessage> messages) {
        setContext(context);
        setMessage(messages);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<PostMessage> getMessages() {
        return messages;
    }

    public void setMessage(List<PostMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PostMessage entry = messages.get(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.message_layout, null);
        }

        TextView name = (TextView) view.findViewById(R.id.sender);
        name.setText(entry.getSender());

        TextView message = (TextView) view.findViewById(R.id.message);
        message.setText(entry.getBody());

        TextView date = (TextView) view.findViewById(R.id.date);
        //new out date
        date.setText(DateUtils.formatDateTime(context, entry.getDateMsg() * 1000,
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE));

        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        image.setImageBitmap(entry.getBitmap());

        return view;
    }
    public void addItem(MessageABS message) {
        messages.add(message);
        notifyDataSetChanged();
        Log.d("Count posts: ", "" + getCount() + "");
        Log.e("Dialog: ", message.toString());
    }
}
