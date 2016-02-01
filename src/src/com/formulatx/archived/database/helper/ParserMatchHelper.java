package com.formulatx.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.ParserCursor;
import com.formulatx.archived.database.model.Table;
import com.formulatx.archived.network.loaders.cursor.ParserDataCursor;
import com.formulatx.archived.parsers.Match;
import com.formulatx.archived.utils.Log;
import org.json.JSONException;


/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.02.14
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class ParserMatchHelper implements BaseColumns {
    public static final String COLUMN_PAGE = "page";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PARSER = "parser";

    public static String DATABASE_TABLE = "parser_matches";
    public static String[] COLS;
    public static String ALL_COLUMNS;
    public static String ALL_COLUMNS_WITH_ALIAS;

    static {
        COLS = new String[]{
                _ID,
                COLUMN_PAGE,
                COLUMN_NUMBER,
                COLUMN_DATA,
                COLUMN_TYPE,
                COLUMN_PARSER,
        };
        ALL_COLUMNS = TextUtils.join(",", COLS);
        ALL_COLUMNS_WITH_ALIAS = "pm." + TextUtils.join(",pm.", COLS);
    }


    private final String TAG = "CommentHelper";

    private DatabaseOpenHelper doh;

    public ParserMatchHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }


    public ParserDataCursor getAll(int[] range, String system, String settings, String page) {
        Log.d(TAG, String.format("getPages( %s, %s, %s, %s)", String.valueOf(range), system, settings, page));

        if (range == null) range = new int[0];
        StringBuilder sb = new StringBuilder();
        for (int i : range) {
            ((sb.length() > 0) ? sb.append(",") : sb).append(i);
        }
        String in = sb.toString();

        String sql = "SELECT " + ALL_COLUMNS_WITH_ALIAS + " FROM " + DATABASE_TABLE + " pm " +
                " LEFT JOIN " + ParserHelper.DATABASE_TABLE + " p ON p._id=pm." + COLUMN_PARSER +
                " WHERE " +
                " pm." + COLUMN_PARSER + " in (" + in + ") AND " +
                " p." + ParserHelper.COLUMN_SYSTEM + "=? AND " +
                " p." + ParserHelper.COLUMN_SETTINGS + "=? AND " +
                " pm." + COLUMN_PAGE + ((page == null) ? " is null " : "=?") +
                " ORDER BY pm._id";

        SQLiteDatabase d = doh.getReadableDatabase();
        ParserDataCursor c = (ParserDataCursor) d.rawQueryWithFactory(
                new ParserDataCursor.Factory(),
                sql,
                (page == null) ? new String[]{system, settings} : new String[]{system, settings, page},
                null);
        c.moveToFirst();
        return c;
    }

    public ParserDataCursor getPages(int[] range, String system, String settings) {
        Log.d(TAG, String.format("getPages( %s, %s, %s )", String.valueOf(range), system, settings));

        if (range == null) range = new int[0];
        StringBuilder sb = new StringBuilder();
        for (int i : range) {
            ((sb.length() > 0) ? sb.append(",") : sb).append(i);
        }
        String in = sb.toString();

        String sql = "SELECT " + ALL_COLUMNS_WITH_ALIAS + " FROM " + DATABASE_TABLE + " pm " +
                " LEFT JOIN " + ParserHelper.DATABASE_TABLE + " p ON p._id=" + COLUMN_PARSER +
                " WHERE " +
                " pm." + COLUMN_PARSER + " in (" + in + ") AND " +
                " p." + ParserHelper.COLUMN_SYSTEM + "=? AND " +
                " p." + ParserHelper.COLUMN_SETTINGS + "=? " +
                " GROUP BY pm." + COLUMN_PAGE + " " +
                " ORDER BY pm._id ";

        SQLiteDatabase d = doh.getReadableDatabase();
        ParserDataCursor c = (ParserDataCursor) d.rawQueryWithFactory(
                new ParserDataCursor.Factory(),
                sql,
                new String[]{system, settings},
                null);
        c.moveToFirst();
        return c;
    }

    public ParserDataCursor getAll() {
        Log.d(TAG, "getAll()");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE;

        SQLiteDatabase d = doh.getReadableDatabase();
        ParserDataCursor c = (ParserDataCursor) d.rawQueryWithFactory(
                new ParserDataCursor.Factory(),
                sql,
                null,
                null);
        c.moveToFirst();
        return c;
    }

    public boolean clear(long id) {
        Log.d(TAG, "clear()");
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.delete(DATABASE_TABLE, "_id=?", new String[]{String.valueOf(id)});
            Log.d(TAG, "success");
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error clearing self location table");
            return false;
        }
    }

    public boolean isItemPresent(long id) {
        Log.d(TAG, "isItemPresent (" + id + ")");
        ParserCursor c = searchItem(id);
        return c.getCount() == 1;
    }

    private ParserCursor searchItem(long id) {
        Log.d(TAG, "searchItem (" + id + ")");

        String sql = "SELECT " + ALL_COLUMNS + " FROM " + DATABASE_TABLE + " WHERE _id = ?";
        SQLiteDatabase d = doh.getReadableDatabase();
        return (ParserCursor) d.rawQueryWithFactory(
                new ParserCursor.Factory(),
                sql,
                new String[]{String.valueOf(id)},
                null);
    }

    public boolean merge(Table item) {
        if (isItemPresent(item.id)) delete(item.id);
        return add(item);
    }

    public boolean add(Table table) {
        Log.d(TAG, "addLocation(" + String.valueOf(table) + ")");

        ContentValues map;
        map = new ContentValues();
        map.put(COLUMN_PAGE, table.page);
        map.put(COLUMN_NUMBER, table.number);
        map.put(COLUMN_DATA, table.data);
        map.put(COLUMN_TYPE, table.type);
        map.put(COLUMN_PARSER, table.parser);
        try {
            SQLiteDatabase db = doh.getWritableDatabase();
            db.insert(DATABASE_TABLE, null, map);
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "Error writing location", e);
            return false;
        }
    }

    public void delete(long id) {
        Log.d(TAG, "delete (" + id + ")");
        try {
            Log.d(TAG, "Delete parser table: " + id);
            SQLiteDatabase db = doh.getWritableDatabase();
            String[] args = {String.valueOf(id)};
            db.delete(DATABASE_TABLE, _ID + "=?", args);
        } catch (SQLException ex) {
            Log.e(TAG, "Delete parser table", ex);
        }
    }

    public void add(Match[] parse, long pid) {
        int i = 1;
        SQLiteDatabase db = doh.getWritableDatabase();
        db.beginTransaction();
        try {
            Log.d(TAG, "Delete parser table: " + pid);
            int delete = db.delete(DATABASE_TABLE, COLUMN_PARSER + "=?", new String[]{String.valueOf(pid)});

            for (Match p : parse) {
                ContentValues map;
                map = new ContentValues();
                map.put(COLUMN_DATA, p.getJSONObject().toString());
                map.put(COLUMN_NUMBER, i++);
                map.put(COLUMN_PAGE, p.header);
                map.put(COLUMN_TYPE, p.type);
                map.put(COLUMN_PARSER, pid);
                try {
                    db.insert(DATABASE_TABLE, null, map);
                } catch (SQLException e) {
                    Log.e(TAG, "Error table", e);
                }
            }
            db.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }
}
