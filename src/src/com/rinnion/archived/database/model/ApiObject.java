package com.rinnion.archived.database.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public ApiObject(JSONObject jsonObject,int objType) throws JSONException {
        this.objType=objType;
        id=jsonObject.getLong("id");
        user=jsonObject.getString("user");
        date=jsonObject.getString("date");
        modified=jsonObject.getString("modified");
        content=jsonObject.getString("content");
        title=jsonObject.getString("title");
        status=jsonObject.getString("status");
        comment_status=jsonObject.getString("comment_status");
        password=jsonObject.getString("password");
        post_name=jsonObject.getString("post_name");
        link=jsonObject.getString("link");
        type=jsonObject.getString("type");
        parent=jsonObject.getString("parent");
        meta_title=jsonObject.getString("meta_title");
        meta_description=jsonObject.getString("meta_description");
        meta_keywords=jsonObject.getString("meta_keywords");
        display_method=jsonObject.getString("display_method");
        rss=jsonObject.getString("rss");
        files=jsonObject.getString("files");
        thumb=jsonObject.getString("thumb");
        lang=jsonObject.getString("lang");
        lang_id=jsonObject.getString("lang_id");
        references_include=jsonObject.getString("references_include");
        gallery_include=jsonObject.getString("gallery_include");
        tables=jsonObject.getString("tables");
        parsers_include=jsonObject.getString("parsers_include");
        login=jsonObject.getString("login");




    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }

    public void Parse(JSONObject jsonObject)
    {

    }





}
