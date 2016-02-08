package com.formulatx.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.AreaCursor;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.database.model.ApiObjects.Area;
import com.formulatx.archived.utils.Log;

/**
 * Helper for working with News repository
 */
public class AreaHelper implements BaseColumns {

    private static final String TAG = "AreaHelper";

    public static final String COLUMN_MAP = "map";
    public static final String COLUMN_ADDRESS= "address";
    public static final String COLUMN_TITLE= "title";
    public static final String COLUMN_CONTENT= "content";

    public static String DATABASE_TABLE_ADDITINAL = "areas";
    private final ApiObjectHelper aoh;
    private DatabaseOpenHelper doh;

    public AreaHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
        aoh = new ApiObjectHelper(doh);
    }

    public static String[] COLS_ADDITIONAL;
    public static String ALL_COLUMNS_ADDITINAL;

    static {
        COLS_ADDITIONAL = new String[]{
                _ID,
                COLUMN_MAP,
                COLUMN_ADDRESS,
                COLUMN_TITLE,
                COLUMN_CONTENT,
        };
        ALL_COLUMNS_ADDITINAL = TextUtils.join(",", COLS_ADDITIONAL);
    }

    public boolean merge(Area area) {
        Log.d(TAG, "merge(" + area.toString() + ")");

        //FIXME: Нужно переделать на update/merge
        ApiObject apiObject = aoh.get(area.id);

        delete(area.id);
        aoh.add(apiObject);

        ContentValues map;
        map = new ContentValues();
        map.put(_ID, area.id);
        map.put(COLUMN_MAP, area.map);
        map.put(COLUMN_ADDRESS, area.address);
        map.put(COLUMN_TITLE, area.title);
        map.put(COLUMN_CONTENT, area.content);

        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            return (db.insert(DATABASE_TABLE_ADDITINAL, null, map)!=-1);

        } catch (SQLException e) {
            Log.e(TAG, "Error writing area", e);
            return false;
        }
    }

    public void delete(long id) {
        Log.d(TAG, "delete (" + id + ")");
        try {
            Log.d(TAG, "Delete area: " + id);
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {Long.toString(id)};
            db.delete(DATABASE_TABLE_ADDITINAL, _ID + "=?", args);
            aoh.delete(id);
        } catch (SQLException ex) {
            Log.e(TAG, "Error deleting area", ex);
        }
    }

    public Area getArea(long id) {
        Log.v(TAG, "getProduct ("+id+")");

        String join = "g." + TextUtils.join(",g.", COLS_ADDITIONAL);

        String sql = "SELECT " + join +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS g " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=g._id " +
                " WHERE " + ApiObjectHelper.COLUMN_DISPLAY_METHOD +"=? AND g._id=?";
        SQLiteDatabase d = doh.getReadableDatabase();
        AreaCursor c = (AreaCursor) d.rawQueryWithFactory(
                new AreaCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.AREA), String.valueOf(id)},
                null);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        return c.getItem();
    }

    public AreaCursor getAllByParent(long parent) {
        Log.v(TAG, "getAllByParent ()");

        String join = "g." + TextUtils.join(",g.", COLS_ADDITIONAL);

        String sql = "SELECT " + join +
                " FROM " + DATABASE_TABLE_ADDITINAL + " AS g " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS ao ON ao._id=g._id " +
                " LEFT JOIN " + ApiObjectHelper.DATABASE_TABLE + " AS p ON ao.parent = p.post_name " +
                " WHERE ao." + ApiObjectHelper.COLUMN_DISPLAY_METHOD + "=? AND p." + ApiObjectHelper._ID + "=? ";

        SQLiteDatabase d = doh.getReadableDatabase();
        AreaCursor c = (AreaCursor) d.rawQueryWithFactory(
                new AreaCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObject.AREA), String.valueOf(parent)},
                null);
        c.moveToFirst();
        return c;
    }

}
