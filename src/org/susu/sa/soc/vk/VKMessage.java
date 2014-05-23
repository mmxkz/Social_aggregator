package org.susu.sa.soc.vk;
import android.graphics.Bitmap;
import android.util.Log;
import org.susu.sa.soc.MessageABS;

import java.util.ArrayList;

/**
 * Created by Andrey on 04.05.14.
 */
public class VKMessage extends MessageABS {
        public final static String LOG_KEY = "VK::VKMessage";

        private final VKSource source;
        private long id;

        public VKMessage(VKSource source, long id, String sender, String message, Long date, Bitmap bitmap, Long owner_id) {
            super(sender, message, date, bitmap, null, owner_id);
            this.source = source;
            this.id = id;
        }

        @Override
        public void reply(String body, Long cid) throws Exception {
//            getSource().reply(this, body, cid);
        }

//       @Override
        public ArrayList<MessageABS> getDialogs(int count, int offset) throws Exception {
            Log.e("Dialog: ", "get dialogs in VKMessage");
            return getSource().getDialogs( count, offset);
        }

        public VKSource getSource() {
            return source;
        }

        public long getMessageId() {
            return id;
        }

        public String toString() {
            return Long.toString(id);
        }
}

