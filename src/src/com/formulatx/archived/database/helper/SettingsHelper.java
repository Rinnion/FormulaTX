package com.formulatx.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.SettingCursor;
import com.formulatx.archived.utils.Log;

/**
 * Created by tretyakov on 06.07.2015.
 */
public class SettingsHelper implements BaseColumns {

    public static String DATABASE_TABLE = "settings";
    public static String COLUMN_IDENTITY = "_id";
    public static String COLUMN_PARAMETER = "parameter";
    public static String COLUMN_VALUE = "value";
    public static String COLUMN_TYPE = "type";
    private final String TAG = "SettingsHelper";
    private final DatabaseOpenHelper doh;

    public SettingsHelper(DatabaseOpenHelper doh) {
        this.doh = doh;
    }



    protected String getParameter(String parameterName, String type) {
        Log.d(TAG, "getStringParameter (" + parameterName + ", " + type + ")");
        String sql = "SELECT l." + SettingsHelper.COLUMN_IDENTITY + ",l." + SettingsHelper.COLUMN_PARAMETER + ", l." + SettingsHelper.COLUMN_VALUE +
                " FROM " + SettingsHelper.DATABASE_TABLE + " AS l" +
                " WHERE l." + SettingsHelper.COLUMN_PARAMETER + "=? AND l." + SettingsHelper.COLUMN_TYPE + "=?";
        SQLiteDatabase d = doh.getReadableDatabase();
        SettingCursor c = (SettingCursor) d.rawQueryWithFactory(
                new SettingCursor.Factory(),
                sql,
                new String[]{parameterName, type},
                null);
        c.moveToFirst();
        int index = c.getColumnIndex(SettingsHelper.COLUMN_VALUE);
        if (c.getCount() == 0) {
            return null;
        }
        return c.getString(index);
    }


    public String getStringParameter(String parameterName) {
        String parameter = getParameter(parameterName, "s");
        return parameter;
    }

    public int getIntParameter(String parameterName, int i) {
        String parameter = getParameter(parameterName, "i");
        if (parameter == null) return i;
        return Integer.parseInt(parameter);
    }
    public long getLongParameter(String parameterName, long i) {
        String parameter = getParameter(parameterName, "l");
        if (parameter == null) return i;
        return Long.parseLong(parameter);
    }

    public double getDoubleParameter(String parameterName, double d) {
        String parameter = getParameter(parameterName, "d");
        if (parameter == null) return d;
        return Double.parseDouble(parameter);
    }

    public void setParameter(String paramName, String paramValue) {
        setTypedParameter(paramName, paramValue, "s");
    }

    public void setParameter(String paramName, int paramValue) {
        setTypedParameter(paramName, String.valueOf(paramValue), "i");
    }

    public void setParameter(String paramName, double paramValue) {
        setTypedParameter(paramName, String.valueOf(paramValue), "d");
    }

    public void setParameter(String paramName, long paramValue) {
        setTypedParameter(paramName, String.valueOf(paramValue), "l");
    }

    protected void setTypedParameter(String paramName, String paramValue, String type) {
        Log.d(TAG, String.format("setTypedParameter (%s, %s, %s)", paramName, paramValue, type));
        ContentValues map;
        map = new ContentValues();
        map.put(SettingsHelper.COLUMN_PARAMETER, paramName);
        map.put(SettingsHelper.COLUMN_VALUE, paramValue);
        map.put(SettingsHelper.COLUMN_TYPE, type);
        SQLiteDatabase db = null;
        try {
            db = this.doh.getWritableDatabase();
            db.beginTransaction();
            db.delete(SettingsHelper.DATABASE_TABLE, SettingsHelper.COLUMN_PARAMETER + "=? AND " + SettingsHelper.COLUMN_TYPE + "=?", new String[]{paramName, type});
            db.insert(SettingsHelper.DATABASE_TABLE, null, map);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Error updating parameters", e);
        } finally {
            if (db != null) db.endTransaction();
        }
    }
}
