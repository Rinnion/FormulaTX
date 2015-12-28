package com.rinnion.archived.database.model;

public class News {

    public final long id;
    public final String parent;
    public final String date;
    public final String name;
    public final String content;
    public final String thumbs;

    public News(long id, String parent, String name, String content, String thumbs, String date) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.content = content;
        this.thumbs = thumbs;
        this.date = date;
    }
}
