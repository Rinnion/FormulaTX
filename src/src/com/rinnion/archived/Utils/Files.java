package com.rinnion.archived.utils;

import android.content.Context;
import com.rinnion.archived.ArchivedApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by alekseev on 11.01.2016.
 */
public class Files {

    static File mFileDir;
    static File mFileCached;
    static Object syncWriteObject;
    static {
        mFileDir= ArchivedApplication.getAppContext().getFilesDir();
        mFileCached=ArchivedApplication.getAppContext().getCacheDir();
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
        //File file = new File(mFileDir, combinedString);
        File file= null;
        try {
            file = File.createTempFile(combinedString, ".log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  file.getPath();
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




}
