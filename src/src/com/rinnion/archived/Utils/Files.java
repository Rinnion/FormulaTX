package com.rinnion.archived.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.rinnion.archived.ArchivedApplication;

import java.io.*;

/**
 * Created by alekseev on 11.01.2016.
 */
public class Files {

    static File mFileDir;
    static File mFileCached;
    static File mFileExternal;
    static Object syncWriteObject;


    public static void Initialize()
    {
        mFileDir= ArchivedApplication.getAppContext().getFilesDir();
        mFileCached=ArchivedApplication.getAppContext().getCacheDir();
        mFileExternal=Environment.getExternalStorageDirectory();
        syncWriteObject=new Object();
    }

    public static String combine (String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }



    public static String getTmpFile(String combinedString)
    {

        File file= null;
        try {
            file = File.createTempFile(combinedString, ".log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  file.getPath();
    }

    public  static String getExternalDir(String combinedString)
    {
        File file = new File(mFileExternal, combinedString);
        return  file.getAbsolutePath();
    }

    public  static String getExternalDir()
    {
        return  mFileExternal.getAbsolutePath();
    }

    public static String getFilesDir(String combinedString)
    {



        File file = new File(mFileDir, combinedString);
        return  file.getPath();
    }
    public static String getCacheDir(String combinedString)
    {
        File file = new File(mFileCached, combinedString);

        return  file.getPath();
    }


    public static String getFilesDir()
    {

        return  mFileDir.getPath();
    }

    public static String getCacheDir()
    {
        return  mFileCached.getPath();
    }

    public static void WriteToFile(String file,String string)
    {
        FileOutputStream outputStream=null;

        try {
            synchronized (syncWriteObject) {

                outputStream =new FileOutputStream(new File(file),true);  //ArchivedApplication.getAppContext().openFileOutput(new File(file), Context.MODE_APPEND);
                outputStream.write(string.getBytes());
                outputStream.close();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void WriteToFile(String file,String string,Exception ex)
    {
        FileOutputStream outputStream=null;

        try {
            synchronized (syncWriteObject) {

                outputStream =new FileOutputStream(new File(file),true);  //ArchivedApplication.getAppContext().openFileOutput(new File(file), Context.MODE_APPEND);
                outputStream.write(string.getBytes());
                PrintWriter pw=new PrintWriter(outputStream);
                ex.printStackTrace(pw);
                pw.flush();
                outputStream.write("\n".getBytes());
                outputStream.flush();
                outputStream.close();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }



}
