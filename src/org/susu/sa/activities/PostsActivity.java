package org.susu.sa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.json.JSONException;
import org.susu.sa.R;
import org.susu.sa.listview.PostAdapter;
import org.susu.sa.soc.ISource;
import org.susu.sa.soc.Post;
import org.susu.sa.soc.vk.VKPost;
import org.susu.sa.soc.vk.VKSource;
import com.perm.kate.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends Activity  {

    private List<Post> posts = new ArrayList<Post>();
    private ArrayList<ISource> sources = new ArrayList<ISource>();
    private final int REQUEST_LOGIN = 1;
    private final int REQUEST_LOGOUT = 2;
    private final int RCode = 1;
    public static final String APP_PREFERENCES = "mysettings";
    TextView statusVK;
    ToggleButton vkState;

    private PostAdapter adapter;

    public final static int LOAD_STEP = 20;
    private int countLoaded = LOAD_STEP;
    private ArrayList<Long> uid = new ArrayList<Long>();
    Api api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postactivity_layout);
        vkState = (ToggleButton) findViewById(R.id.VKState);
        ListView list = (ListView) findViewById(R.id.PostsView);


        if (adapter == null) {
            list = initList(list);
            adapter = new PostAdapter(this, posts);
            list.setAdapter(adapter);
            listOnClickListener(list);
        }

        int windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        int windowHeight = getWindowManager().getDefaultDisplay().getHeight();

        Button postButton = (Button) findViewById(R.id.post_button);
        setButtonParams(postButton, windowWidth / 2 - 40, windowHeight / 15);

        Button refreshButton = (Button) findViewById(R.id.refresh_button);
        setButtonParams(refreshButton, windowWidth / 2 - 40, windowHeight / 15);

        Button showMoreButton = (Button) findViewById(R.id.show_button);
        setButtonParams(showMoreButton, windowWidth, windowHeight / 10);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent writeMessage = new Intent(getApplicationContext(), NewMessageActivity.class);
                startActivity(writeMessage);
            }
        });

        showMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                receivePosts(LOAD_STEP, countLoaded);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                refreshPosts();
            }
        });

        list.setOnCreateContextMenuListener(this);

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, "Удалить запись");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try{
            AdapterView.AdapterContextMenuInfo tmp = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            SharedPreferences settings = getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            VKSource myApi = new VKSource(settings.getLong("user_id", 0), settings.getString("access_token", null));
            long PostId = ((VKPost) posts.get(tmp.position-1)).getPostId();
            Log.d("post id:","" + PostId);
            myApi.deletePost(PostId);
        }
        catch (KException k) {;}
        catch (IOException i) {;}
        catch (JSONException j) {;}
        refreshPosts();

        Toast.makeText(getApplicationContext(),"запись удалена", Toast.LENGTH_LONG);
        return super.onContextItemSelected(item);
    }

    private ListView initList(ListView list) {
        list.setClickable(true);
        list.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);

        View footerView =  ((LayoutInflater)getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_message_view, null, false);
        footerView.setEnabled(false);

        list.addFooterView(footerView);

        View headerView =  ((LayoutInflater)getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.header_post_view, null, false);
        list.addHeaderView(headerView);

        return list;
    }

    private void listOnClickListener(ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
                Intent comment = new Intent(getApplicationContext(), CommentActivity.class);
                long tmp = ((VKPost) posts.get(position - 1)).getPostId();
                String text = ((VKPost) posts.get(position - 1)).getBody();
                comment.putExtra("position", tmp);
                comment.putExtra("text", text);
                startActivity(comment);
                //startActivityForResult(comment, RCode);
            }
        });
    }

    private void setButtonParams(Button button, int width, int height) {
        button.setWidth(width);
        button.setHeight(height);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            sources.add(
                new VKSource(
                    intent.getLongExtra(VKLoginActivity.KEY_USER_ID, 0),
                    intent.getStringExtra(VKLoginActivity.KEY_ACCESS_TOKEN)
                )
            );
            refreshPosts();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_to_vk:
                Intent intentVKlogin = new Intent(getApplicationContext(), VKLoginActivity.class);
                intentVKlogin.putExtra("key_str","login_vk");
                startActivityForResult(intentVKlogin, REQUEST_LOGIN);
                vkState.setEnabled(true);
                vkState.setChecked(true);
                return true;

            case R.id.logout_of_vk:
                Intent intentVKlogout = new Intent(getApplicationContext(), VKLoginActivity.class);
                intentVKlogout.putExtra("key_str","logout_vk");
                startActivityForResult(intentVKlogout, REQUEST_LOGOUT);
                vkState.setChecked(false);
                vkState.setEnabled(false);
                return true;

            case R.id.login_to_fb:
                Toast.makeText(getApplicationContext(),"Не доступно", Toast.LENGTH_LONG);
                return true;

            case R.id.logout_of_fb:
                Toast.makeText(getApplicationContext(),"Не доступно", Toast.LENGTH_LONG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void VkStateClick(View v){
        LayoutInflater ltInflater =(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout InfoLayout = (LinearLayout) findViewById(R.id.vkInfo);
        View vkInfo = ltInflater.inflate(R.layout.vkinfo, null, false);
        if(!vkState.isChecked()){
            InfoLayout.addView(vkInfo,0);
            vkInfoInit();
            Log.i("addview: ","" + vkState.isChecked());
        }
        else{
            ((LinearLayout)InfoLayout.getParent()).removeView(vkInfo);
            InfoLayout.removeViewAt(0);
            //vkInfoInit();
            Log.i("removeview: ","" + vkState.isChecked());

        }
    }
    private void receivePosts(int count, int offset) {
        for (ISource source : sources) {
            try {
                for (Post post : source.getPosts(count, offset)) { // lalka
                    adapter.addItem(post);
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        countLoaded = count + offset;
    }

    public void refreshPosts() {
        // TODO clear list
        posts.clear();
        receivePosts(countLoaded, 0);
    }
    public void vkInfoInit(){
        initUser();
        //init info vk
    }
    public void initUser(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(
                APP_PREFERENCES, Context.MODE_PRIVATE);
        Long user_id = settings.getLong("user_id", 0);
        VKSource myApi = new VKSource(user_id, settings.getString("access_token", null));

        ISource IApiVk = myApi;
        TextView VkName = (TextView)findViewById(R.id.UserNameVK);
        TextView VkStatus = (TextView)findViewById(R.id.UserStatusVK);
        ImageView VkImage = (ImageView)findViewById(R.id.UserImageVK);
        try{
            VkStatus.setText(IApiVk.getStatus().text);
            uid.add(user_id);
            String fields = "photo_big";
            ArrayList<User> tmp = myApi.getProfiles(uid, null, fields,"nom");
            if (tmp.isEmpty()) {
                Log.i("empty user info: ","" + tmp.size());
            }
            User tmpUser = tmp.get(0);
            VkImage.setImageBitmap(myApi.getUserPictureById(tmpUser.photo_big));
            //Log.i("user image","" + myApi.getUserImage(22457823));
            VkName.setText(tmpUser.first_name + " " + tmpUser.last_name);
            //Log.i("user image","" + myApi.getUserName(22457823));
        }
        catch (IOException i) {
            i.printStackTrace();
        }
        catch (JSONException j) {
            j.printStackTrace();
        }
        catch (KException k) {
            k.printStackTrace();
        }

    }
}