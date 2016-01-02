package com.rinnion.archived.database.helper;

import android.database.sqlite.SQLiteDatabase;
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

    public GamerHelper(DatabaseOpenHelper doh) {
        super(doh);
    }

    @Override
    public boolean add(ApiObject apiObject) {
        return super.add(apiObject);
    }

    public boolean add(Gamer tournament) {
        return super.add(tournament);
    }

    @Override
    public GamerCursor getAll() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE " + COLUMN_OBJ_TYPE + "=?  ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase d = doh.getReadableDatabase();
        GamerCursor c = (GamerCursor) d.rawQueryWithFactory(
                new GamerCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObjectTypes.EN_Gamer)},
                null);
        c.moveToFirst();
        return c;
    }

    public GamerCursor getAllByParent(String parent) {
        Log.v(TAG, "getAllByParent ()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE " + COLUMN_OBJ_TYPE + "=? AND " + COLUMN_PARENT + "=?  ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase d = doh.getReadableDatabase();
        GamerCursor c = (GamerCursor) d.rawQueryWithFactory(
                new GamerCursor.Factory(),
                sql,
                new String[]{String.valueOf(ApiObjectTypes.EN_Gamer), parent},
                null);
        c.moveToFirst();
        return c;
    }

}
