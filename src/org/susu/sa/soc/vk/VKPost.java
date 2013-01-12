package org.susu.sa.soc.vk;

import com.perm.kate.api.WallMessage;
import org.susu.sa.soc.IPost;

import java.util.Date;

public class VKPost implements IPost {

    private final VKSource source;
    private final WallMessage message;
    private String sender;
    private Date date;

    public VKPost(VKSource source, WallMessage message) {
        this.source = source;
        this.message = message;
        this.sender = source.getNameById(message.from_id);
        this.date = new Date(message.date);
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public String getBody() {
        return message.text;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void reply(String body) throws Exception {
        source.reply(this, body);
    }

    public Long getPostId() {
        return this.message.id;
    }
}
