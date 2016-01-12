package com.rinnion.archived;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.rinnion.archived.utils.Files;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.helper.SettingsHelper;
import com.rinnion.archived.utils.MyLocale;

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

    public static Bitmap getBitmap(String thumb) {
        Bitmap bitmap = BitmapFactory.decodeFile(thumb);
        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_splash_screen);
        }
        return bitmap;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ArchivedApplication.context = getApplicationContext();
        ArchivedApplication.Settings = new SettingAccessor();
        Files.Initialize();
        Log.Initialize();
        MyLocale.Initialize();

        Log.i(TAG, "onCreate");
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

    }

}
