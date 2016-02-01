package com.formulatx.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.formulatx.archived.database.helper.ParserHelper;
import com.formulatx.archived.database.model.Parser;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class ParserCursor extends SQLiteCursor {

    public ParserCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public Parser getItem() {
        long id = getLong(getColumnIndexOrThrow(ParserHelper._ID));
        String title = getString(getColumnIndexOrThrow(ParserHelper.COLUMN_TITLE));
        String date = getString(getColumnIndexOrThrow(ParserHelper.COLUMN_DATE));
        String data = getString(getColumnIndexOrThrow(ParserHelper.COLUMN_DATA));
        String system = getString(getColumnIndexOrThrow(ParserHelper.COLUMN_SYSTEM));
        String settings = getString(getColumnIndexOrThrow(ParserHelper.COLUMN_SETTINGS));
        long downloaded = getLong(getColumnIndexOrThrow(ParserHelper.COLUMN_DOWNLOADED));
        long parsed = getLong(getColumnIndexOrThrow(ParserHelper.COLUMN_PARSED));
        return new Parser(id, title, date, data, system, settings, downloaded, parsed);
    }

    public int getColId() {
        return getInt(getColumnIndexOrThrow(ParserHelper._ID));
    }

    public String getData() {
        return getString(getColumnIndexOrThrow(ParserHelper.COLUMN_DATA));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            ParserCursor c = new ParserCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}