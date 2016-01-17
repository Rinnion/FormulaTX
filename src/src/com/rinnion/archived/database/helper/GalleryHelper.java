package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.renderscript.Program;
import android.text.TextUtils;
import com.rinnion.archived.database.cursor.GalleryDescriptionCursor;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.CommentCursor;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.model.Comment;
import com.rinnion.archived.database.model.GalleryItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.02.14
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class GalleryHelper implements BaseColumns {
    public static final String COLUMN_API_OBJECT_ID = "api_object_id";
    public static final String COLUMN_LINK_GALLERY_ID= "gallery_id_link";
    public static final String COLUMN_GALLERY_ID = "gallery_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_PICTURE = "picture";

    public static final String TYPE_PICTURE = "picture";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_WATERMARK = "watermark";
    public static final String TYPE_AUDIO = "audio";
    public static final String COLUMN_GALLERY_DESCRIPTION_TITLE = "gd_title";
    public static final String COLUMN_GALLERY_DESCRIPTION_PICTURE = "gd_picture";
    public static final String COLUMN_GALLERY_DESCRIPTION_TYPE = "gd_type";
    public static final String COLUMN_GALLERY_DESCRIPTION_VIDEO = "gd_video";
    public static String DATABASE_TABLE = "gallery";
    public static String DATABASE_TABLE_DESCRIPTION = "gallery_description";
    public static String DATABASE_TABLE_API_OBJECT_LINK = "api_object_gallery";

    public static String[] COLS;
    public static String ALL_COLUMNS;

    static {
        COLS = new String[]{
                _ID,
                COLUMN_GALLERY_ID,
                COLUMN_TYPE,
                COLUMN_PICTURE,
                COLUMN_LINK
        };
        ALL_COLUMNS = TextUtils.join(",", COLS);
    }

    private final String TAG = "CommentHelper";

    private DatabaseOpenHelper doh;

    public GalleryHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }

    public GalleryItemCursor getAllItems() {
        Log.v(TAG, "getAllItems ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE;

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryItemCursor c = (GalleryItemCursor) d.rawQueryWithFactory(
                new GalleryItemCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public GalleryItemCursor getAllItemsByGalleryIdAndType(long gallery_id, String type) {
        Log.v(TAG, "getAllItems ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE " + COLUMN_GALLERY_ID + "=? AND " + COLUMN_TYPE + "=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryItemCursor c = (GalleryItemCursor) d.rawQueryWithFactory(
                new GalleryItemCursor.Factory(),
                sql,
                new String[]{String.valueOf(gallery_id), type},
                null);
        c.moveToFirst();
        return c;
    }

    public GalleryDescriptionCursor.GalleryDescription getGallery(long gallery_id) {
        Log.v(TAG, "getGallery (" + String.valueOf(gallery_id) + ")");

        String sql = "SELECT " + COLUMN_GALLERY_DESCRIPTION_TITLE + ", "+COLUMN_GALLERY_DESCRIPTION_TYPE + ", "+ COLUMN_GALLERY_DESCRIPTION_PICTURE + ", "+ COLUMN_GALLERY_DESCRIPTION_VIDEO + ", " + _ID +
                " FROM " + DATABASE_TABLE_DESCRIPTION +
                " WHERE " + _ID + "=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryDescriptionCursor c = (GalleryDescriptionCursor) d.rawQueryWithFactory(
                new GalleryDescriptionCursor.Factory(),
                sql,
                new String[] {String.valueOf(gallery_id)},
                null);
        c.moveToFirst();
        if (c.getCount() == 0) return null;
        return c.getItem();
    }

    public CommentCursor getAllByApiObjectId(long api_object_id) {
        Log.v(TAG, "getAllByApiObjectId (" + String.valueOf(api_object_id) + ")");

        String sql = "SELECT " + ALL_COLUMNS +
                " FROM " + DATABASE_TABLE + " AS g " +
                " LEFT JOIN " + DATABASE_TABLE_API_OBJECT_LINK + " AS ao ON ao."+COLUMN_LINK_GALLERY_ID+"=g."+COLUMN_GALLERY_ID +
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
        Log.v(TAG, "getAllByApiObjectAndItemTypeId (api_object:" + String.valueOf(api_object_id) + ", item_type:"+String.valueOf(item_type) + ")");

        String sql = "SELECT " + ALL_COLUMNS +
                " FROM " + DATABASE_TABLE + " AS g " +
                " LEFT JOIN " + DATABASE_TABLE_API_OBJECT_LINK + " AS aol ON aol."+COLUMN_LINK_GALLERY_ID+"=g."+COLUMN_GALLERY_ID +
                " WHERE aol." + COLUMN_API_OBJECT_ID + "=? AND g." + COLUMN_TYPE + "=?";

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
        Log.d(TAG, "getItem (" + id + ")");
        CommentCursor c = searchItem(id);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        return c.getItem();
    }

    public boolean isItemPresent(long id) {
        Log.d(TAG, "isItemPresent (" + id + ")");
        CommentCursor c = searchItem(id);
        return c.getCount() == 1;
    }

    private CommentCursor searchItem(long id) {
        Log.d(TAG, "searchItem (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE _id = ?";
        SQLiteDatabase d = doh.getReadableDatabase();
        return (CommentCursor) d.rawQueryWithFactory(
                new CommentCursor.Factory(),
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

    public boolean merge(GalleryItem item){
        if (isItemPresent(item.id)) deleteItem(item.id);
        return add(item);
    }

    public boolean add(GalleryItem item) {
        Log.d(TAG, "add(" + String.valueOf(item) + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, item.id);
        map.put(COLUMN_GALLERY_ID, item.gallery_id);
        map.put(COLUMN_TYPE, item.type);
        map.put(COLUMN_PICTURE, item.url);
        map.put(COLUMN_LINK, item.link);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.insert(DATABASE_TABLE, null, map);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public Cursor getAttachedGallery(long api_object_id){
        Log.d(TAG, "getAttachedGallery (" + api_object_id + ")");

        String sql = "SELECT " + COLUMN_API_OBJECT_ID + ", " + COLUMN_LINK_GALLERY_ID + " FROM " + DATABASE_TABLE_API_OBJECT_LINK;
        SQLiteDatabase d = doh.getReadableDatabase();
        CommentCursor c = (CommentCursor) d.rawQueryWithFactory(
                new CommentCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public boolean attachGallery(long api_object_id, long gallery_id){
        Log.d(TAG, "attachGallery(api_object:" + String.valueOf(api_object_id) + ", gallery_id:"+String.valueOf(gallery_id) + ")");

        detachGallery(api_object_id, gallery_id);

        ContentValues map;
        map = new ContentValues();
        map.put(COLUMN_API_OBJECT_ID, api_object_id);
        map.put(COLUMN_LINK_GALLERY_ID, gallery_id);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            long insert = db.insert(DATABASE_TABLE_API_OBJECT_LINK, null, map);
            return insert != -1;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public void detachGallery(long api_object_id, long gallery_id){
        Log.d(TAG, "detachGallery(api_object:" + String.valueOf(api_object_id) + ", gallery_id:"+String.valueOf(gallery_id) + ")");
        try {
            Log.d(TAG, "Delete gallery " + String.valueOf(gallery_id)+ " from apiObject: " + String.valueOf(api_object_id));
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {String.valueOf(api_object_id),String.valueOf(gallery_id)};
            db.delete(DATABASE_TABLE_API_OBJECT_LINK,
                    COLUMN_API_OBJECT_ID + "=? AND " + COLUMN_LINK_GALLERY_ID + "=?",
                    args);
        } catch (SQLException ex) {
            Log.e(TAG, "Error detaching gallery", ex);
        }
    }


    public GalleryDescriptionCursor getAllGalleries() {
        Log.v(TAG, "getAllGalleries ()");

        String sql = "SELECT " + COLUMN_GALLERY_DESCRIPTION_TITLE + ", "+ COLUMN_GALLERY_DESCRIPTION_PICTURE + ", "+ COLUMN_GALLERY_DESCRIPTION_VIDEO + ", " + _ID +
                " FROM " + DATABASE_TABLE_DESCRIPTION;

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryDescriptionCursor c = (GalleryDescriptionCursor) d.rawQueryWithFactory(
                new GalleryDescriptionCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public GalleryDescriptionCursor getAllGalleries(int[] range) {
        Log.v(TAG, "getAllGalleries ()");

        if (range == null) range = new int[0];
        StringBuilder sb = new StringBuilder();
        for (int i : range) {
            ((sb.length() > 0) ? sb.append(",") : sb).append(i);
        }
        String in = sb.toString();

        String sql = "SELECT " + COLUMN_GALLERY_DESCRIPTION_TITLE + ", "+ COLUMN_GALLERY_DESCRIPTION_PICTURE + ", "+ COLUMN_GALLERY_DESCRIPTION_VIDEO + ", " + _ID +
                " FROM " + DATABASE_TABLE_DESCRIPTION +
                " WHERE " + _ID + " in (" + in + ") AND " + COLUMN_GALLERY_DESCRIPTION_TYPE + "=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryDescriptionCursor c = (GalleryDescriptionCursor) d.rawQueryWithFactory(
                new GalleryDescriptionCursor.Factory(),
                sql,
                new String[]{"gallery"},
                null);
        c.moveToFirst();
        return c;
    }

    public GalleryDescriptionCursor getAllGalleriesByContent(String type) {
        Log.v(TAG, "getAllGalleries ()");

        String sql = "SELECT " + COLUMN_GALLERY_DESCRIPTION_TITLE + ", "+ COLUMN_GALLERY_DESCRIPTION_PICTURE + ", "+ COLUMN_GALLERY_DESCRIPTION_VIDEO + ", " + _ID +
                " FROM " + DATABASE_TABLE_DESCRIPTION +
                " WHERE " + COLUMN_GALLERY_DESCRIPTION_TYPE + "=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryDescriptionCursor c = (GalleryDescriptionCursor) d.rawQueryWithFactory(
                new GalleryDescriptionCursor.Factory(),
                sql,
                new String[] {type},
                null);
        c.moveToFirst();
        return c;
    }

    public boolean isGalleryExists(long id) {
        Log.v(TAG, "isGalleryExists ()");

        String sql = "SELECT " + COLUMN_GALLERY_DESCRIPTION_TITLE +
                " FROM " + DATABASE_TABLE_DESCRIPTION +
                " WHERE _id=?" ;

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryDescriptionCursor c = (GalleryDescriptionCursor) d.rawQueryWithFactory(
                new GalleryDescriptionCursor.Factory(),
                sql,
                new String[] {String.valueOf(id)},
                null);
        c.moveToFirst();
        return c.getCount() > 0;
    }

    public boolean addGallery(GalleryDescriptionCursor.GalleryDescription gd) {
        Log.v(TAG, "addGallery (" + gd.toString() +")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, gd.id);
        map.put(COLUMN_GALLERY_DESCRIPTION_TITLE, gd.title);
        map.put(COLUMN_GALLERY_DESCRIPTION_TYPE, gd.type);
        map.put(COLUMN_GALLERY_DESCRIPTION_PICTURE, gd.picture);
        map.put(COLUMN_GALLERY_DESCRIPTION_VIDEO, gd.video);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.insert(DATABASE_TABLE_DESCRIPTION, null, map);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public void deleteGallery(long id) {
        Log.d(TAG, "deleteGallery(" + String.valueOf(id) + ")");
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {String.valueOf(id)};
            db.delete(DATABASE_TABLE_DESCRIPTION,
                    _ID + "=?",
                    args);
        } catch (SQLException ex) {
            Log.e(TAG, "Error deleting item", ex);
        }
    }

    public boolean mergeGallery(GalleryDescriptionCursor.GalleryDescription gd) {
        Log.d(TAG, "mergeGallery(" + String.valueOf(gd) + ")");
        if (isGalleryExists(gd.id)) deleteGallery(gd.id);
        return addGallery(gd);
    }
}
