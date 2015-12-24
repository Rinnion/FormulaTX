package com.rinnion.archived.database.model;

public class Message {

    public final String content;
    public final String background;
    public final long date_post;
    public final String tags;
    public final long date_receive;
    public final Long comments;
    private final String TAG = getClass().getSimpleName();
    public long id;
    public long likes;
    public boolean like;

    public Message(long id, String content, String background, long date_post, long likes, boolean vote, Long comments, String tags, long date_receive) {
        this.id = id;
        this.content = content;
        this.background = background;
        this.date_post = date_post;
        this.likes = likes;
        this.like = vote;
        this.comments = comments;
        this.tags = tags;
        this.date_receive = date_receive;
    }
}
