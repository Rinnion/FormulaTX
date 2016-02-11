package com.formulatx.archived;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.rinnion.archived.R;
import com.formulatx.archived.utils.Files;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.helper.SettingsHelper;
import com.formulatx.archived.utils.MyLocale;

import java.io.File;

/**
 * Created by tretyakov on 08.05.2014.
 */
public class FormulaTXApplication extends Application {
    private final static String TAG = "FormulaTXApplication";

    @Override
    public File getCacheDir() {
        return Files.getExternalCachePath();
        /*return super.getCacheDir();*/
    }

    public static SettingAccessor Settings;
    private static DatabaseOpenHelper doh;
    private static Context context;

    public static Context getAppContext() {
        return FormulaTXApplication.context;
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
    public static long getLongParameter(String parameter, long l) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        return sh.getLongParameter(parameter, l);
    }

    public static double getDoubleParameter(String parameter, double d) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        return sh.getDoubleParameter(parameter, d);
    }

    public static Bundle getBundleParameter(String parameter, Bundle b) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        return sh.getBundleParameter(parameter, b);
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

    public static void setParameter(String parameter, long value) {
        SettingsHelper sh = new SettingsHelper(getDatabaseOpenHelper());
        sh.setParameter(parameter, value);
    }

    public static void setParameter(String parameter, Bundle value) {
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

        FormulaTXApplication.context = getApplicationContext();
        FormulaTXApplication.Settings = new SettingAccessor();
        Files.Initialize();
        Log.Initialize();
        MyLocale.Initialize();

        //Parse.initialize(this, "b51B16Td0EF2fQFsXYIKPoQv49047k2AiI9pEg41", "DVRPRwdQnlUnelGBXHA7bVqqfmUYHqC45zdXGbcV"); //FormulaTX
        //Parse.initialize(this, "pEGaA2qIqCmEsCEwejFxNyGs1bPUBLXeqDMOcwxs", "reX8ztBii9yEqZF5lxjTnOK4k3R2AHteA4rfu69x"); //MB
        Parse.initialize(this);
        ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        currentInstallation.saveInBackground();

        ParsePush.subscribeInBackground("formula");

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
