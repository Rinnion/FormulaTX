package com.formulatx.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.formulatx.archived.database.model.ApiObjects.News;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class NewsCursor extends ApiObjectCursor {

    public NewsCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public News getItem() {
        long id = getColId();
        News apiObject = new News(id);
        fillApiObject(apiObject);
        return apiObject;
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {
        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            NewsCursor c = new NewsCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }

}
