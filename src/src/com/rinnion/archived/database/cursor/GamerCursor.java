package com.rinnion.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.Gamer;
import com.rinnion.archived.database.model.ApiObjects.Tournament;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class GamerCursor extends SQLiteCursor {

    public GamerCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public Gamer getItem() {
        long id = getColId();
        Gamer gamer = new Gamer(id);
        gamer.favorite = getInt(getColumnIndexOrThrow(GamerHelper.COLUMN_FAVORITE)) == 1;
        gamer.name = getString(getColumnIndexOrThrow(GamerHelper.COLUMN_NAME));
        gamer.surname = getString(getColumnIndexOrThrow(GamerHelper.COLUMN_SURNAME));
        gamer.full_name = getString(getColumnIndexOrThrow(GamerHelper.COLUMN_FULL_NAME));
        gamer.rating = getLong(getColumnIndexOrThrow(GamerHelper.COLUMN_RATING));
        gamer.country = getString(getColumnIndexOrThrow(GamerHelper.COLUMN_COUNTRY));
        gamer.flag = getString(getColumnIndexOrThrow(GamerHelper.COLUMN_FLAG));
        gamer.thumb = getString(getColumnIndexOrThrow(GamerHelper.COLUMN_THUMB));
        gamer.title = getString(getColumnIndexOrThrow(ApiObjectHelper.COLUMN_TITLE));
        return gamer;
    }

    protected long getColId() {
        return getLong(getColumnIndexOrThrow(ApiObjectHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            GamerCursor c = new GamerCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}
