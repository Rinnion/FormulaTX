package com.rinnion.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.SettingCursor;

/**
 * Created by tretyakov on 06.07.2015.
 */
public class SettingsHelper implements BaseColumns {

    public static String DATABASE_TABLE = "settings";
    public static String COLUMN_IDENTITY = "_id";
    public static String COLUMN_PARAMETER = "parameter";
    public static String COLUMN_VALUE = "value";
    private final String TAG = "SettingsHelper";
    private final DatabaseOpenHelper doh;

    public SettingsHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }

    public String getParameter(String parameterName) {
        Log.d(TAG, "getParameter (" + parameterName + ")");
        String sql = "SELECT l." + SettingsHelper.COLUMN_IDENTITY + ",l." + SettingsHelper.COLUMN_PARAMETER + "," + SettingsHelper.COLUMN_VALUE +
                " FROM " + SettingsHelper.DATABASE_TABLE + " AS l" +
                " WHERE l." + SettingsHelper.COLUMN_PARAMETER + "=?";
        SQLiteDatabase d = doh.getReadableDatabase();
        SettingCursor c = (SettingCursor) d.rawQueryWithFactory(
                new SettingCursor.Factory(),
                sql,
                new String[]{parameterName},
                null);
        c.moveToFirst();
        int index = c.getColumnIndex(SettingsHelper.COLUMN_VALUE);
        if (c.getCount() == 0) {
            return null;
        }
        return c.getString(index);
    }

    public Integer getIntParameter(String parameterName, int i) {
        String parameter = getParameter(parameterName);
        if (parameter == null) return i;
        return Integer.parseInt(parameter);
    }

    public Double getDoubleParameter(String parameterName, double i) {
        String parameter = getParameter(parameterName);
        if (parameter == null) return i;
        return Double.parseDouble(parameter);
    }

    public void setParameter(String paramName, String paramValue) {
        Log.d(TAG, String.format("setParameter (%s,%s)", paramName, paramValue));
        ContentValues map;
        map = new ContentValues();
        map.put(SettingsHelper.COLUMN_PARAMETER, paramName);
        map.put(SettingsHelper.COLUMN_VALUE, paramValue);
        SQLiteDatabase db = null;
        try {
            db = this.doh.getWritableDatabase();
            db.beginTransaction();
            db.delete(SettingsHelper.DATABASE_TABLE, SettingsHelper.COLUMN_PARAMETER + "=?", new String[]{paramName});
            db.insert(SettingsHelper.DATABASE_TABLE, null, map);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error updating parameters", e);
        } finally {
            if (db != null) db.endTransaction();
        }
    }
}
