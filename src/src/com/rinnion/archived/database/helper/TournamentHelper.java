package com.rinnion.archived.database.helper;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.database.cursor.TournamentCursor;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.database.model.ApiObjects.Tournament;

/**
 * Helper for working with News repository
 */
public class TournamentHelper extends ApiObjectHelper {

    public static final String TOURNAMENT_LADIES_TROPHY = "stpetersburg_ladies_trophy";
    //public static final String TOURNAMENT_OPEN = "st_petersburg_open";
    public static final String TOURNAMENT_OPEN = "turnir_1";
    private static final String TAG = "TournamentHelper";

    public TournamentHelper(DatabaseOpenHelper doh) {
        super(doh);
    }

    @Override
    public boolean add(ApiObject apiObject) {
        return super.add(apiObject);
    }

    public boolean add(Tournament tournament) {
        return super.add(tournament);
    }

    public TournamentCursor getAllOther() {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS +
                " FROM " + DATABASE_TABLE +
                " WHERE "+COLUMN_OBJ_TYPE+"=? AND " + COLUMN_POST_NAME + " not in (?,?)" +
                " ORDER BY " + COLUMN_TITLE + " ASC";

        SQLiteDatabase d = doh.getReadableDatabase();
        TournamentCursor c = (TournamentCursor) d.rawQueryWithFactory(
                new TournamentCursor.Factory(),
                sql,
                new String[] {String.valueOf(ApiObjectTypes.EN_Object), TOURNAMENT_LADIES_TROPHY, TOURNAMENT_OPEN},
                null);
        c.moveToFirst();
        return c;
    }
}
