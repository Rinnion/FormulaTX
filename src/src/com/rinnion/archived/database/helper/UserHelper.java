package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.model.ApiObjects.News;
import com.rinnion.archived.database.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.02.14
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class UserHelper implements BaseColumns {
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE_RECEIVE = "date_receive";
    public static final String COLUMN_AVATAR = "avatar";
    public static String DATABASE_TABLE = "users";
    public static String[] COLS;
    public static String ALL_COLUMNS;

    static {
        COLS = new String[]{
                _ID,
                COLUMN_NAME,
                COLUMN_DATE_RECEIVE
        };
        ALL_COLUMNS = TextUtils.join(",", COLS);
    }

    private final String TAG = "UserHelper";

    private DatabaseOpenHelper doh;

    public UserHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }

    public NewsCursor getAll() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " ORDER BY " + COLUMN_NAME + " DESC";

        SQLiteDatabase d = doh.getReadableDatabase();
        NewsCursor c = (NewsCursor) d.rawQueryWithFactory(
                new NewsCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public News get(int id) {
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
            Log.e(TAG, "success");
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error clearing self location table");
            return false;
        }
    }

    public boolean add(User user) {
        Log.d(TAG, "addLocation(" + String.valueOf(user) + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, user.id);
        map.put(COLUMN_NAME, user.name);
        map.put(COLUMN_DATE_RECEIVE, user.date_receive);
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
