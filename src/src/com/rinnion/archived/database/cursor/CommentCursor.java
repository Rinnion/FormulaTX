package com.rinnion.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.rinnion.archived.database.helper.CommentHelper;
import com.rinnion.archived.database.model.Comment;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class CommentCursor extends SQLiteCursor {

    public CommentCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public Comment getItem() {
        long id = getColId();
        String content = getColContent();
        long date_post = getColDatePost();
        long date_receive = getColDateReceive();
        long user_id = getColUserId();
        String user_name = getColUserName();
        String user_avatar = getColUserAvatar();
        long message_id = getColMessageId();
        Comment comment = new Comment(id, content, date_post, user_id, date_receive, user_name, user_avatar, message_id);
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
            CommentCursor c = new CommentCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}