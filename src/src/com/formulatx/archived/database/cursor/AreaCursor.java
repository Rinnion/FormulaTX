package com.formulatx.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.AreaHelper;
import com.formulatx.archived.database.model.ApiObjects.Area;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class AreaCursor extends SQLiteCursor {

    public AreaCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public Area getItem() {
        long id = getColId();
        Area area = new Area(id);
        area.address = getString(getColumnIndexOrThrow(AreaHelper.COLUMN_ADDRESS));
        area.map = getString(getColumnIndexOrThrow(AreaHelper.COLUMN_MAP));
        area.content = getString(getColumnIndexOrThrow(AreaHelper.COLUMN_CONTENT));
        area.title = getString(getColumnIndexOrThrow(AreaHelper.COLUMN_TITLE));
        return area;
    }

    protected long getColId() {
        return getLong(getColumnIndexOrThrow(ApiObjectHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            AreaCursor c = new AreaCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}
