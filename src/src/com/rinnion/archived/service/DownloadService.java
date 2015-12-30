package com.rinnion.archived.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.network.HttpRequester;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.network.handlers.ApiObjectHandler;
import org.json.JSONObject;

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

            Bundle tmpTurnirList= bundleTurnirList.getBundle(HttpRequester.RESULT_HTTP).getBundle(HttpRequester.RESULT_HTTP_PARSE);


            int[] intArray = tmpTurnirList.getIntArray("ID[]");

            for (int bundleIndex=0;bundleIndex<intArray.length;bundleIndex++) {
                Bundle bundleTurnir = MyNetwork.queryTournaments(intArray[bundleIndex]);

                if (bundleTurnir.containsKey(HttpRequester.RESULT_HTTP)) {


                    Bundle tmpHttpBundle = bundleTurnir.getBundle(HttpRequester.RESULT_HTTP).getBundle(HttpRequester.RESULT_HTTP_PARSE);
                    if (tmpHttpBundle.containsKey("ApiObject")) {
                        Tournament tournament = new Tournament(new JSONObject(tmpHttpBundle.getString("ApiObject")));
                        ApiObjectHelper aoh = new ApiObjectHelper(ArchivedApplication.getDatabaseOpenHelper());
                        if (!aoh.add(tournament))
                            Log.e(TAG, "Error when insert 'Tournament' data to DB");
                        else
                            Log.d(TAG, "'Tournament' success added to DB id='" + tournament.id + "'" );
                    }
                }
            }


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