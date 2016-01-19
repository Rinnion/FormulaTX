package com.rinnion.archived;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.rinnion.archived.network.MyNetworkContentContract;
import com.rinnion.archived.utils.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 01.02.14
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static final int REQUEST_TAKE_PHOTO = 2;
    private static final String TAG = Utils.class.getSimpleName();

    public static Date ConvertJSONStringToDate(String jsonDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateTime = sdf.parse(jsonDate);
        return dateTime;
    }

    /*
    Compress image from path sourcePath to targetPath with new size width, height
    */
    public static Bitmap compressImage(String sourcePath, String targetPath, int width, int height) {
        // Get the dimensions of the View
        int targetW = width;//ivRegPhoto.getWidth();
        int targetH = height;//ivRegPhoto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(sourcePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(sourcePath, bmOptions);

        saveBitmap(bitmap, targetPath);
        return bitmap;
    }

    public static int getImageOrientation(Context context, String imagePath) {
        int orientation = getOrientationFromExif(imagePath);
        if (orientation <= 0) {
            //orientation = getOrientationFromMediaStore(context, imagePath);
        }

        return orientation;
    }

    private static int getOrientationFromExif(String imagePath) {
        int orientation = -1;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;

                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    orientation = 0;

                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to get image exif orientation", e);
        }

        return orientation;
    }

    private static void saveBitmap(Bitmap bitmap, String targetPath) {
        File dir = new File(targetPath);

        if (!dir.exists()) dir.mkdirs();

        File file = new File(targetPath);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "FileNotFound", e);
        } catch (IOException e) {
            Log.d(TAG, "IOException", e);
        }
    }

    public static File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */);

        return image;
    }

    public static String runPictureIntent(Activity registerActivity, int requestTakePhoto) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                registerActivity.startActivityForResult(takePictureIntent, requestTakePhoto);
                return photoFile.getAbsolutePath();
            }
        } catch (IOException ioex) {
            Log.e("runPictureIntent", "Couldn't take photo");
            AlertDialog.Builder builder = new AlertDialog.Builder(registerActivity);
            //FIXME: should not use resource string locally
            builder.setTitle(registerActivity.getString(R.string.title_photography))
                    .setMessage(registerActivity.getString(R.string.message_cannot_take_photo) + ioex.getMessage());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return null;
    }


    public static HashMap getHashMap(JSONObject object) throws JSONException {
        HashMap map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, object.get(key).toString());
        }
        return map;
    }

    public static long getTimeStamp(JSONObject object, String beginTime) throws JSONException {
        try {
            String timeStart = object.getString(beginTime);
            return ConvertJSONStringToDate(timeStart).getTime();
        } catch (ParseException pe) {
            Log.d(TAG, String.format("Wrong date format at field '%s'", beginTime));
            throw new JSONException("Wrong date format: '%s'");
        }
    }

    public static boolean isTomorrow(Calendar c2) {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        return (tomorrow.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && tomorrow.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isToday(Calendar c2) {
        Calendar today = Calendar.getInstance();
        return (today.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isYesterday(Calendar c2) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        return (yesterday.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && yesterday.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static void rotateImage(String photo, String targetPhoto, int imageOrientation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(imageOrientation);

        Bitmap sourceBitmap = BitmapFactory.decodeFile(photo);

        Bitmap bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);

        saveBitmap(bitmap, targetPhoto);

    }

    public static void uploadPhoto(String filepath, String photoUploadPath, String method) {
        if (photoUploadPath == null || photoUploadPath.equals("")) {
            Log.e(TAG, "photoUploadPath should be defined");
            return;
        }
        if (filepath == null || filepath.equals("")) {
            Log.e(TAG, "filename should be defined");
            return;
        }

        Log.d(TAG, String.format("Upload '%s' to '%s'", filepath, photoUploadPath));


        try {
            URL photoUploadPURL = new URL(photoUploadPath);

            // Set your file path here
            FileInputStream fstrm = new FileInputStream(filepath);
            String filename = Uri.parse(filepath).getLastPathSegment();

            String credentials = ArchivedApplication.getStringParameter(Settings.CREDENTIALS);

            SendFile(photoUploadPURL, method, credentials, fstrm, filename, "no description");

        } catch (FileNotFoundException e) {
            Log.e(TAG, "file not found...", e);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Cannot send...", e);
        }
    }

    public static void SendFile(URL connectURL, String method, String credentials, FileInputStream fileInputStream, String title, String description) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag = "fSnd";
        try {
            Log.e(Tag, "Starting Http File Sending to URL");

            // Open a HTTP connection to the URL
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod(method);

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            conn.setRequestProperty("Authorization", "Basic " + credentials);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"title\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(title);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"description\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(description);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + title + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.d(Tag, "Headers are written");

            // create a buffer of maximum size
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            fileInputStream.close();

            dos.flush();

            Log.d(Tag, "File Sent, Response: " + String.valueOf(conn.getResponseCode()));

            InputStream is = conn.getInputStream();

            // retrieve the response from server
            int ch;

            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            Log.d("Response", s);
            dos.close();
        } catch (MalformedURLException ex) {
            Log.e(Tag, "URL error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
        }
    }

    public static String fixUrlWithFullPath(String url) {
        if (url.startsWith("/")) url = MyNetworkContentContract.URL + url.substring(1);
        return url;
    }
}

