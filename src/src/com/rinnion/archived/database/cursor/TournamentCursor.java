package com.rinnion.archived.database.cursor;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

/**
 * Created by tretyakov on 25.12.2015.
 */
public class TournamentCursor extends SQLiteCursor {
    public TournamentCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        super(db, driver, editTable, query);
    }
}
