package com.rinnion.archived.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.rinnion.archived.Settings;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.network.MyNetwork;
import org.json.JSONException;

import java.util.ArrayList;

public class DownloadService extends IntentService {

    public static final String TYPE = "type";
    public static final String PROGRESS = "progress";
    public static final String ERROR = "error";
    public static final String CUSTOM_MESSAGE = "message";

    public static final String NOTIFICATION = "com.rinnion.archived.service.receiver";
    private String TAG = getClass().getSimpleName();

    public DownloadService() {
        super("DownloadService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            publishProgress(10, null);
            int[] intArray = MyNetwork.getIntArray(MyNetwork.queryTournamentList());
            if (intArray == null) {
                publishError("Network error", null);
                return;
            }

            publishProgress(25, null);
            FetchApiObjectsList(intArray, ApiObjectTypes.EN_Object);
            publishProgress(45, null);

            for (int id : intArray) {
                int[] iaNewsList = MyNetwork.getIntArray(MyNetwork.queryTournamentNewsList(id));
                if (iaNewsList == null) {
                    continue;
                }

                FetchApiObjectsList(iaNewsList, ApiObjectTypes.EN_News);
            }

            publishProgress(50, null);
            loadAbout(Settings.ABOUT_API_OBJECT);
            publishProgress(80, null);
            publishProgress(100, null);
        } catch (Exception ex) {
            Log.e(TAG, "Error during handle intent", ex);
            publishError("Error during network ", ex.toString());
        }

    }

    private void loadAbout(int aboutApiObject) {
        MyNetwork.queryApiObject(aboutApiObject, ApiObjectTypes.EN_About);
    }

    private ArrayList<ApiObject> FetchApiObjectsList(int[] intArray, int type) throws JSONException {
        ArrayList<ApiObject> tournamentList = new ArrayList<ApiObject>(intArray.length);
        for (int anIntArray : intArray) {
            MyNetwork.queryApiObject(anIntArray, type);
        }
        return tournamentList;

    }

    private void publishError(String error, String custom_message) {
        Log.d(TAG, "publishError: " + error);
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(TYPE, ERROR);
        intent.putExtra(ERROR, error);
        intent.putExtra(CUSTOM_MESSAGE, custom_message);
        sendBroadcast(intent);
    }


    private void publishProgress(int progress, String custom_message) {
        Log.d(TAG, "publishProgress: " + progress);
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(TYPE, PROGRESS);
        intent.putExtra(PROGRESS, progress);
        intent.putExtra(CUSTOM_MESSAGE, custom_message);
        sendBroadcast(intent);
    }

}