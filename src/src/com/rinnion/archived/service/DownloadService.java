package com.rinnion.archived.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Settings;
import com.rinnion.archived.database.helper.TwitterHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.utils.Log;
import org.json.JSONException;
import org.lorecraft.phparser.SerializedPhpParser;

import java.util.ArrayList;
import java.util.Map;

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

            publishProgress(15, null);
            FetchTournamentsList(intArray, 15, 80);

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

    private ArrayList<ApiObject> FetchTournamentsList(int[] intArray, int startProgress, int endProgress) throws JSONException {
        ArrayList<ApiObject> tournamentList = new ArrayList<ApiObject>(intArray.length);
        float pr = (endProgress - startProgress) / ((intArray.length == 0) ? 1 : intArray.length);
        for (int i = 0; i < intArray.length; i++) {
            int id = intArray[i];
            Bundle bundle = MyNetwork.queryApiObject(id, ApiObjectTypes.EN_Object);
            ApiObject ao = MyNetwork.getApiObjectCasted(ApiObject.class, bundle);
            FetchNewsForTournament(ao);
            FetchSocialsForTournament(ao);
            publishProgress((int)(startProgress + pr * i), null);
        }
        return tournamentList;

    }

    private void FetchNewsForTournament(ApiObject ao) throws JSONException {
        if (ao == null) return;
        int[] iaNewsList = MyNetwork.getIntArray(MyNetwork.queryTournamentNewsList(ao.id));
        if (iaNewsList == null) {
            return;
        }
        FetchApiObjectsList(iaNewsList, ApiObjectTypes.EN_News);
    }

    private void FetchSocialsForTournament(ApiObject ao) throws JSONException {
        if (ao == null) return;
        String references_include = ao.references_include;
        Log.d(TAG, String.valueOf(references_include));
        SerializedPhpParser php = new SerializedPhpParser(references_include);
        Map parse = (Map) php.parse();
        TwitterHelper aoh = new TwitterHelper(ArchivedApplication.getDatabaseOpenHelper());
        for (Object item : parse.keySet()) {
            Log.d(TAG, "key:'" + String.valueOf(item) + "'");
            try {
                String value = parse.get(item).toString();
                Log.d(TAG, "value:'" + String.valueOf(value) + "'");
                long l = Long.parseLong(value);
                MyNetwork.queryTwitter(l);
                aoh.attachReference(ao.id, l);
            } catch (Exception ignored) {
            }
        }
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