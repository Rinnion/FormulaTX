package com.rinnion.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.AreaHelper;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.model.ApiObjects.Area;
import com.rinnion.archived.database.model.ApiObjects.Gamer;

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
        Area gamer = new Area(id);
        gamer.address = getString(getColumnIndexOrThrow(AreaHelper.COLUMN_ADDRESS));
        gamer.map = getString(getColumnIndexOrThrow(AreaHelper.COLUMN_MAP));
        return gamer;
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
