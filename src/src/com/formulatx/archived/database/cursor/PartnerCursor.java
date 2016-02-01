package com.formulatx.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.formulatx.archived.database.helper.PartnerHelper;
import com.formulatx.archived.database.model.ApiObjects.Partner;
import com.formulatx.archived.database.helper.ApiObjectHelper;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class PartnerCursor extends SQLiteCursor {

    public PartnerCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public Partner getItem() {
        long id = getColId();
        Partner card = new Partner(id);
        card.title = getString(getColumnIndexOrThrow(PartnerHelper.COLUMN_TITLE));
        card.status = getString(getColumnIndexOrThrow(PartnerHelper.COLUMN_STATUS));
        card.link = getString(getColumnIndexOrThrow(PartnerHelper.COLUMN_LINK));
        card.thumb = getString(getColumnIndexOrThrow(PartnerHelper.COLUMN_THUMB));
        return card;
    }

    protected long getColId() {
        return getLong(getColumnIndexOrThrow(ApiObjectHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            PartnerCursor c = new PartnerCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}
