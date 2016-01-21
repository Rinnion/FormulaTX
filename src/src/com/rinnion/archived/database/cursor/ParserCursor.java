package com.rinnion.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.rinnion.archived.database.helper.CommentHelper;
import com.rinnion.archived.database.helper.ParserHelper;
import com.rinnion.archived.database.model.Comment;
import com.rinnion.archived.database.model.Parser;

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
        return new Parser(id, title, date, data, system);
    }

    public int getColId() {
        return getInt(getColumnIndexOrThrow(CommentHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            ParserCursor c = new ParserCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}