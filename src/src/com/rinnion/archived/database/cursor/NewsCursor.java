package com.rinnion.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.rinnion.archived.database.helper.NewsHelper;
import com.rinnion.archived.database.model.Message;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class NewsCursor extends SQLiteCursor {

    public NewsCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public Message getItem() {
        long id = getColId();
        long date_post = getColDatePost();
        long likes = getColLikes();
        String tags = getColTags();
        long date_receive = getColDateReceive();
        boolean like = getColVote();
        Long comments = getColComments();
        //Message message = new Message(id, content, background, date_post, likes, like, comments, tags, date_receive);
        //return message;
        return null;
    }

    private boolean getColVote() {
        return getLong(getColumnIndexOrThrow(NewsHelper.COLUMN_NAME)) == 1;
    }

    private long getColComments() {
        return getLong(getColumnIndexOrThrow(NewsHelper.COLUMN_CAPTION));
    }

    private long getColDateReceive() {
        return getLong(getColumnIndexOrThrow(NewsHelper.COLUMN_CONTENT));
    }

    private String getColTags() {
        return getString(getColumnIndexOrThrow(NewsHelper.COLUMN_DATE));
    }

    private long getColLikes() {
        return getLong(getColumnIndexOrThrow(NewsHelper.COLUMN_THUMBS));
    }

    private long getColDatePost() {
        return getLong(getColumnIndexOrThrow(NewsHelper.COLUMN_TYPE));
    }

    private long getColId() {
        return getLong(getColumnIndexOrThrow(NewsHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            NewsCursor c = new NewsCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}
