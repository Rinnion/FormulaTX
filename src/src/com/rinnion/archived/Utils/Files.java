package com.rinnion.archived.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Settings;

import java.io.*;

/**
 * Created by alekseev on 11.01.2016.
 */
public class Files {

    static File mFileDir;
    static File mFileCached;
    static File mFileExternal;
    static Object syncWriteObject;


    public static File getExternalCachePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalStorageDir = Environment.getExternalStorageDirectory();
            // {SD_PATH}/Android/data/com.package.name/cache

            File extStorageAppCachePath = new File(externalStorageDir, Settings.EXTERNAL_PATH + File.separator + "cache");
            externalStorageDir.mkdir();
            return extStorageAppCachePath;
        }

        return null;
    }


    public static File getFileExternal()
    {
        return mFileExternal;
    }

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

    public  static String getExternalDir(String filename)
    {
        return getExternalDir("", filename);
    }

    public  static String getExternalDir(String path, String filename)
    {


        File fileDir=new File(mFileExternal,Settings.EXTERNAL_PATH + ((path==null || path.isEmpty())? "": (File.separatorChar + path))) ;




        fileDir.mkdir();

        File file=new File(fileDir, filename);


        /*if(createSuccess)*/

        /*else
            file= new File(mFileExternal, combinedString);*/

        return  file.getAbsolutePath();
    }

    public  static String getExternalDir()
    {
        File fileDir=new File(mFileExternal, Settings.EXTERNAL_PATH);



        fileDir.mkdir();
        return  fileDir.getAbsolutePath();
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

    public static byte[] getFileAllBytes(String filePath)
    {
        File file = new File(filePath);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }



}
