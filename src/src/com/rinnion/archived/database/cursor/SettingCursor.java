package com.rinnion.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.rinnion.archived.database.helper.MessageHelper;
import com.rinnion.archived.database.helper.SettingsHelper;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class SettingCursor extends SQLiteCursor {

    public SettingCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public long getIntValue() {
        return getLong(getColumnIndexOrThrow(SettingsHelper.COLUMN_VALUE));
    }

    public double getDoubleValue() {
        return getDouble(getColumnIndexOrThrow(SettingsHelper.COLUMN_VALUE));
    }

    public String getStringValue() {
        return getString(getColumnIndexOrThrow(SettingsHelper.COLUMN_VALUE));
    }

    public int getColId() {
        return getInt(getColumnIndexOrThrow(MessageHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            SettingCursor c = new SettingCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}
