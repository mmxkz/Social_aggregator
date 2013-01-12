package org.susu.sa.soc;

import java.util.Date;

public interface IPost {
    String getSender();
    String getBody();
    Date getDate();

    void reply(String body) throws Exception;
}
