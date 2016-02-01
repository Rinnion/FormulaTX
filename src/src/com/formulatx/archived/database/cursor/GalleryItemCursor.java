package com.formulatx.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.formulatx.archived.database.model.GalleryItem;
import com.formulatx.archived.database.helper.CommentHelper;
import com.formulatx.archived.database.helper.GalleryHelper;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class GalleryItemCursor extends SQLiteCursor {

    public GalleryItemCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public GalleryItem getItem() {
        long id = getColId();
        long gallery = getLong(getColumnIndexOrThrow(GalleryHelper.COLUMN_GALLERY_ID));
        String type = getString(getColumnIndexOrThrow(GalleryHelper.COLUMN_TYPE));
        String url = getString(getColumnIndexOrThrow(GalleryHelper.COLUMN_PICTURE));
        String link = getString(getColumnIndexOrThrow(GalleryHelper.COLUMN_LINK));
        GalleryItem comment = new GalleryItem(id, gallery, type, url, link);
        return comment;
    }

    private long getColDateReceive() {
        return getLong(getColumnIndexOrThrow(CommentHelper.COLUMN_CONTENT));
    }

    private long getColDatePost() {
        return getLong(getColumnIndexOrThrow(CommentHelper.COLUMN_DATE_POST));
    }

    private long getColMessageId() {
        return getLong(getColumnIndexOrThrow(CommentHelper.COLUMN_MESSAGE_ID));
    }

    private String getColContent() {
        return getString(getColumnIndexOrThrow(CommentHelper.COLUMN_CONTENT));
    }

    public int getColId() {
        return getInt(getColumnIndexOrThrow(CommentHelper._ID));
    }

    public int getColUserId() {
        return getInt(getColumnIndexOrThrow(CommentHelper.COLUMN_USER_ID));
    }

    private String getColUserName() {
        return getString(getColumnIndexOrThrow(CommentHelper.COLUMN_USER_NAME));
    }

    private String getColUserAvatar() {
        return getString(getColumnIndexOrThrow(CommentHelper.COLUMN_USER_AVATAR));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            GalleryItemCursor c = new GalleryItemCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}