package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.cursor.ParserCursor;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.database.model.Parser;
import com.rinnion.archived.utils.Log;


/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.02.14
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class ParserHelper implements BaseColumns {
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_SYSTEM = "system";

    public static String DATABASE_TABLE = "parsers";
    public static String[] COLS;
    public static String ALL_COLUMNS;

    static {
        COLS = new String[]{
                _ID,
                COLUMN_TITLE,
                COLUMN_DATE,
                COLUMN_DATA,
                COLUMN_SYSTEM,
        };
        ALL_COLUMNS = TextUtils.join(",", COLS);
    }

    private final String TAG = "CommentHelper";

    private DatabaseOpenHelper doh;

    public ParserHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }

    public ParserCursor getAll() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " ORDER BY _id";

        SQLiteDatabase d = doh.getReadableDatabase();
        ParserCursor c = (ParserCursor) d.rawQueryWithFactory(
                new ParserCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public ParserCursor getAllWithType(int[] range, String type) {
        Log.v(TAG, "getAllGalleries ()");

        if (range == null) range = new int[0];
        StringBuilder sb = new StringBuilder();
        for (int i : range) {
            ((sb.length() > 0) ? sb.append(",") : sb).append(i);
        }
        String in = sb.toString();

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE +
                " WHERE " + _ID + " in (" + in + ") AND " + COLUMN_SYSTEM + "=? " +
                " ORDER BY _id";

        SQLiteDatabase d = doh.getReadableDatabase();
        ParserCursor c = (ParserCursor) d.rawQueryWithFactory(
                new ParserCursor.Factory(),
                sql,
                new String[]{type},
                null);
        c.moveToFirst();
        return c;
    }


    public ParserCursor getAll(int[] range) {
        Log.v(TAG, "getAllGalleries ()");

        if (range == null) range = new int[0];
        StringBuilder sb = new StringBuilder();
        for (int i : range) {
            ((sb.length() > 0) ? sb.append(",") : sb).append(i);
        }
        String in = sb.toString();

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE +
                " WHERE " + _ID + " in (" + in + ") " +
                " ORDER BY _id";

        SQLiteDatabase d = doh.getReadableDatabase();
        ParserCursor c = (ParserCursor) d.rawQueryWithFactory(
                new ParserCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public Parser get(int id) {
        Log.d(TAG, "getLocation (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE _id = ?";
        SQLiteDatabase d = doh.getReadableDatabase();
        ParserCursor c = (ParserCursor) d.rawQueryWithFactory(
                new ParserCursor.Factory(),
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


    public boolean isItemPresent(long id) {
        Log.d(TAG, "isItemPresent (" + id + ")");
        ParserCursor c = searchItem(id);
        return c.getCount() == 1;
    }

    private ParserCursor searchItem(long id) {
        Log.d(TAG, "searchItem (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE _id = ?";
        SQLiteDatabase d = doh.getReadableDatabase();
        return (ParserCursor) d.rawQueryWithFactory(
                new ParserCursor.Factory(),
                sql,
                new String[]{String.valueOf(id)},
                null);
    }

    public boolean merge(Parser item){
        if (isItemPresent(item.id)) delete(item.id);
        return add(item);
    }

    public boolean add(Parser comment) {
        Log.d(TAG, "addLocation(" + String.valueOf(comment) + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, comment.id);
        map.put(COLUMN_TITLE, comment.title);
        map.put(COLUMN_DATE, comment.date);
        map.put(COLUMN_DATA, comment.data);
        map.put(COLUMN_SYSTEM, comment.system);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.insert(DATABASE_TABLE, null, map);
            return true;
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
            String[] args = {String.valueOf(id)};
            db.delete(DATABASE_TABLE, _ID + "=?", args);
        } catch (SQLException ex) {
            Log.e(TAG, "Error delete self location", ex);
        }
    }
}
