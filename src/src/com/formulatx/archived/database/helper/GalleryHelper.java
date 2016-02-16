package com.formulatx.archived.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.CommentCursor;
import com.formulatx.archived.database.cursor.GalleryDescriptionCursor;
import com.formulatx.archived.database.cursor.GalleryItemCursor;
import com.formulatx.archived.database.model.GalleryItem;
import com.formulatx.archived.utils.Log;


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

    public GalleryItem getItem(long id) {
        Log.d(TAG, "getItem (" + id + ")");
        GalleryItemCursor c = searchItem(id);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        return c.getItem();
    }

    private GalleryItemCursor searchItem(long id) {
        Log.d(TAG, "searchItem (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE _id = ?";
        SQLiteDatabase d = doh.getReadableDatabase();
        return (GalleryItemCursor) d.rawQueryWithFactory(
                new GalleryItemCursor.Factory(),
                sql,
                new String[]{String.valueOf(id)},
                null);
    }
    public boolean merge(GalleryItem item) {
        Log.d(TAG, "merge(" + String.valueOf(item) + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, item.id);
        map.put(COLUMN_GALLERY_ID, item.gallery_id);
        map.put(COLUMN_TYPE, item.type);
        map.put(COLUMN_PICTURE, item.url);
        map.put(COLUMN_LINK, item.link);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.insertWithOnConflict(DATABASE_TABLE, null, map, SQLiteDatabase.CONFLICT_REPLACE);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public GalleryDescriptionCursor getAllPodcasts() {
        Log.v(TAG, "getAllGalleries ()");

        String sql = "SELECT " + COLUMN_GALLERY_DESCRIPTION_TITLE + ", "+ COLUMN_GALLERY_DESCRIPTION_PICTURE + ", "+ COLUMN_GALLERY_DESCRIPTION_VIDEO + ", " + _ID +
                " FROM " + DATABASE_TABLE_DESCRIPTION +
                " WHERE " + COLUMN_GALLERY_DESCRIPTION_TYPE + "=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryDescriptionCursor c = (GalleryDescriptionCursor) d.rawQueryWithFactory(
                new GalleryDescriptionCursor.Factory(),
                sql,
                new String[]{"podcast"},
                null);
        c.moveToFirst();
        return c;
    }

    public GalleryDescriptionCursor getAllGalleries(String content_type) {
        return getAllGalleries("gallery", content_type);
    }
    public GalleryDescriptionCursor getAllGalleriesWithContent(int[] range, String content_type) {
        return getAllGalleries("gallery", range, content_type);
    }

    protected GalleryDescriptionCursor getAllGalleries(String type, int[] range, String contentType) {
        Log.v(TAG, "getAllGalleries ()");

        if (range == null) range = new int[0];
        StringBuilder sb = new StringBuilder();
        for (int i : range) {
            ((sb.length() > 0) ? sb.append(",") : sb).append(i);
        }
        String in = sb.toString();

        String sql = "SELECT " + COLUMN_GALLERY_DESCRIPTION_TITLE + ", "+ COLUMN_GALLERY_DESCRIPTION_PICTURE + ", "+ COLUMN_GALLERY_DESCRIPTION_VIDEO + ", " + _ID +
                " FROM " + DATABASE_TABLE_DESCRIPTION +
                " WHERE " + _ID + " in (" + in + ") AND " + COLUMN_GALLERY_DESCRIPTION_TYPE + "=? AND gd_"+contentType + " is not null";

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryDescriptionCursor c = (GalleryDescriptionCursor) d.rawQueryWithFactory(
                new GalleryDescriptionCursor.Factory(),
                sql,
                new String[]{type},
                null);
        c.moveToFirst();
        return c;
    }

    protected GalleryDescriptionCursor getAllGalleries(String type, String contentType) {
        Log.v(TAG, "getAllGalleries ()");

        String sql = "SELECT " + COLUMN_GALLERY_DESCRIPTION_TITLE + ", "+ COLUMN_GALLERY_DESCRIPTION_PICTURE + ", "+ COLUMN_GALLERY_DESCRIPTION_VIDEO + ", " + _ID +
                " FROM " + DATABASE_TABLE_DESCRIPTION +
                " WHERE " + COLUMN_GALLERY_DESCRIPTION_TYPE + "=? AND gd_"+contentType + " is not null";

        SQLiteDatabase d = doh.getReadableDatabase();
        GalleryDescriptionCursor c = (GalleryDescriptionCursor) d.rawQueryWithFactory(
                new GalleryDescriptionCursor.Factory(),
                sql,
                new String[]{type},
                null);
        c.moveToFirst();
        return c;
    }

    public void deleteGallery(long id) {
        Log.d(TAG, "deleteGallery(" + String.valueOf(id) + ")");
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {String.valueOf(id)};
            int descs = db.delete(DATABASE_TABLE_DESCRIPTION,
                    _ID + "=?",
                    args);
            int items = db.delete(DATABASE_TABLE,
                    COLUMN_GALLERY_ID + "=?",
                    args);
            Log.d(TAG, String.format("delete %d galleries with %d items", descs, items));
        } catch (SQLException ex) {
            Log.e(TAG, "Error deleting item", ex);
        }
    }

    public boolean mergeGallery(GalleryDescriptionCursor.GalleryDescription gd) {
        Log.d(TAG, "mergeGallery(" + String.valueOf(gd) + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, gd.id);
        map.put(COLUMN_GALLERY_DESCRIPTION_TITLE, gd.title);
        map.put(COLUMN_GALLERY_DESCRIPTION_TYPE, gd.type);
        if (gd.picture!=null) map.put(COLUMN_GALLERY_DESCRIPTION_PICTURE, gd.picture);
        if (gd.video != null) map.put(COLUMN_GALLERY_DESCRIPTION_VIDEO, gd.video);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.insertWithOnConflict(DATABASE_TABLE_DESCRIPTION, null, map, SQLiteDatabase.CONFLICT_REPLACE);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public boolean setGalleryPicture(long id, String picture) {
        Log.d(TAG, "setGalleryPicture(" + String.valueOf(id) + ", " + String.valueOf(picture)+ ")");
        ContentValues map;
        map = new ContentValues();
        map.put(COLUMN_GALLERY_DESCRIPTION_PICTURE, picture);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.updateWithOnConflict(DATABASE_TABLE_DESCRIPTION, map, "_id=?", new String[]{String.valueOf(id)},SQLiteDatabase.CONFLICT_IGNORE);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public boolean setGalleryVideo(long id, String picture) {
        Log.d(TAG, "setGalleryPicture(" + String.valueOf(id) + ", " + String.valueOf(picture)+ ")");
        ContentValues map;
        map = new ContentValues();
        map.put(COLUMN_GALLERY_DESCRIPTION_VIDEO, picture);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.updateWithOnConflict(DATABASE_TABLE_DESCRIPTION, map, "_id=?", new String[]{String.valueOf(id)},SQLiteDatabase.CONFLICT_IGNORE);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }
}
