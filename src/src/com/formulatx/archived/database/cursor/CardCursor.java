package com.formulatx.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.formulatx.archived.database.model.ApiObjects.Card;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.CardHelper;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class CardCursor extends SQLiteCursor {

    public CardCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public Card getItem() {
        long id = getColId();
        Card card = new Card(id);
        card.title = getString(getColumnIndexOrThrow(CardHelper.COLUMN_TITLE));
        card.status = getString(getColumnIndexOrThrow(CardHelper.COLUMN_STATUS));
        card.link = getString(getColumnIndexOrThrow(CardHelper.COLUMN_LINK));
        card.thumb = getString(getColumnIndexOrThrow(CardHelper.COLUMN_THUMB));
        return card;
    }

    protected long getColId() {
        return getLong(getColumnIndexOrThrow(ApiObjectHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            CardCursor c = new CardCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}
