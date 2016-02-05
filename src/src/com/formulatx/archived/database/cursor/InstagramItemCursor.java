package com.formulatx.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.formulatx.archived.database.helper.CommentHelper;
import com.formulatx.archived.database.helper.InstagramHelper;
import com.formulatx.archived.database.helper.TwitterHelper;
import com.formulatx.archived.database.model.TwitterItem;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class InstagramItemCursor extends SQLiteCursor {

    public InstagramItemCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public TwitterItem getItem() {
        long id = getColId();
        long reference_id = getInt(getColumnIndexOrThrow(InstagramHelper.COLUMN_REFERENCE_ID));
        String text = getString(getColumnIndexOrThrow(InstagramHelper.COLUMN_TEXT));
        String link = getString(getColumnIndexOrThrow(InstagramHelper.COLUMN_LINK));
        String date = getString(getColumnIndexOrThrow(InstagramHelper.COLUMN_DATE));
        TwitterItem ti = new TwitterItem(id, reference_id, text, link, date);
        return ti;
    }

    public int getColId() {
        return getInt(getColumnIndexOrThrow(CommentHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            InstagramItemCursor c = new InstagramItemCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}