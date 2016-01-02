package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.database.model.ApiObject;

/**
 * Helper for working with News repository
 */
public class ApiObjectHelper implements BaseColumns {

    public static final String COLUMN_USER = "user";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_MODIFIED = "modified";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_COMMENT_STATUS = "comment_status";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_POST_NAME = "post_name";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PARENT = "parent";
    public static final String COLUMN_META_TITLE = "meta_title";
    public static final String COLUMN_META_DESCRIPTION = "meta_description";
    public static final String COLUMN_META_KEYWORDS = "meta_keywords";
    public static final String COLUMN_DISPLAY_METHOD = "display_method";
    public static final String COLUMN_RSS = "rss";
    public static final String COLUMN_FILES = "files";
    public static final String COLUMN_THUMB = "thumb";
    public static final String COLUMN_LANG = "lang";
    public static final String COLUMN_LANG_ID = "lang_id";
    public static final String COLUMN_REFERENCES_INCLUDE = "references_include";
    public static final String COLUMN_GALLERY_INCLUDE = "gallery_include";
    public static final String COLUMN_TABLES = "tables";
    public static final String COLUMN_PARSERS_INCLUDE = "parsers_include";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String COLUMN_OBJ_TYPE = "objType";


    public static String DATABASE_TABLE = "objects";
    public static String[] COLS;
    public static String ALL_COLUMNS;

    static {
        COLS = new String[]{
                _ID,
                COLUMN_USER,
                COLUMN_DATE,
                COLUMN_MODIFIED,
                COLUMN_CONTENT,
                COLUMN_TITLE,
                COLUMN_STATUS,
                COLUMN_COMMENT_STATUS,
                COLUMN_PASSWORD,
                COLUMN_POST_NAME,
                COLUMN_LINK,
                COLUMN_TYPE,
                COLUMN_PARENT,
                COLUMN_META_TITLE,
                COLUMN_META_DESCRIPTION,
                COLUMN_META_KEYWORDS,
                COLUMN_DISPLAY_METHOD,
                COLUMN_RSS,
                COLUMN_FILES,
                COLUMN_THUMB,
                COLUMN_LANG,
                COLUMN_LANG_ID,
                COLUMN_REFERENCES_INCLUDE,
                COLUMN_GALLERY_INCLUDE,
                COLUMN_TABLES,
                COLUMN_PARSERS_INCLUDE,
                COLUMN_LOGIN,
                COLUMN_UPDATE_TIME,
                COLUMN_OBJ_TYPE
        };
        ALL_COLUMNS = TextUtils.join(",", COLS);
    }

    private final String TAG = "ObjectsStore";

    protected DatabaseOpenHelper doh;

    public ApiObjectHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }

    public ApiObjectCursor getAll() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase d = doh.getReadableDatabase();
        ApiObjectCursor c = (ApiObjectCursor) d.rawQueryWithFactory(
                new ApiObjectCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public ApiObjectCursor getAllByType(int apiObjectType)
    {
        Log.v(TAG, "getAllByType ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE " + COLUMN_OBJ_TYPE + "=?  ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase d = doh.getReadableDatabase();
        ApiObjectCursor c = (ApiObjectCursor) d.rawQueryWithFactory(
                new ApiObjectCursor.Factory(),
                sql,
                new String[]{String.valueOf(apiObjectType)},
                null);
        c.moveToFirst();
        return c;

    }

    public ApiObject get(long id) {
        Log.d(TAG, "get (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE _id = ?";
        SQLiteDatabase d = doh.getReadableDatabase();
        ApiObjectCursor c = (ApiObjectCursor) d.rawQueryWithFactory(
                new ApiObjectCursor.Factory(),
                sql,
                new String[]{String.valueOf(id)},
                null);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        return c.getItem();
    }

    public boolean clear() {
        Log.d(TAG, "clear()");
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.delete(DATABASE_TABLE, null, null);
            Log.d(TAG, "success");
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error clearing self location table");
            return false;
        }
    }

    public boolean add(ApiObject apiObject) {

        delete(apiObject.id,apiObject.objType);

        Log.d(TAG, "add(" + apiObject.toString() + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, apiObject.id);
        map.put(COLUMN_USER, apiObject.user);
        map.put(COLUMN_DATE, apiObject.date);
        map.put(COLUMN_MODIFIED, apiObject.modified);
        map.put(COLUMN_CONTENT, apiObject.content);
        map.put(COLUMN_TITLE, apiObject.title);
        map.put(COLUMN_STATUS, apiObject.status);
        map.put(COLUMN_COMMENT_STATUS, apiObject.comment_status);
        map.put(COLUMN_PASSWORD, apiObject.password);
        map.put(COLUMN_POST_NAME, apiObject.post_name);
        map.put(COLUMN_LINK, apiObject.link);
        map.put(COLUMN_TYPE, apiObject.type);
        map.put(COLUMN_PARENT, apiObject.parent);
        map.put(COLUMN_META_TITLE, apiObject.meta_title);
        map.put(COLUMN_META_DESCRIPTION, apiObject.meta_description);
        map.put(COLUMN_META_KEYWORDS, apiObject.meta_keywords);
        map.put(COLUMN_DISPLAY_METHOD, apiObject.display_method);
        map.put(COLUMN_RSS, apiObject.rss);
        map.put(COLUMN_FILES, apiObject.files);
        map.put(COLUMN_THUMB, apiObject.thumb);
        map.put(COLUMN_LANG, apiObject.lang);
        map.put(COLUMN_LANG_ID, apiObject.lang_id);
        map.put(COLUMN_REFERENCES_INCLUDE, apiObject.references_include);
        map.put(COLUMN_GALLERY_INCLUDE, apiObject.gallery_include);
        map.put(COLUMN_TABLES, apiObject.tables);
        map.put(COLUMN_PARSERS_INCLUDE, apiObject.parsers_include);
        map.put(COLUMN_LOGIN, apiObject.login);
        map.put(COLUMN_UPDATE_TIME, apiObject.update_time);
        map.put(COLUMN_OBJ_TYPE,apiObject.objType);

        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            return (db.insert(DATABASE_TABLE, null, map)!=-1);

        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public void delete(long id) {
        Log.d(TAG, "delete (" + id + ")");
        try {
            Log.d(TAG, "Delete self location: " + id);
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {Long.toString(id)};
            db.delete(DATABASE_TABLE, _ID + "=?", args);
        } catch (SQLException ex) {
            Log.e(TAG, "Error delete self location", ex);
        }
    }

    public void delete(long id,int objApiType) {
        Log.d(TAG, "delete (" + id + ")");
        try {
            Log.d(TAG, "Delete self location: " + id);
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {Long.toString(id),Integer.toString(objApiType)};
            db.delete(DATABASE_TABLE, _ID + "=? and objType=?", args);
        } catch (SQLException ex) {
            Log.e(TAG, "Error delete self location", ex);
        }
    }
}
