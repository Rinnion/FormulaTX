package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.CardCursor;
import com.rinnion.archived.database.cursor.ProductCursor;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.Card;
import com.rinnion.archived.database.model.ApiObjects.Product;
import com.rinnion.archived.utils.Log;

/**
 * Helper for working with News repository
 */
public class CardHelper implements BaseColumns {

    private static final String TAG = "CardHelper";

    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_THUMB = "ao_" + ApiObjectHelper.COLUMN_THUMB;
    public static final String COLUMN_TITLE = "ao_" + ApiObjectHelper.COLUMN_TITLE;

    public static String DATABASE_TABLE_ADDITINAL = "cards";
    public static String ALIAS = "c";
    private final ApiObjectHelper aoh;
    private DatabaseOpenHelper doh;

    public CardHelper() {
        this.doh = ArchivedApplication.getDatabaseOpenHelper();
        aoh = new ApiObjectHelper(doh);
    }

    public static String[] COLS_ADDITIONAL;
    public static String ALL_COLUMNS_ADDITINAL;
    public static String ALL_COLUMNS_ADDITINAL_WITH_ALIAS;

    static {
        COLS_ADDITIONAL = new String[]{
                _ID,
                COLUMN_LINK,
                COLUMN_STATUS
        };
        ALL_COLUMNS_ADDITINAL = TextUtils.join(",", COLS_ADDITIONAL);
        ALL_COLUMNS_ADDITINAL_WITH_ALIAS = ALIAS + "." + TextUtils.join("," + ALIAS + ".", COLS_ADDITIONAL);
    }

    public boolean merge(Card card) {
        Log.d(TAG, "merge(" + card.toString() + ")");

        //FIXME: Нужно переделать на update/merge
        ApiObject apiObject = aoh.get(card.id);

        delete(card.id);
        apiObject.thumb = card.thumb;
        aoh.add(apiObject);

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, card.id);
        map.put(COLUMN_LINK, card.link);
        map.put(COLUMN_STATUS, card.status);

        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            return (db.insert(DATABASE_TABLE_ADDITINAL, null, map)!=-1);

        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public void delete(long id) {
        Log.d(TAG, "delete (" + id + ")");
        try {
            Log.d(TAG, "Delete card: " + id);
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {Long.toString(id)};
            db.delete(DATABASE_TABLE_ADDITINAL, _ID + "=?", args);
            aoh.delete(id);
        } catch (SQLException ex) {
            Log.e(TAG, "Error delete self location", ex);
        }
    }

    public Card getCard(long id) {
        Log.v(TAG, "getProduct (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS_ADDITINAL_WITH_ALIAS +
                ", ao." + ApiObjectHelper.COLUMN_THUMB + " AS " + COLUMN_THUMB +
                ", ao." + ApiObjectHelper.COLUMN_TITLE + " AS " + COLUMN_TITLE +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS " + ALIAS + " " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=" + ALIAS + "._id " +
                " WHERE ao." + ApiObjectHelper.COLUMN_DISPLAY_METHOD +"=? AND " + ALIAS + "._id=?";
        SQLiteDatabase d = doh.getReadableDatabase();
        CardCursor c = (CardCursor) d.rawQueryWithFactory(
                new CardCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.CARD), String.valueOf(id)},
                null);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        return c.getItem();
    }

    public CardCursor getAllByParent(long parent) {
        Log.v(TAG, "getAllByParent ("+parent+")");

        String sql = "SELECT " + ALL_COLUMNS_ADDITINAL_WITH_ALIAS +
                ", ao." + ApiObjectHelper.COLUMN_THUMB + " AS " + COLUMN_THUMB +
                ", ao." + ApiObjectHelper.COLUMN_TITLE + " AS " + COLUMN_TITLE +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS " + ALIAS + " " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=" + ALIAS + "._id " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS p ON ao.parent = p.post_name " +
                " WHERE ao." + ApiObjectHelper.COLUMN_DISPLAY_METHOD +"=? AND " + ALIAS + "._id=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        CardCursor c = (CardCursor) d.rawQueryWithFactory(
                new CardCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.CARD), String.valueOf(parent)},
                null);
        c.moveToFirst();
        return c;
    }

    public CardCursor getAll() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS_ADDITINAL_WITH_ALIAS +
                ", ao." + ApiObjectHelper.COLUMN_THUMB + " AS " + COLUMN_THUMB +
                ", ao." + ApiObjectHelper.COLUMN_TITLE + " AS " + COLUMN_TITLE +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS " + ALIAS + " " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=" + ALIAS + "._id " +
                " WHERE ao." + ApiObjectHelper.COLUMN_DISPLAY_METHOD +"=?";

        SQLiteDatabase d = doh.getReadableDatabase();
        CardCursor c = (CardCursor) d.rawQueryWithFactory(
                new CardCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.CARD)},
                null);
        c.moveToFirst();
        return c;
    }

}
