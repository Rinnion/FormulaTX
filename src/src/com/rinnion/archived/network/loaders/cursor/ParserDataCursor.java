package com.rinnion.archived.network.loaders.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.provider.BaseColumns;
import com.rinnion.archived.database.helper.ParserMatchHelper;
import com.rinnion.archived.database.model.Table;

/**
 * Created by tretyakov on 15.01.2016.
 * represents program cursor
 */
public class ParserDataCursor extends SQLiteCursor {

    public static final String _ID = BaseColumns._ID;
    public static final String PAGE = ParserMatchHelper.COLUMN_PAGE;
    public static final String NUMBER = ParserMatchHelper.COLUMN_NUMBER;
    public static final String DATA = ParserMatchHelper.COLUMN_DATA;
    public static final String TYPE = ParserMatchHelper.COLUMN_TYPE;
    public static final String PARSER = ParserMatchHelper.COLUMN_PARSER;

    private static final String TAG = "TableCursor";

    private static String[] names = new String[]{PAGE, NUMBER, DATA, TYPE};
    private static String[] columns = new String[]{_ID, PAGE, NUMBER, DATA, TYPE};

    public ParserDataCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);             }


    public long getId(){
        return getLong(getColumnIndexOrThrow(_ID));
    }

    public String getType(){
        return getString(getColumnIndexOrThrow(TYPE));
    }

    public String getPage() {
        return getString(getColumnIndexOrThrow(PAGE));
    }

    public int getNumber() {
        return getInt(getColumnIndexOrThrow(NUMBER));
    }

    public String getData() {
        return getString(getColumnIndexOrThrow(DATA));
    }

    public long getParser() {
        return getLong(getColumnIndexOrThrow(DATA));
    }

    public Table getItem() {
        Table table = new Table();
        table.id = getId();
        table.page = getPage();
        table.number = getNumber();
        table.data = getData();
        table.type = getType();
        table.parser = getParser();
        return table;
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            ParserDataCursor c = new ParserDataCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }

}
