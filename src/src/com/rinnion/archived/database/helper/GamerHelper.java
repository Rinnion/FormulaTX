package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.GamerCursor;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.database.model.ApiObjects.Gamer;

/**
 * Helper for working with News repository
 */
public class GamerHelper implements BaseColumns {

    private static final String TAG = "GamerHelper";

    public static final String COLUMN_NAME= "name";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_SURNAME= "surname";
    public static final String COLUMN_FULL_NAME= "full_name";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_FLAG = "flag";
    public static final String COLUMN_THUMB = "ao_" + ApiObjectHelper.COLUMN_THUMB;

    public static String DATABASE_TABLE_ADDITINAL = "gamers";
    private final ApiObjectHelper aoh;
    private DatabaseOpenHelper doh;

    public GamerHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
        aoh = new ApiObjectHelper(doh);
    }

    public static String[] COLS_ADDITIONAL;
    public static String ALL_COLUMNS_ADDITINAL;

    static {
        COLS_ADDITIONAL = new String[]{
                _ID,
                COLUMN_NAME,
                COLUMN_SURNAME,
                COLUMN_FULL_NAME,
                COLUMN_RATING,
                COLUMN_COUNTRY,
                COLUMN_FLAG,
                COLUMN_FAVORITE
        };
        ALL_COLUMNS_ADDITINAL = TextUtils.join(",", COLS_ADDITIONAL);
    }

    public boolean merge(Gamer gamer) {
        Log.d(TAG, "merge(" + gamer.toString() + ")");

        //FIXME: Нужно переделать на update/merge
        ApiObject apiObject = aoh.get(gamer.id);

        delete(gamer.id);
        apiObject.thumb = gamer.thumb;
        aoh.add(apiObject);

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, gamer.id);
        map.put(COLUMN_NAME, gamer.name);
        map.put(COLUMN_SURNAME, gamer.surname);
        map.put(COLUMN_FULL_NAME, gamer.full_name);
        map.put(COLUMN_RATING, gamer.rating);
        map.put(COLUMN_COUNTRY, gamer.country);
        map.put(COLUMN_FLAG, gamer.flag);
        map.put(COLUMN_FAVORITE, gamer.favorite);

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
            Log.d(TAG, "Delete self location: " + id);
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {Long.toString(id)};
            db.delete(DATABASE_TABLE_ADDITINAL, _ID + "=?", args);
            aoh.delete(id);
        } catch (SQLException ex) {
            Log.e(TAG, "Error delete self location", ex);
        }
    }

    public Gamer getGamer(long id) {
        Log.v(TAG, "getProduct ("+id+")");

        String sql = "SELECT g." + ALL_COLUMNS_ADDITINAL + ",ao." + ApiObjectHelper.COLUMN_THUMB + " AS " + COLUMN_THUMB +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS g " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=g._id " +
                " WHERE " + ApiObjectHelper.COLUMN_DISPLAY_METHOD +"=? AND g._id=?" +
                " ORDER BY g." + COLUMN_RATING + " ASC";
        SQLiteDatabase d = doh.getReadableDatabase();
        GamerCursor c = (GamerCursor) d.rawQueryWithFactory(
                new GamerCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.GAMER), String.valueOf(id)},
                null);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        return c.getItem();
    }

    public GamerCursor getAllByParent(long parent) {
        Log.v(TAG, "getAllByParent ()");

        String sql = "SELECT g." + ALL_COLUMNS_ADDITINAL + ",ao." + ApiObjectHelper.COLUMN_THUMB + " AS " + COLUMN_THUMB +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS g " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=g._id " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS p ON ao.parent = p.post_name " +
                " WHERE ao." + ApiObjectHelper.COLUMN_DISPLAY_METHOD + "=? AND p." + ApiObjectHelper._ID + "=? " +
                " ORDER BY g." + COLUMN_RATING + " ASC";

        SQLiteDatabase d = doh.getReadableDatabase();
        GamerCursor c = (GamerCursor) d.rawQueryWithFactory(
                new GamerCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.GAMER), String.valueOf(parent)},
                null);
        c.moveToFirst();
        return c;
    }

}
