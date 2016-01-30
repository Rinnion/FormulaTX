package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.CommentCursor;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.cursor.TwitterItemCursor;
import com.rinnion.archived.database.model.Comment;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.database.model.TwitterItem;


/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.02.14
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class TwitterHelper implements BaseColumns {
    //<item>"CREATE TABLE twitter_cache (_id INTEGER PRIMARY KEY, reference_id INTEGER, text TEXT, link TEXT, date TEXT );"</item>

    public static final String COLUMN_API_OBJECT_ID = "api_object_id";
    public static final String COLUMN_API_OBJECT_REFERENCE_ID = "reference_id_link";
    public static final String COLUMN_API_OBJECT_REFERENCE_TYPE = "reference_type";

    public static final String COLUMN_REFERENCE_ID = "reference_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_DATE = "date";
    public static final String ALIAS_API_OBJECT_TITLE = "title";

    public static final String TYPE = "twitter.com";
    public static String DATABASE_TABLE = "twitter_cache";
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

    public TwitterHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }

    public TwitterItemCursor getAllItems() {
        Log.v(TAG, "getAllItems ()");

        String allColumns = "t." +TextUtils.join(",t.", COLS);

        String sql = "SELECT " + allColumns + ", ao." + ApiObjectHelper.COLUMN_TITLE + " AS " + ALIAS_API_OBJECT_TITLE +
                " FROM " + DATABASE_TABLE + " t" +
                " LEFT JOIN " + DATABASE_TABLE_API_OBJECT_LINK + " AS aol ON aol." + COLUMN_API_OBJECT_REFERENCE_ID + "=t." +COLUMN_REFERENCE_ID +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=aol." + COLUMN_API_OBJECT_ID +
                " WHERE aol."+COLUMN_API_OBJECT_REFERENCE_TYPE+"=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        TwitterItemCursor c = (TwitterItemCursor) d.rawQueryWithFactory(
                new TwitterItemCursor.Factory(),
                sql,
                new String[]{TYPE},
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

    public boolean merge(TwitterItem item){
        if (isItemPresent(item.id)) deleteItem(item.id);
        return add(item);
    }


    public boolean add(TwitterItem item) {
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

    public Cursor getAttachedReference(long api_object_id){
        Log.d(TAG, "getAttachedTwitter (" + api_object_id + ")");

        String sql = "SELECT " + COLUMN_API_OBJECT_ID + ", " + COLUMN_API_OBJECT_REFERENCE_ID +
                " FROM " + DATABASE_TABLE_API_OBJECT_LINK +
                " WHERE " + COLUMN_API_OBJECT_REFERENCE_TYPE + "=?";
        SQLiteDatabase d = doh.getReadableDatabase();
        CommentCursor c = (CommentCursor) d.rawQueryWithFactory(
                new CommentCursor.Factory(),
                sql,
                new String[] {TYPE},
                null);
        c.moveToFirst();
        return c;
    }

    public boolean attachReference(long api_object_id, long reference_id){
        Log.d(TAG, "attachReference(api_object:" + String.valueOf(api_object_id) + ", reference_id:"+String.valueOf(reference_id) + ")");

        detachReference(api_object_id, reference_id);

        ContentValues map;
        map = new ContentValues();
        map.put(COLUMN_API_OBJECT_ID, api_object_id);
        map.put(COLUMN_API_OBJECT_REFERENCE_ID, reference_id);
        map.put(COLUMN_API_OBJECT_REFERENCE_TYPE, TYPE);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            long insert = db.insert(DATABASE_TABLE_API_OBJECT_LINK, null, map);
            return insert != -1;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public void detachReference(long api_object_id, long reference_id){
        Log.d(TAG, "detachReference(api_object:" + String.valueOf(api_object_id) + ", reference_id:"+String.valueOf(reference_id) + ")");
        try {
            Log.d(TAG, "Delete reference " + String.valueOf(reference_id)+ " from apiObject: " + String.valueOf(api_object_id));
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {String.valueOf(api_object_id), String.valueOf(reference_id), TYPE};
            db.delete(DATABASE_TABLE_API_OBJECT_LINK,
                    COLUMN_API_OBJECT_ID + "=? AND " + COLUMN_API_OBJECT_REFERENCE_ID + "=? AND " + COLUMN_API_OBJECT_REFERENCE_TYPE + "=?",
                    args);
        } catch (SQLException ex) {
            Log.e(TAG, "Error detaching reference", ex);
        }
    }

    public void detachAllReferences(long api_object_id){
        Log.d(TAG, "detachReference(api_object:" + String.valueOf(api_object_id) + ")");
        try {
            Log.d(TAG, "Delete ALL references from apiObject: " + String.valueOf(api_object_id));
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {String.valueOf(api_object_id), TYPE};
            db.delete(DATABASE_TABLE_API_OBJECT_LINK,
                    COLUMN_API_OBJECT_ID + "=? AND " + COLUMN_API_OBJECT_REFERENCE_TYPE + "=?",
                    args);
        } catch (SQLException ex) {
            Log.e(TAG, "Error detaching reference", ex);
        }
    }
}
