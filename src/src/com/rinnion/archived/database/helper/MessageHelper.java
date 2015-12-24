package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.MessageCursor;
import com.rinnion.archived.database.model.Message;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.02.14
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class MessageHelper implements BaseColumns {
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_BACKGROUND = "background";
    public static final String COLUMN_DATE_POST = "date_post";
    public static final String COLUMN_TAGS = "tags";
    public static final String COLUMN_LIKES = "likes";
    public static final String COLUMN_LIKE = "like";
    public static final String COLUMN_DATE_RECEIVE = "date_receive";
    public static final String COLUMN_COMMENTS = "comments";
    public static String DATABASE_TABLE = "messages";
    public static String[] COLS;
    public static String ALL_COLUMNS;

    static {
        COLS = new String[]{
                _ID,
                COLUMN_CONTENT,
                COLUMN_BACKGROUND,
                COLUMN_DATE_POST,
                COLUMN_TAGS,
                COLUMN_LIKES,
                COLUMN_LIKE,
                COLUMN_COMMENTS,
                COLUMN_DATE_RECEIVE
        };
        ALL_COLUMNS = TextUtils.join(",", COLS);
    }

    private final String TAG = "MessageHelper";

    private DatabaseOpenHelper doh;

    public MessageHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }

    public MessageCursor getAll() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " ORDER BY " + COLUMN_DATE_POST + " DESC";

        SQLiteDatabase d = doh.getReadableDatabase();
        MessageCursor c = (MessageCursor) d.rawQueryWithFactory(
                new MessageCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public Message get(int id) {
        Log.d(TAG, "getLocation (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE _id = ?";
        SQLiteDatabase d = doh.getReadableDatabase();
        MessageCursor c = (MessageCursor) d.rawQueryWithFactory(
                new MessageCursor.Factory(),
                sql,
                new String[]{Integer.toString(id)},
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

    public boolean add(Message message) {
        Log.d(TAG, "addLocation(" + message.toString() + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, message.id);
        map.put(COLUMN_CONTENT, message.content);
        map.put(COLUMN_BACKGROUND, message.background);
        map.put(COLUMN_DATE_POST, message.date_post);
        map.put(COLUMN_LIKES, message.likes);
        map.put(COLUMN_LIKE, message.like);
        map.put(COLUMN_COMMENTS, message.comments);
        map.put(COLUMN_TAGS, message.tags);
        map.put(COLUMN_DATE_RECEIVE, message.date_receive);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.insert(DATABASE_TABLE, null, map);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public void delete(int id) {
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
}
