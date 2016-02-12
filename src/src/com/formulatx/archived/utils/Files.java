package com.formulatx.archived.utils;

import android.os.Environment;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Settings;

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
        mFileDir= FormulaTXApplication.getAppContext().getFilesDir();
        mFileCached= FormulaTXApplication.getAppContext().getCacheDir();
        mFileExternal=Environment.getExternalStorageDirectory();
        syncWriteObject=new Object();
    }

    public static String combine (String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
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

    public static void WriteToFile(String file,String string) {
        synchronized (syncWriteObject) {
            try {

                FileOutputStream outputStream = null;
                File logFile = new File(file);

                try {


                    outputStream = new FileOutputStream(logFile, true);  //FormulaTXApplication.getAppContext().openFileOutput(new File(file), Context.MODE_APPEND);
                    outputStream.write(string.getBytes());


                } catch (Exception e) {

                    e.printStackTrace();
                } finally {
                    if (outputStream != null)
                        outputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void WriteToFile(String file,String string,Exception ex)
    {


        synchronized (syncWriteObject) {
            try {

                FileOutputStream outputStream = null;
                File logFile = new File(file);
                PrintWriter pw=null;
                try {
                    outputStream = new FileOutputStream(logFile, true);  //FormulaTXApplication.getAppContext().openFileOutput(new File(file), Context.MODE_APPEND);
                    pw = new PrintWriter(outputStream);
                    try {


                        ex.printStackTrace(pw);
                        pw.flush();

                    }
                    finally {
                        if(pw!=null)
                        pw.close();
                    }
                    outputStream.write("\n".getBytes());
                    outputStream.flush();



                } catch (Exception e) {

                    e.printStackTrace();
                } finally {
                    if (outputStream != null)
                        outputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
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
