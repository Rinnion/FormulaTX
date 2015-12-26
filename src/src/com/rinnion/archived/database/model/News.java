package com.rinnion.archived.database.model;

public class News {

    public final long id;
    public final String caption;
    public final String type;
    public final String date;
    public final String name;
    public final String content;
    public final String thumb;

    public News(long id, String caption, String type, String date, String name, String content, String thumb) {
        this.id = id;
        this.caption = caption;
        this.type = type;
        this.date = date;
        this.name = name;
        this.content = content;
        this.thumb = thumb;
    }
}
