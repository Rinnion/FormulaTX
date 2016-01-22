package com.rinnion.archived.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Settings;
import com.rinnion.archived.database.helper.TwitterHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.network.HttpRequester;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.utils.Log;
import org.json.JSONException;
import org.lorecraft.phparser.SerializedPhpParser;

import java.util.ArrayList;
import java.util.Map;

public class DownloadService extends IntentService {

    public static final String TYPE = "type";

    public static final String NOTIFICATION = "com.rinnion.archived.service.receiver";
    private String TAG = getClass().getSimpleName();

    public DownloadService() {
        super("DownloadService");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArchivedApplication.setParameter(Settings.LOADING_TYPE, Settings.LOADING_PROGRESS);
        ArchivedApplication.setParameter(Settings.LOADING_PROGRESS, String.valueOf(0));
        ArchivedApplication.setParameter(Settings.LOADING_CUSTOM_MESSAGE, "");
        return super.onStartCommand(intent, flags, startId);
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if (!loadAbout(Settings.ABOUT_API_OBJECT)){
                publishError("Network error", (Settings.DEBUG) ? "Couldn't load about" : null);
                return;
            }

            FetchTournamentsList(10, 50);
            FetchAreasList(50, 95);

            publishProgress(100, null);
        } catch (Exception ex) {
            Log.e(TAG, "Error during handle intent", ex);
            publishError("Error during network ", ex.getMessage());
        }
    }

    private boolean loadAbout(int aboutApiObject) {
        Bundle bundle = MyNetwork.queryApiObject(aboutApiObject, ApiObjectTypes.EN_About);
        String result = bundle.getString(HttpRequester.RESULT);
        boolean equals = result.equals(HttpRequester.RESULT_HTTP);
        if (!equals){
            String mess = bundle.getString(result);
            publishError("Network error", (Settings.DEBUG) ? mess : null);
        }
        return equals;
    }

    private ArrayList<ApiObject> FetchApiObjectsList(int[] intArray, int type) throws JSONException {
        ArrayList<ApiObject> tournamentList = new ArrayList<ApiObject>(intArray.length);
        for (int anIntArray : intArray) {
            MyNetwork.queryApiObject(anIntArray, type);
        }
        return tournamentList;

    }

    private boolean FetchTournamentsList(int startProgress, int endProgress) throws JSONException {

        final int firstStep = 5;

        Bundle tournaments = MyNetwork.queryTournamentList();
        int[] intArray = MyNetwork.getIntArray(tournaments);
        startProgress += firstStep;
        publishProgress(startProgress, null);
        if (intArray == null) {
            publishError("Network error", (Settings.DEBUG) ? tournaments.toString() : null);
            return false;
        }

        float pr = (endProgress - startProgress) / ((intArray.length == 0) ? 1 : intArray.length);
        for (int i = 0; i < intArray.length; i++) {
            int id = intArray[i];
            MyNetwork.queryApiObject(id, ApiObjectTypes.EN_Object);
            publishProgress((int)(startProgress + pr * i), null);
        }
        return true;

    }

    private boolean FetchAreasList(int startProgress, int endProgress) throws JSONException {
        final int firstStep = 5;

        Bundle areas = MyNetwork.queryAreasList();
        int[] intArray = MyNetwork.getIntArray(areas);
        startProgress += firstStep;
        publishProgress(startProgress, null);

        float pr = (endProgress - startProgress) / ((intArray.length == 0) ? 1 : intArray.length);
        for (int i = 0; i < intArray.length; i++) {
            int id = intArray[i];
            MyNetwork.queryArea(id);
            publishProgress((int)(startProgress + pr * i), null);
        }

        return true;
    }

    private void FetchSocialsForTournament(ApiObject ao) throws JSONException {
        if (ao == null) return;
        try {
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
                    Log.d(TAG, "skip item " + String.valueOf(item));
                }
            }
        }catch(Exception ex){
            Log.d(TAG, "FetchSocialsForTournament exception:", ex);
        }
    }

    private void publishError(String error, String custom_message) {
        Log.d(TAG, "publishError: " + error);
        ArchivedApplication.setParameter(Settings.LOADING_TYPE, Settings.LOADING_ERROR);
        ArchivedApplication.setParameter(Settings.LOADING_ERROR, error);
        ArchivedApplication.setParameter(Settings.LOADING_CUSTOM_MESSAGE, custom_message);

        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(Settings.LOADING_TYPE, Settings.LOADING_ERROR);
        intent.putExtra(Settings.LOADING_ERROR, error);
        intent.putExtra(Settings.LOADING_CUSTOM_MESSAGE, custom_message);
        sendBroadcast(intent);


    }


    private void publishProgress(int progress, String custom_message) {
        Log.d(TAG, "publishProgress: " + progress);
        ArchivedApplication.setParameter(Settings.LOADING_TYPE, Settings.LOADING_PROGRESS);
        ArchivedApplication.setParameter(Settings.LOADING_PROGRESS, progress);
        ArchivedApplication.setParameter(Settings.LOADING_CUSTOM_MESSAGE, custom_message);

        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(Settings.LOADING_TYPE, Settings.LOADING_PROGRESS);
        intent.putExtra(Settings.LOADING_PROGRESS, progress);
        intent.putExtra(Settings.LOADING_CUSTOM_MESSAGE, custom_message);
        sendBroadcast(intent);
    }

}