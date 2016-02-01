package com.formulatx.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.formulatx.archived.database.helper.CommentHelper;
import com.formulatx.archived.database.helper.GalleryHelper;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class GalleryDescriptionCursor extends SQLiteCursor {

    public GalleryDescriptionCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public GalleryDescription getItem() {
        long id = getColId();
        String title = getString(getColumnIndexOrThrow(GalleryHelper.COLUMN_GALLERY_DESCRIPTION_TITLE));
        String type = getString(getColumnIndexOrThrow(GalleryHelper.COLUMN_GALLERY_DESCRIPTION_TYPE));
        GalleryDescription gd = new GalleryDescription(id, title, type);
        gd.picture = getString(getColumnIndexOrThrow(GalleryHelper.COLUMN_GALLERY_DESCRIPTION_PICTURE));
        gd.video = getString(getColumnIndexOrThrow(GalleryHelper.COLUMN_GALLERY_DESCRIPTION_VIDEO));
        return gd;
    }

    public int getColId() {
        return getInt(getColumnIndexOrThrow(CommentHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            GalleryDescriptionCursor c = new GalleryDescriptionCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }

    public static class GalleryDescription {
        public static final String TYPE_GALLERY = "gallery";
        public static final String TYPE_PODCAST = "podcast";

        public final long id;
        public final String title;
        public final String type;
        public String picture;
        public String video;

        public GalleryDescription(long id, String title, String type) {

            this.id = id;
            this.title = title;
            this.type = type;
        }
    }
}