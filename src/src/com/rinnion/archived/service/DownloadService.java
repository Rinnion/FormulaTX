package com.rinnion.archived.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.rinnion.archived.network.MyNetwork;

public class DownloadService extends IntentService {

    public static final String PROGRESS = "progress";
    public static final String TYPE = "type";
    public static final String NOTIFICATION = "com.rinnion.archived.service.receiver";
    private String TAG = getClass().getSimpleName();

    public DownloadService() {
        super("DownloadService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            publishResults(10);
            //ask for weather
            Bundle bundleTurnirList = MyNetwork.queryTournamentsList();
            publishResults(20);

            Bundle bundleTurnir = MyNetwork.queryTournaments(208);

            //list of tournaments
            Thread.sleep(2000);
            publishResults(50);
            //list of objects
            Thread.sleep(2000);
            publishResults(80);
            //list of news for tournaments
            Thread.sleep(2000);
        } catch (Exception ex) {
            Log.e(TAG, "Error during handle intent", ex);
        }
        publishResults(100);
    }

    private void publishResults(int result) {
        Log.d(TAG, "publishResults: " + result);
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(TYPE, PROGRESS);
        intent.putExtra(PROGRESS, result);
        sendBroadcast(intent);
    }
} 