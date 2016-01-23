package com.rinnion.archived;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.rinnion.archived.utils.Files;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.helper.SettingsHelper;
import com.rinnion.archived.utils.MyLocale;

import java.io.File;
import java.io.IOException;

/**
 * Created by tretyakov on 08.05.2014.
 */
public class ArchivedApplication extends Application {
    private final static String TAG = "ArchivedApplication";

    @Override
    public File getCacheDir() {
        return Files.getExternalCachePath();
        /*return super.getCacheDir();*/
    }

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

    public static String getStringParameter(String parameter) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        return sh.getStringParameter(parameter);
    }

    public static int getIntParameter(String parameter, int i) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        return sh.getIntParameter(parameter, i);
    }

    public static double getDoubleParameter(String parameter, double d) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        return sh.getDoubleParameter(parameter, d);
    }

    public static void setParameter(String parameter, String value) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        sh.setParameter(parameter, value);
    }

    public static void setParameter(String parameter, int value) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        sh.setParameter(parameter, value);
    }

    public static void setParameter(String parameter, double value) {
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

        Parse.initialize(this, "b51B16Td0EF2fQFsXYIKPoQv49047k2AiI9pEg41", "DVRPRwdQnlUnelGBXHA7bVqqfmUYHqC45zdXGbcV");

        ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        currentInstallation.saveInBackground();



        Log.i(TAG, "onCreate");
    }

    @Override
    public void onTerminate() {
        Log.i(TAG, "onTerminate");
        super.onTerminate();

    }

    @Override
    public void onLowMemory() {
        // In-memory caches should be thrown overboard here
        Log.i(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "onConifgurationChanged");
        super.onConfigurationChanged(newConfig);
    }

}
