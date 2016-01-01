package com.rinnion.archived.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.database.model.News;
import com.rinnion.archived.network.HttpRequester;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.network.handlers.ApiObjectHandler;
import org.json.JSONException;
import org.json.JSONObject;

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
            Bundle bundleTurnirList = MyNetwork.queryTournamentsList();
            String result = bundleTurnirList.getString(HttpRequester.RESULT);
            if (!result.equals(HttpRequester.RESULT_HTTP)){
                publishError(result, bundleTurnirList.getString(result));
                return;
            }

            Bundle tmpTurnirList = bundleTurnirList.getBundle(HttpRequester.RESULT_HTTP);
            if (tmpTurnirList == null) {publishError("No parse http", null); return;}
            tmpTurnirList = tmpTurnirList.getBundle(HttpRequester.RESULT_HTTP_PARSE);
            publishProgress(15, null);
            ArrayList<Tournament> tournamentList=new ArrayList<Tournament>();
            int[] intArray = tmpTurnirList.getIntArray("ID[]");
            publishProgress(25, null);

            CreateTournamentList(tournamentList, intArray);

            for(Tournament tmpTournament: tournamentList) {
                publishProgress(30, null);
                Bundle tmpTurnirNewsList = MyNetwork.queryTournamentNewsList(tmpTournament.post_name);
                publishProgress(35, null);
                result = tmpTurnirNewsList.getString(HttpRequester.RESULT);
                if (!result.equals(HttpRequester.RESULT_HTTP)) continue;
                Bundle bundle = tmpTurnirNewsList.getBundle(result);
                if (bundle == null) continue;
                tmpTurnirNewsList = bundle.getBundle(HttpRequester.RESULT_HTTP_PARSE);


                if (tmpTurnirNewsList != null) {
                    intArray = tmpTurnirNewsList.getIntArray("ID[]");


                    for (int newsIndex = 0; newsIndex < intArray.length; newsIndex++) {
                        Bundle bundleTurnirNews = MyNetwork.queryObjects(intArray[newsIndex], new ApiObjectHandler());
                        Bundle tmpHttpBundle = bundleTurnirNews.getBundle(HttpRequester.RESULT_HTTP).getBundle(HttpRequester.RESULT_HTTP_PARSE);
                        if (tmpHttpBundle.containsKey("ApiObject")) {
                            News news = new News(new JSONObject(tmpHttpBundle.getString("ApiObject")));
                            ApiObjectHelper aoh = new ApiObjectHelper(ArchivedApplication.getDatabaseOpenHelper());
                            if (!aoh.add(news))
                                Log.e(TAG, "Error when insert 'News' data to DB");
                            else {

                                Log.d(TAG, "'News' success added to DB id='" + news.id + "'");
                            }
                        }
                    }

                }
            }


            publishProgress(50, null);
            publishProgress(80, null);
            publishProgress(100, null);
        } catch (Exception ex) {
            Log.e(TAG, "Error during handle intent", ex);
            publishError("Error during network", ex.toString());
        }

    }

    private void CreateTournamentList(ArrayList<Tournament> tournamentList, int[] intArray) throws JSONException {
        for (int bundleTournamentIndex=0;bundleTournamentIndex<intArray.length;bundleTournamentIndex++) {
            Bundle bundleTurnir = MyNetwork.queryTournament(intArray[bundleTournamentIndex]);
            if (bundleTurnir.containsKey(HttpRequester.RESULT_HTTP)) {
                Bundle tmpHttpBundle = bundleTurnir.getBundle(HttpRequester.RESULT_HTTP).getBundle(HttpRequester.RESULT_HTTP_PARSE);
                if (tmpHttpBundle.containsKey("ApiObject")) {
                    Tournament tournament = new Tournament(new JSONObject(tmpHttpBundle.getString("ApiObject")));
                    ApiObjectHelper aoh = new ApiObjectHelper(ArchivedApplication.getDatabaseOpenHelper());
                    if (!aoh.add(tournament))
                        Log.e(TAG, "Error when insert 'Tournament' data to DB");
                    else {
                        tournamentList.add(tournament);
                        Log.d(TAG, "'Tournament' success added to DB id='" + tournament.id + "'");
                    }
                }
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