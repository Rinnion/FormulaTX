package com.formulatx.archived.utils;


import android.os.Environment;
import com.formulatx.archived.Settings;

/**
 * Created by alekseev on 11.01.2016.
 */
public class Log {

    public static final int VERBOSE = android.util.Log.VERBOSE;
    public static final int DEBUG = android.util.Log.DEBUG;
    public static final int INFO = android.util.Log.INFO;
    public static final int WARN = android.util.Log.WARN;
    public static final int ERROR = android.util.Log.ERROR;
    public static final int ASSERT = android.util.Log.ASSERT;

    static private String logFile;



    public static void Initialize() {
        if ((Settings.DEBUG) && (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))) {
            logFile = Files.getExternalDir("FormulaTX.txt");
        } else {
            logFile = Files.getCacheDir("FormulaTX.txt");
        }
    }

    private static void writeToFile(String method,String tag,String msg)
    {
        String string="[%s][%s] %s\n";
        Files.WriteToFile(logFile,String.format(string,method,tag,msg) );
    }

    private static void writeToFile(String method,String tag,String msg,Exception ex)
    {
        String string="[%s][%s] %s \nException:\n";
        Files.WriteToFile(logFile,String.format(string,method,tag,msg),ex );
    }

    private static boolean isLevelDebug() {
        return Settings.LogLevel <= DEBUG;
    }

    private static boolean isLevelInfo() {
        return Settings.LogLevel <= INFO;
    }

    private static boolean isLevelError() {
        return Settings.LogLevel <= ERROR;
    }

    private static boolean isLevelVerbose() {
        return Settings.LogLevel <= VERBOSE;
    }

    private static boolean isLevelWarning() {
        return Settings.LogLevel <= WARN;
    }

    public static void d(String tag,String msg,Exception ex)
    {
        if (!isLevelDebug()) return;
        writeToFile("dbg",tag,msg,ex);
        android.util.Log.d(tag, msg,ex);
    }

    public static void d(String tag,String msg)
    {
        if (!isLevelDebug()) return;
        writeToFile("dbg",tag,msg);
        android.util.Log.d(tag,msg);
    }

    public static void i(String tag,String msg,Exception ex)
    {
        if (!isLevelInfo()) return;
        writeToFile("inf",tag,msg,ex);
        android.util.Log.i(tag, msg,ex);
    }
    public static void i(String tag,String msg)
    {
        if (!isLevelInfo()) return;
        writeToFile("inf",tag,msg);
        android.util.Log.i(tag, msg);
    }

    public static void e(String tag,String msg,Exception ex)
    {
        if (!isLevelError()) return;
        writeToFile("err",tag,msg,ex);
        android.util.Log.e(tag, msg,ex);
    }
    public static void e(String tag,String msg)
    {
        if (!isLevelError()) return;
        writeToFile("err",tag,msg);
        android.util.Log.e(tag, msg);
    }

    public static void v(String tag,String msg,Exception ex)
    {
        if (!isLevelVerbose()) return;
        writeToFile("v",tag,msg,ex);
        android.util.Log.v(tag, msg,ex);
    }
    public static void v(String tag,String msg)
    {
        if (!isLevelVerbose()) return;
        writeToFile("v",tag,msg);
        android.util.Log.v(tag, msg);
    }

    public static void w(String tag,String msg,Exception ex)
    {
        if (!isLevelWarning()) return;
        writeToFile("wrn",tag,msg,ex);
        android.util.Log.w(tag, msg,ex);
    }
    public static void w(String tag,String msg)
    {
        if (!isLevelWarning()) return;
        writeToFile("wrn",tag,msg);
        android.util.Log.w(tag, msg);
    }

}
