package com.rinnion.archived;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.helper.SettingsHelper;

/**
 * Created by tretyakov on 08.05.2014.
 */
public class ArchivedApplication extends Application {
    private final static String TAG = "ArchivedApplication";
    public static SettingAccessor Settings;
    private static DatabaseOpenHelper doh;
    private static Context context;

    public static Context getAppContext() {
        return ArchivedApplication.context;
    }

    public static DatabaseOpenHelper getDatabaseOpenHelper() {
        if (doh == null) {
            doh = new DatabaseOpenHelper(context);
        }
        return doh;
    }

    public static String getParameter(String parameter) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        return sh.getParameter(parameter);
    }

    public static void setParameter(String parameter, String value) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        sh.setParameter(parameter, value);
    }

    public static String getResourceString(int id) {
        return getAppContext().getResources().getString(id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        ArchivedApplication.context = getApplicationContext();
        ArchivedApplication.Settings = new SettingAccessor();
    }

    @Override
    public void onTerminate() {
        Log.i(TAG, "onTerminate");

    }

    @Override
    public void onLowMemory() {
        // In-memory caches should be thrown overboard here
        Log.i(TAG, "onLowMemory");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "onConifgurationChanged");
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, newConfig.toString());
        }
    }

}
