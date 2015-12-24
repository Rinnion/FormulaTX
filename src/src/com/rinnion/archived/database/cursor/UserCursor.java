package com.rinnion.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.rinnion.archived.database.helper.UserHelper;
import com.rinnion.archived.database.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class UserCursor extends SQLiteCursor {

    public UserCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public User getItem() {
        long id = getColId();
        String name = getColName();
        String avatar = getColAvatar();

        long date_receive = getColDateReceive();

        User user = new User(id, name, avatar, date_receive);
        return user;
    }

    private String getColAvatar() {
        return getString(getColumnIndexOrThrow(UserHelper.COLUMN_AVATAR));
    }
    private String getColName() {
        return getString(getColumnIndexOrThrow(UserHelper.COLUMN_NAME));
    }

    public int getColId() {
        return getInt(getColumnIndexOrThrow(UserHelper._ID));
    }

    public long getColDateReceive() {
        return getInt(getColumnIndexOrThrow(UserHelper.COLUMN_DATE_RECEIVE));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            UserCursor c = new UserCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}
