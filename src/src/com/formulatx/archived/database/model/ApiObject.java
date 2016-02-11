package com.formulatx.archived.database.model;

import com.formulatx.archived.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ApiObject implements Serializable{

    public static final String OTHER = "";
    public static final String NEWS = "post";
    public static final String GAMER = "gamer";
    public static final String OBJECT = "object";
    public static final String PRODUCT = "product";
    public static final String PARTNER = "partner";
    public static final String CARD = "card";
    public static final String AREA = "area";
    public long id;
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

    public ApiObject(long id) {
        this.id = id;
    }

    public ApiObject(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getLong("id");
        user = Utils.getStringOrNull(jsonObject, "user");
        date = Utils.getStringOrNull(jsonObject, "date");
        modified = Utils.getStringOrNull(jsonObject, "modified");
        content = Utils.getStringOrNull(jsonObject, "content");
        title = Utils.getStringOrNull(jsonObject, "title");
        status = Utils.getStringOrNull(jsonObject, "status");
        comment_status = Utils.getStringOrNull(jsonObject, "comment_status");
        password = Utils.getStringOrNull(jsonObject, "password");
        post_name = Utils.getStringOrNull(jsonObject, "post_name");
        link = Utils.getStringOrNull(jsonObject, "link");
        type = Utils.getStringOrNull(jsonObject, "type");
        parent = Utils.getStringOrNull(jsonObject, "parent");
        meta_title = Utils.getStringOrNull(jsonObject, "meta_title");
        meta_description = Utils.getStringOrNull(jsonObject, "meta_description");
        meta_keywords = Utils.getStringOrNull(jsonObject, "meta_keywords");
        display_method = Utils.getStringOrNull(jsonObject, "display_method");
        rss = Utils.getStringOrNull(jsonObject, "rss");
        files = Utils.getStringOrNull(jsonObject, "files");
        thumb = Utils.getStringOrNull(jsonObject, "thumb");
        lang = Utils.getStringOrNull(jsonObject, "lang");
        lang_id = Utils.getStringOrNull(jsonObject, "lang_id");
        references_include = Utils.getStringOrNull(jsonObject, "references_include");
        gallery_include = Utils.getStringOrNull(jsonObject, "gallery_include");
        tables = Utils.getStringOrNull(jsonObject, "tables");
        parsers_include = Utils.getStringOrNull(jsonObject, "parsers_include");
        login = Utils.getStringOrNull(jsonObject, "login");
    }

    @Override
    public String toString() {return ApiObject.class.getSimpleName() + ":" + id;}

}
