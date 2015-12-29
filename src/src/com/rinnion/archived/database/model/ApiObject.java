package com.rinnion.archived.database.model;


public class ApiObject {




    public final long id;
    public String user;
    public String date;
    public String modified;
    public String content;
    public String title;
    public String status;
    public String comment_status;
    public String password;
    public String post_name;
    public String link;
    public String type;
    public String parent;
    public String meta_title;
    public String meta_description;
    public String meta_keywords;
    public String display_method;
    public String rss;
    public String files;
    public String thumb;
    public String lang;
    public String lang_id;
    public String references_include;
    public String gallery_include;
    public String tables;
    public String parsers_include;
    public String login;
    public long update_time;
    public int objType;

    public ApiObject(long id,int objType) {
        this.id = id;
        this.objType=objType;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }
}
