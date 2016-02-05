package com.formulatx.archived.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.CommentCursor;
import com.formulatx.archived.database.cursor.InstagramItemCursor;
import com.formulatx.archived.database.cursor.TwitterItemCursor;
import com.formulatx.archived.database.model.InstagramItem;
import com.formulatx.archived.database.model.TwitterItem;
import com.formulatx.archived.utils.Log;


/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.02.14
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class InstagramHelper implements BaseColumns {
    //<item>"CREATE TABLE twitter_cache (_id INTEGER PRIMARY KEY, reference_id INTEGER, text TEXT, link TEXT, date TEXT );"</item>

    public static final String COLUMN_API_OBJECT_ID = "api_object_id";
    public static final String COLUMN_API_OBJECT_REFERENCE_ID = "reference_id_link";
    public static final String COLUMN_API_OBJECT_REFERENCE_TYPE = "reference_type";

    public static final String COLUMN_REFERENCE_ID = "reference_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_DATE = "date";

    public static String DATABASE_TABLE = "instagram_cache";
    public static String DATABASE_TABLE_API_OBJECT_LINK = "api_object_reference";

    public static String[] COLS;
    public static String ALL_COLUMNS;

    static {
        COLS = new String[]{
                _ID,
                COLUMN_REFERENCE_ID,
                COLUMN_TEXT,
                COLUMN_LINK,
                COLUMN_DATE
        };
        ALL_COLUMNS = TextUtils.join(",", COLS);
    }

    private final String TAG = "CommentHelper";

    private DatabaseOpenHelper doh;

    public InstagramHelper() {
        this.doh = FormulaTXApplication.getDatabaseOpenHelper();
    }


    public InstagramItemCursor getAllItems() {
        Log.v(TAG, "getAllItems ()");

        String sql = "SELECT " + ALL_COLUMNS +
                " FROM " + DATABASE_TABLE + "";

        SQLiteDatabase d = doh.getReadableDatabase();
        InstagramItemCursor c = (InstagramItemCursor) d.rawQueryWithFactory(
                new InstagramItemCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public boolean isItemPresent(long id) {
        Log.d(TAG, "isItemPresent (" + id + ")");
        TwitterItemCursor c = searchItem(id);
        return c.getCount() == 1;
    }

    private TwitterItemCursor searchItem(long id) {
        Log.d(TAG, "searchItem (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE _id = ?";
        SQLiteDatabase d = doh.getReadableDatabase();
        return (TwitterItemCursor) d.rawQueryWithFactory(
                new TwitterItemCursor.Factory(),
                sql,
                new String[]{String.valueOf(id)},
                null);
    }

    public void deleteItem(long id){
        Log.d(TAG, "deleteItem(" + String.valueOf(id) + ")");
        try {
            Log.d(TAG, "Delete item " + String.valueOf(id) + " from gallery");
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {String.valueOf(id)};
            db.delete(DATABASE_TABLE,
                    _ID + "=?",
                    args);
        } catch (SQLException ex) {
            Log.e(TAG, "Error deleting item", ex);
        }
    }

    public boolean merge(InstagramItem item){
        if (isItemPresent(item.id)) deleteItem(item.id);
        return add(item);
    }


    public boolean add(InstagramItem item) {
        Log.d(TAG, "add(" + String.valueOf(item) + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, item.id);
        map.put(COLUMN_REFERENCE_ID, item.reference_id);
        map.put(COLUMN_TEXT, item.text);
        map.put(COLUMN_LINK, item.link);
        map.put(COLUMN_DATE, item.date);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.insert(DATABASE_TABLE, null, map);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }
}
