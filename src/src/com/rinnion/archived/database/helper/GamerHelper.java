package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.database.cursor.GamerCursor;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.database.model.ApiObjects.Gamer;
import com.rinnion.archived.database.model.ApiObjects.Tournament;

/**
 * Helper for working with News repository
 */
public class GamerHelper extends ApiObjectHelper {

    private static final String TAG = "GamerHelper";

    public static final String COLUMN_NAME= "name";
    public static final String COLUMN_SURNAME= "surname";
    public static final String COLUMN_FULL_NAME= "full_name";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_FLAG = "flag";

    public static String DATABASE_TABLE_ADDITINAL = "gamers";

    public GamerHelper(DatabaseOpenHelper doh) {
        super(doh);
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
                COLUMN_FLAG
        };
        ALL_COLUMNS_ADDITINAL = TextUtils.join(",", COLS_ADDITIONAL);
    }

    public boolean add(Gamer gamer) {
        Log.d(TAG, "add(" + gamer.toString() + ")");

        delete(gamer.id);
        super.add(gamer);

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, gamer.id);
        map.put(COLUMN_NAME, gamer.name);
        map.put(COLUMN_SURNAME, gamer.surname);
        map.put(COLUMN_FULL_NAME, gamer.full_name);
        map.put(COLUMN_RATING, gamer.rating);
        map.put(COLUMN_COUNTRY, gamer.country);
        map.put(COLUMN_FLAG, gamer.flag);

        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            return (db.insert(DATABASE_TABLE, null, map)!=-1);

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
            super.delete(id);
        } catch (SQLException ex) {
            Log.e(TAG, "Error delete self location", ex);
        }
    }

    @Override
    public GamerCursor getAll() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS_ADDITINAL +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS g " +
                " LEFT JOIN " + DATABASE_TABLE + " AS ao ON ao._id=o._id " +
                " WHERE " + COLUMN_OBJ_TYPE +"=? " +
                " ORDER BY o." + COLUMN_RATING + " ASC";
        SQLiteDatabase d = doh.getReadableDatabase();
        GamerCursor c = (GamerCursor) d.rawQueryWithFactory(
                new GamerCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObjectTypes.EN_Gamer)},
                null);
        c.moveToFirst();
        return c;
    }

    public GamerCursor getAllByParent(long parent) {
        Log.v(TAG, "getAllByParent ()");

        String sql = "SELECT " + ALL_COLUMNS_ADDITINAL +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS g " +
                " LEFT JOIN " + DATABASE_TABLE + " AS ao ON ao._id=o._id " +
                " WHERE ao." + COLUMN_OBJ_TYPE + "=? AND ao." + COLUMN_PARENT + "=? " +
                " ORDER BY o." + COLUMN_RATING + " ASC";

        SQLiteDatabase d = doh.getReadableDatabase();
        GamerCursor c = (GamerCursor) d.rawQueryWithFactory(
                new GamerCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObjectTypes.EN_Gamer), String.valueOf(parent)},
                null);
        c.moveToFirst();
        return c;
    }

}
