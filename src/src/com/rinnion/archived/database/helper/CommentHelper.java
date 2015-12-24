package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.CommentCursor;
import com.rinnion.archived.database.model.Comment;


/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.02.14
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class CommentHelper implements BaseColumns {
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_DATE_POST = "date_post";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_DATE_RECEIVE = "date_receive";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_AVATAR = "user_avatar";
    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static String DATABASE_TABLE = "comments";
    public static String[] COLS;
    public static String ALL_COLUMNS;

    static {
        COLS = new String[]{
                _ID,
                COLUMN_CONTENT,
                COLUMN_DATE_POST,
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_AVATAR,
                COLUMN_DATE_RECEIVE,
                COLUMN_MESSAGE_ID
        };
        ALL_COLUMNS = TextUtils.join(",", COLS);
    }

    private final String TAG = "CommentHelper";

    private DatabaseOpenHelper doh;

    public CommentHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }

    public CommentCursor getAll() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " ORDER BY " + COLUMN_DATE_POST + " ASC";

        SQLiteDatabase d = doh.getReadableDatabase();
        CommentCursor c = (CommentCursor) d.rawQueryWithFactory(
                new CommentCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public CommentCursor getAllForMessage(long messageId) {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE " + COLUMN_MESSAGE_ID + "=? ORDER BY " + COLUMN_DATE_POST + " ASC";

        SQLiteDatabase d = doh.getReadableDatabase();
        CommentCursor c = (CommentCursor) d.rawQueryWithFactory(
                new CommentCursor.Factory(),
                sql,
                new String[]{String.valueOf(messageId)},
                null);
        c.moveToFirst();
        return c;
    }

    public Comment get(int id) {
        Log.d(TAG, "getLocation (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE _id = ?";
        SQLiteDatabase d = doh.getReadableDatabase();
        CommentCursor c = (CommentCursor) d.rawQueryWithFactory(
                new CommentCursor.Factory(),
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

    public boolean add(Comment comment) {
        Log.d(TAG, "addLocation(" + String.valueOf(comment) + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, comment.id);
        map.put(COLUMN_CONTENT, comment.content);
        map.put(COLUMN_DATE_POST, comment.date_post);
        map.put(COLUMN_USER_ID, comment.user_id);
        map.put(COLUMN_USER_NAME, comment.user_id);
        map.put(COLUMN_USER_AVATAR, comment.user_id);
        map.put(COLUMN_DATE_RECEIVE, comment.date_receive);
        map.put(COLUMN_MESSAGE_ID, comment.message_id);
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
