package com.rinnion.archived.database.model;

import java.io.Serializable;

public class User implements Serializable {
    public final long id;
    public final Profile profile;
    public final String name;
    public final String avatar;
    public final String token;
    public final String message_token;
    public final long date_receive;
    private final String TAG = User.class.getSimpleName();

    public User(long id, String name, String avatar, long date_receive) {
        this(id, name, avatar, date_receive, null, null);
    }

    public User(long id, String name, String avatar, long date_receive, String token, String message_token) {
        this.id = id;
        this.profile = new Profile(name, avatar);
        this.name = name;
        this.avatar = avatar;
        this.date_receive = date_receive;
        this.token = token;
        this.message_token = message_token;
    }
}

