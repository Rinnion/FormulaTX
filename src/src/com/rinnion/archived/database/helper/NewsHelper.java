package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.model.Message;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.02.14
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class NewsHelper implements BaseColumns {
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CAPTION = "caption";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_THUMBS = "thumbs";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CONTENT = "content";
    public static String DATABASE_TABLE = "news";
    public static String[] COLS;
    public static String ALL_COLUMNS;

    static {
        COLS = new String[]{
                _ID,
                COLUMN_TYPE,
                COLUMN_CAPTION,
                COLUMN_NAME,
                COLUMN_THUMBS,
                COLUMN_DATE,
                COLUMN_CONTENT,
        };
        ALL_COLUMNS = TextUtils.join(",", COLS);
    }

    private final String TAG = "MessageHelper";

    private DatabaseOpenHelper doh;

    public NewsHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }

    public NewsCursor getAll() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase d = doh.getReadableDatabase();
        NewsCursor c = (NewsCursor) d.rawQueryWithFactory(
                new NewsCursor.Factory(),
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
        NewsCursor c = (NewsCursor) d.rawQueryWithFactory(
                new NewsCursor.Factory(),
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
        map.put(COLUMN_CAPTION, message.caption);
        map.put(COLUMN_TYPE, message.type);
        map.put(COLUMN_DATE, message.date);
        map.put(COLUMN_NAME, message.name);
        map.put(COLUMN_CONTENT, message.content);
        map.put(COLUMN_THUMBS, message.thumb);
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
