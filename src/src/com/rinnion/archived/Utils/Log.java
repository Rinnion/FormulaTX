package com.rinnion.archived.utils;


import android.os.Environment;
import com.rinnion.archived.Settings;

/**
 * Created by alekseev on 11.01.2016.
 */
public class Log {


    static private String logFile;



    public static void Initialize()
    {
        if ((Settings.DEBUG) && (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))) {
            logFile=Files.getExternalDir("FormulaTX.log"); //Files.getTmpFile("AppFTXLog");
        }
        else {
            logFile = Files.getCacheDir("FormulaTX.log");
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

    public static void d(String tag,String msg,Exception ex)
    {
        writeToFile("dbg",tag,msg,ex);
        android.util.Log.d(tag, msg,ex);
    }

    public static void d(String tag,String msg)
    {
        writeToFile("dbg",tag,msg);
        android.util.Log.d(tag,msg);
    }

    public static void i(String tag,String msg,Exception ex)
    {
        writeToFile("inf",tag,msg,ex);
        android.util.Log.i(tag, msg,ex);
    }
    public static void i(String tag,String msg)
    {
        writeToFile("inf",tag,msg);
        android.util.Log.i(tag, msg);
    }

    public static void e(String tag,String msg,Exception ex)
    {
        writeToFile("err",tag,msg,ex);
        android.util.Log.e(tag, msg,ex);
    }
    public static void e(String tag,String msg)
    {
        writeToFile("err",tag,msg);
        android.util.Log.e(tag, msg);
    }


    public static void v(String tag,String msg,Exception ex)
    {
        writeToFile("v",tag,msg,ex);
        android.util.Log.v(tag, msg,ex);
    }
    public static void v(String tag,String msg)
    {
        writeToFile("v",tag,msg);
        android.util.Log.v(tag, msg);
    }

    public static void w(String tag,String msg,Exception ex)
    {
        writeToFile("wrn",tag,msg,ex);
        android.util.Log.w(tag, msg,ex);
    }
    public static void w(String tag,String msg)
    {
        writeToFile("wrn",tag,msg);
        android.util.Log.w(tag, msg);
    }

}
