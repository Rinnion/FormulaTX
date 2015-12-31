package com.rinnion.archived.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
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

            ArrayList<Tournament> tournamentList=new ArrayList<Tournament>();

            int[] intArray = tmpTurnirList.getIntArray("ID[]");

            CreateTournamentList(tournamentList, intArray);


             for(Tournament tmpTournament: tournamentList) {

                 Bundle tmpTurnirNewsList = MyNetwork.queryTournamentNewsList(tmpTournament.post_name);
                 tmpTurnirNewsList = tmpTurnirNewsList.getBundle(HttpRequester.RESULT_HTTP).getBundle(HttpRequester.RESULT_HTTP_PARSE);

                 if (tmpTurnirNewsList != null) {
                     intArray = tmpTurnirNewsList.getIntArray("ID[]");


                     for (int newsIndex = 0; newsIndex < intArray.length; newsIndex++) {
                         Bundle bundleTurnirNews = MyNetwork.queryTournaments(intArray[newsIndex]);
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


            //list of tournaments
            //Thread.sleep(2000);
            publishResults(50);
            //list of objects
            //Thread.sleep(2000);
            publishResults(80);
            //list of news for tournaments
            Thread.sleep(2000);
        } catch (Exception ex) {
            Log.e(TAG, "Error during handle intent", ex);
            Toast.makeText(this,
                    ex.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        publishResults(100);
    }

    private void CreateTournamentList(ArrayList<Tournament> tournamentList, int[] intArray) throws JSONException {
        for (int bundleTournamentIndex=0;bundleTournamentIndex<intArray.length;bundleTournamentIndex++) {
            Bundle bundleTurnir = MyNetwork.queryTournaments(intArray[bundleTournamentIndex]);

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

    private void publishResults(int result) {
        Log.d(TAG, "publishResults: " + result);
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(TYPE, PROGRESS);
        intent.putExtra(PROGRESS, result);
        sendBroadcast(intent);
    }
} 