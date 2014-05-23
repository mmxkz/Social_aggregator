package org.susu.sa.listview;

import android.content.Context;
import android.provider.ContactsContract;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.susu.sa.R;
import org.susu.sa.soc.MessageABS;
import org.susu.sa.soc.PostMessage;

import java.util.List;

/**
 * Created by Andrey on 17.05.14.
 */
public class TalkAdapter extends BaseAdapter {
    private Context context;
    private List<PostMessage> messages;
    private String LOGKEY = "TALK ADAPTER: ";

    public TalkAdapter(Context context, List<PostMessage> messages) {
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
        ViewHolder holder;
//        view = null; //ToDo: разобраться почему не применяются параметры, когда view уже есть
//        if (view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.talk_layout, viewGroup, false);
            holder.message = (TextView) view.findViewById(R.id.message_text);
            holder.logo = (ImageView) view.findViewById(R.id.logoView);
            holder.date = (TextView)view.findViewById(R.id.date_talk);
            view.setTag(holder);
//        }
//        else
//            holder = (ViewHolder) view.getTag();

        holder.message.setText(entry.getBody());
        holder.logo.setImageBitmap(entry.getBitmap());
        TextView date = (TextView) view.findViewById(R.id.date);
//        //new out date
        holder.date.setText(DateUtils.formatDateTime(context, entry.getDateMsg() * 1000,
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE));

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.message.getLayoutParams();
        RelativeLayout.LayoutParams lpLogo = (RelativeLayout.LayoutParams) holder.logo.getLayoutParams();
        RelativeLayout.LayoutParams lpDate = (RelativeLayout.LayoutParams) holder.date.getLayoutParams();

        //Check whether message is mine to show green background and align to right
        if(entry.getOwner() != 22457823)
        {

            holder.message.setBackgroundResource(R.drawable.speech_bubble_orange);
            try  {
                lpLogo.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lpDate.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            }
            catch (NullPointerException e){
                Log.d(LOGKEY, e.toString());
            }
            Log.d(LOGKEY, "TALK ID: " + entry.getOwner());
        }
        //If not mine then it is from sender to show orange background and align to left
        else
        {
            holder.message.setBackgroundResource(R.drawable.speech_bubble_green);
            try  {
                lpLogo.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            catch (NullPointerException e){
                Log.d(LOGKEY, e.toString());
            }

            Log.d(LOGKEY, "TALK ID: " + entry.getOwner());
        }
        holder.message.setLayoutParams(lp);

        return view;

//        if (view == null) {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(R.layout.message_layout, null);
//        }
//
//        TextView name = (TextView) view.findViewById(R.id.sender);
//        name.setText(entry.getSender());
//
//        TextView message = (TextView) view.findViewById(R.id.message);
//        message.setText(entry.getBody());
//
//        TextView date = (TextView) view.findViewById(R.id.date);
//        //new out date
//        date.setText(DateUtils.formatDateTime(context, entry.getDateMsg() * 1000,
//                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE));
//
//        ImageView image = (ImageView) view.findViewById(R.id.imageView);
//        image.setImageBitmap(entry.getBitmap());
//
//        return view;
    }

    public void addItem(int i, MessageABS message) {
        messages.add(i, message);
        notifyDataSetChanged();
        Log.d("Count talk: ", "" + getCount() + "");
        Log.d("Talk: ", message.toString());
    }
    private static class ViewHolder
    {
        TextView message;
        TextView date;
        ImageView logo;

    }
}

