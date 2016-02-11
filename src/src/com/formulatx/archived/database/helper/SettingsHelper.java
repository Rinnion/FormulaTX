package com.formulatx.archived.database.helper;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.BaseColumns;
import android.util.Base64;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.SettingCursor;
import com.formulatx.archived.utils.Log;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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

    public Bundle getBundleParameter(String parameterName, Bundle b) {
        String parameter = getParameter(parameterName, "b");
        if (parameter == null) return b;
        return deserializeBundle(parameter);
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

    public void setParameter(String paramName, Bundle paramValue) {setTypedParameter(paramName, serializeBundle(paramValue), "b");}

    private String serializeBundle(final Bundle bundle) {
        String base64 = null;
        final Parcel parcel = Parcel.obtain();
        try {
            parcel.writeBundle(bundle);
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final GZIPOutputStream zos = new GZIPOutputStream(new BufferedOutputStream(bos));
            zos.write(parcel.marshall());
            zos.close();
            base64 = Base64.encodeToString(bos.toByteArray(), 0);
        } catch(IOException e) {
            e.printStackTrace();
            base64 = null;
        } finally {
            parcel.recycle();
        }
        return base64;
    }

    private Bundle deserializeBundle(final String base64) {
        Bundle bundle = null;
        final Parcel parcel = Parcel.obtain();
        try {
            final ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            final GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(Base64.decode(base64, 0)));
            int len = 0;
            while ((len = zis.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            zis.close();
            parcel.unmarshall(byteBuffer.toByteArray(), 0, byteBuffer.size());
            parcel.setDataPosition(0);
            bundle = parcel.readBundle();
        } catch (IOException e) {
            e.printStackTrace();
            bundle = null;
        }  finally {
            parcel.recycle();
        }

        return bundle;
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
