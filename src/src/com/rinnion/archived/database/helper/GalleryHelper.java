package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.CommentCursor;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.model.Comment;
import com.rinnion.archived.database.model.GalleryItem;


/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.02.14
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class GalleryHelper implements BaseColumns {
    public static final String COLUMN_API_OBJECT_ID = "api_object_id";
    public static final String COLUMN_GALLERY_ID = "gallery_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_URL = "url";
    public static String DATABASE_TABLE = "gallery";
    public static String DATABASE_TABLE_API_OBJECT = "api_object_gallery";

    public static String[] COLS;
    public static String ALL_COLUMNS;

    static {
        COLS = new String[]{
                _ID,
                COLUMN_GALLERY_ID,
                COLUMN_TYPE,
                COLUMN_URL
        };
        ALL_COLUMNS = TextUtils.join(",", COLS);
    }

    private final String TAG = "CommentHelper";

    private DatabaseOpenHelper doh;

    public GalleryHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }

    public CommentCursor getAllItems() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE;

        SQLiteDatabase d = doh.getReadableDatabase();
        CommentCursor c = (CommentCursor) d.rawQueryWithFactory(
                new CommentCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public CommentCursor getGallery(long gallery_id) {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE " + COLUMN_GALLERY_ID + "=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        CommentCursor c = (CommentCursor) d.rawQueryWithFactory(
                new CommentCursor.Factory(),
                sql,
                new String[] {String.valueOf(gallery_id)},
                null);
        c.moveToFirst();
        return c;
    }

    public CommentCursor getAllByApiObjectId(long api_object_id) {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS +
                " FROM " + DATABASE_TABLE +
                " LEFT JOIN " + DATABASE_TABLE_API_OBJECT + " ao ON ao._id="+COLUMN_GALLERY_ID +
                " WHERE " + COLUMN_API_OBJECT_ID + "=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        CommentCursor c = (CommentCursor) d.rawQueryWithFactory(
                new CommentCursor.Factory(),
                sql,
                new String[]{String.valueOf(api_object_id)},
                null);
        c.moveToFirst();
        return c;
    }

    public GalleryItemCursor getAllByApiObjectAndItemTypeId(long api_object_id, String item_type) {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS +
                " FROM " + DATABASE_TABLE +
                " LEFT JOIN " + DATABASE_TABLE_API_OBJECT + " ao ON ao._id="+COLUMN_GALLERY_ID +
                " WHERE " + COLUMN_API_OBJECT_ID + "=? AND " + COLUMN_TYPE + "=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryItemCursor c = (GalleryItemCursor) d.rawQueryWithFactory(
                new GalleryItemCursor.Factory(),
                sql,
                new String[]{String.valueOf(api_object_id), String.valueOf(item_type)},
                null);
        c.moveToFirst();
        return c;
    }

    public Comment getItem(int id) {
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

    public boolean add(GalleryItem item) {
        Log.d(TAG, "addLocation(" + String.valueOf(item) + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, item.id);
        map.put(COLUMN_GALLERY_ID, item.gallery_id);
        map.put(COLUMN_TYPE, item.type);
        map.put(COLUMN_URL, item.url);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.insert(DATABASE_TABLE, null, map);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public boolean attachGallery(long api_object_id, long gallery_id){
        Log.d(TAG, "attachGallery(api_object:" + String.valueOf(api_object_id) + ", gallery_id:"+String.valueOf(gallery_id) + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(COLUMN_API_OBJECT_ID, api_object_id);
        map.put(COLUMN_GALLERY_ID, gallery_id);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.insert(DATABASE_TABLE, null, map);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public void dettachGallery(long api_object_id, long gallery_id){
        Log.d(TAG, "dettachGallery(api_object:" + String.valueOf(api_object_id) + ", gallery_id:"+String.valueOf(gallery_id) + ")");
        try {
            Log.d(TAG, "Delete gallery " + String.valueOf(gallery_id)+ " from apiObject: " + String.valueOf(api_object_id));
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {String.valueOf(api_object_id),String.valueOf(gallery_id)};
            db.delete(DATABASE_TABLE,
                    COLUMN_API_OBJECT_ID + "=? AND " + COLUMN_GALLERY_ID + "=?",
                    args);
        } catch (SQLException ex) {
            Log.e(TAG, "Error delete self location", ex);
        }
    }
}
