package com.rinnion.archived.database.model;

import java.io.Serializable;

public class Comment implements Serializable {
    public final long id;
    public final String content;
    public final long date_post;
    public final long user_id;
    public final String user_avatar;
    public final String user_name;
    private final String TAG = Comment.class.getSimpleName();
    public long date_receive;
    public long message_id;

    public Comment(long id, String content, long date_post, long user_id, long date_receive, String user_avatar, String user_name, long message_id) {
        this.id = id;
        this.content = content;
        this.date_post = date_post;
        this.user_id = user_id;
        this.user_avatar = user_avatar;
        this.user_name = user_name;
        this.date_receive = date_receive;
        this.message_id = message_id;
    }


}
