package com.rinnion.archived.database.helper;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.NewsCursor;

/**
 * Helper for working with News repository
 */
public class NewsHelper extends ApiObjectHelper {

    private final String TAG = "NewsHelper";

    public NewsHelper(DatabaseOpenHelper doh) {
        super(doh);
    }

    public NewsCursor getByParent(String parent) {
        Log.v(TAG, "getAll ()");

        String sql = "SELECT " + ALL_COLUMNS +
                " FROM " + DATABASE_TABLE +
                " WHERE " + COLUMN_PARENT + "=? " +
                " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase d = doh.getReadableDatabase();
        NewsCursor c = (NewsCursor) d.rawQueryWithFactory(
                new NewsCursor.Factory(),
                sql,
                new String[] {parent},
                null);
        c.moveToFirst();
        return c;
    }
}
