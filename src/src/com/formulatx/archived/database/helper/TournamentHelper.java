package com.formulatx.archived.database.helper;

import android.database.sqlite.SQLiteDatabase;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.ApiObjectCursor;
import com.formulatx.archived.database.cursor.TournamentCursor;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.formulatx.archived.utils.Log;

/**
 * Helper for working with News repository
 */
public class TournamentHelper extends ApiObjectHelper {

    public static final String TOURNAMENT_LADIES_TROPHY = "stpetersburg_ladies_trophy";
    public static final String TOURNAMENT_OPEN = "st_petersburg_open";
    //public static final String TOURNAMENT_OPEN = "turnir_1";
    private static final String TAG = "TournamentHelper";

    public TournamentHelper(DatabaseOpenHelper doh) {
        super(doh);
    }

    public boolean add(Tournament tournament) {
        return super.add(tournament);
    }

    @Override
    public ApiObjectCursor getAll() {
        return getAllByType(ApiObject.OBJECT);
    }

    public Tournament get(long id) {
        Log.d(TAG, "get (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS +
                " FROM " + DATABASE_TABLE +
                " WHERE _id=? AND " + COLUMN_DISPLAY_METHOD + "=?";
        SQLiteDatabase d = doh.getReadableDatabase();
        TournamentCursor c = (TournamentCursor) d.rawQueryWithFactory(
                new TournamentCursor.Factory(),
                sql,
                new String[]{String.valueOf(id), ApiObject.OBJECT},
                null);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        return c.getItem();
    }

    public TournamentCursor getAllOther() {
        Log.v(TAG, "getAllOther ()");

        String sql = "SELECT " + ALL_COLUMNS +
                " FROM " + DATABASE_TABLE +
                " WHERE " + COLUMN_DISPLAY_METHOD + "=? AND " + COLUMN_POST_NAME + " not in (?,?)" +
                " ORDER BY " + COLUMN_TITLE + " ASC";

        SQLiteDatabase d = doh.getReadableDatabase();
        TournamentCursor c = (TournamentCursor) d.rawQueryWithFactory(
                new TournamentCursor.Factory(),
                sql,
                new String[]{ApiObject.OBJECT, TOURNAMENT_LADIES_TROPHY, TOURNAMENT_OPEN},
                null);
        c.moveToFirst();
        return c;
    }

    @Override
    public Tournament getByPostName(String post_name) {
        Log.v(TAG, "getByPostName ("+String.valueOf(post_name)+")");

        if (post_name == null){
            Log.e(TAG, "post_name is null");
            return null;
        }

        String sql = "SELECT " + ALL_COLUMNS +
                " FROM " + DATABASE_TABLE +
                " WHERE " + COLUMN_POST_NAME + "=? AND " + COLUMN_DISPLAY_METHOD + "=?" +
                " ORDER BY " + COLUMN_TITLE + " ASC";

        SQLiteDatabase d = doh.getReadableDatabase();
        TournamentCursor c = (TournamentCursor) d.rawQueryWithFactory(
                new TournamentCursor.Factory(),
                sql,
                new String[]{post_name, ApiObject.OBJECT},
                null);
        if (c.getCount() == 0) return null;
        c.moveToFirst();
        return c.getItem();
    }

}
