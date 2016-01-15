package com.rinnion.archived.database.cursor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.helper.ProductHelper;
import com.rinnion.archived.database.model.ApiObjects.Gamer;
import com.rinnion.archived.database.model.ApiObjects.Product;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 25.10.13
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class ProductCursor extends SQLiteCursor {

    public ProductCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }

    public Product getItem() {
        long id = getColId();
        Product gamer = new Product(id);
        gamer.content= getString(getColumnIndexOrThrow(ProductHelper.COLUMN_CONTENT));
        gamer.title = getString(getColumnIndexOrThrow(ProductHelper.COLUMN_TITLE));
        gamer.price = getString(getColumnIndexOrThrow(ProductHelper.COLUMN_PRICE));
        gamer.thumb = getString(getColumnIndexOrThrow(ProductHelper.COLUMN_THUMB));
        //gamer.top = getString(getColumnIndexOrThrow(GamerHelper.COLUMN_TOP));
        return gamer;
    }

    protected long getColId() {
        return getLong(getColumnIndexOrThrow(ApiObjectHelper._ID));
    }

    public static class Factory implements SQLiteDatabase.CursorFactory {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            ProductCursor c = new ProductCursor(sqLiteDatabase, sqLiteCursorDriver, s, sqLiteQuery);
            return c;
        }
    }
}
