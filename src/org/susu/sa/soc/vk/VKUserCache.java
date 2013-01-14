package org.susu.sa.soc.vk;

import com.perm.kate.api.Api;
import com.perm.kate.api.KException;
import com.perm.kate.api.User;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class VKUserCache {

    private ArrayList<Long> toReceive = new ArrayList<>();
    private HashMap<Long, User> cache = new HashMap<>();
    private Api api;

    public VKUserCache(Api api) {
        this.api = api;
    }

    public User get(long id) {
        return (!cache.containsKey(id)) ? null : cache.get(id);
    }

    public void add(long id) {
        if (!cache.containsKey(id)) toReceive.add(id);
    }

    public void update() throws KException, IOException, JSONException {
        if (toReceive.size() <= 0) return;
        for (User profile: api.getProfiles(toReceive, null, null, null))
            cache.put(profile.uid, profile);
        toReceive.clear();
    }
}
