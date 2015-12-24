package com.rinnion.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.rinnion.archived.database.helper.MessageHelper;
import com.rinnion.archived.database.model.Message;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class MessageCursor extends SQLiteCursor {

    public MessageCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public Message getItem() {
        long id = getColId();
        String content = getColContent();
        String background = getColBackground();
        long date_post = getColDatePost();
        long likes = getColLikes();
        String tags = getColTags();
        long date_receive = getColDateReceive();
        boolean like = getColVote();
        Long comments = getColComments();
        Message message = new Message(id, content, background, date_post, likes, like, comments, tags, date_receive);
        return message;
    }

    private boolean getColVote() {
        return getLong(getColumnIndexOrThrow(MessageHelper.COLUMN_LIKE)) == 1;
    }

    private long getColComments() {
        return getLong(getColumnIndexOrThrow(MessageHelper.COLUMN_COMMENTS));
    }

    private long getColDateReceive() {
        return getLong(getColumnIndexOrThrow(MessageHelper.COLUMN_CONTENT));
    }

    private String getColTags() {
        return getString(getColumnIndexOrThrow(MessageHelper.COLUMN_TAGS));
    }

    private long getColLikes() {
        return getLong(getColumnIndexOrThrow(MessageHelper.COLUMN_LIKES));
    }

    private long getColDatePost() {
        return getLong(getColumnIndexOrThrow(MessageHelper.COLUMN_DATE_POST));
    }

    private String getColBackground() {
        return getString(getColumnIndexOrThrow(MessageHelper.COLUMN_BACKGROUND));
    }

    private String getColContent() {
        return getString(getColumnIndexOrThrow(MessageHelper.COLUMN_CONTENT));
    }

    public int getColId() {
        return getInt(getColumnIndexOrThrow(MessageHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            MessageCursor c = new MessageCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}
