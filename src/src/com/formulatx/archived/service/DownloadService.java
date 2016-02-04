package com.formulatx.archived.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.formulatx.archived.network.HttpRequester;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.network.handlers.ApiObjectHandler;
import com.formulatx.archived.network.handlers.TournamentHandler;
import com.formulatx.archived.network.loaders.cursor.WeatherCursor;
import com.formulatx.archived.Settings;
import com.formulatx.archived.database.helper.TwitterHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.utils.NetworkConnectionCheck;
import com.rinnion.archived.R;
import org.json.JSONException;
import org.lorecraft.phparser.SerializedPhpParser;

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

        FormulaTXApplication.setParameter(Settings.LOADING_TYPE, Settings.LOADING_PROGRESS);
        FormulaTXApplication.setParameter(Settings.LOADING_PROGRESS, 0);
        FormulaTXApplication.setParameter(Settings.LOADING_CUSTOM_MESSAGE, "");

        return super.onStartCommand(intent, flags, startId);
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
                    /*
            publishProgress(5, null);
            MyNetwork.queryWeather(WeatherCursor.MOSCOW);
            publishProgress(10, null);
            MyNetwork.queryWeather(WeatherCursor.PETERSBURG);

            loadAbout(Settings.ABOUT_API_OBJECT);
            //publishError("Network error", (Settings.DEBUG) ? "Couldn't load about" : null);
            publishProgress(20, null);

            if (!FetchTournamentsList(20, 50)) {
                return;
            }
            if (!FetchAreasList(50, 70)){
                return;
            }
            if (!PreLoadNews(70, 95)){
                return;
            }


                                       /**/
            publishProgress(100, null);
        } catch (Exception ex) {
            Log.e(TAG, "Error during handle intent", ex);
            publishError(getString(R.string.string_connection_error), ex.getMessage());
        }
    }

    private boolean PreLoadNews(int i, int i1) {
        fetchTournamentNews(TournamentHelper.TOURNAMENT_LADIES_TROPHY);
        publishProgress(i+(i1-i)/2, "");
        fetchTournamentNews(TournamentHelper.TOURNAMENT_OPEN);
        return true;
    }

    private boolean fetchTournamentNews(String tn) {
        Bundle bundle;
        TournamentHelper th = new TournamentHelper(FormulaTXApplication.getDatabaseOpenHelper());
        Tournament ladies = th.getByPostName(tn);
        bundle = MyNetwork.queryTournamentNewsList(ladies.id, ladies.post_name);
        String result = bundle.getString(HttpRequester.RESULT);
        boolean equals = result.equals(HttpRequester.RESULT_HTTP);
        if (!equals){
            String mess = bundle.getString(result);
            publishError(getString(R.string.string_connection_error), (Settings.DEBUG) ? mess : null);
            return false;
        }
        return true;
    }

    private boolean loadAbout(int aboutApiObject) {
        Bundle bundle = MyNetwork.queryApiObject(aboutApiObject, new ApiObjectHandler());
        String result = bundle.getString(HttpRequester.RESULT);
        boolean equals = result.equals(HttpRequester.RESULT_HTTP);
        if (!equals){
            String mess = bundle.getString(result);
            publishError(getString(R.string.string_connection_error), (Settings.DEBUG) ? mess : null);
        }
        return equals;
    }

    private boolean FetchTournamentsList(int startProgress, int endProgress) throws JSONException {

        final int firstStep = 5;

        Bundle tournaments = MyNetwork.queryTournamentList();
        int[] intArray = MyNetwork.getIntArray(tournaments);
        startProgress += firstStep;
        publishProgress(startProgress, null);
        if (intArray == null) {
            publishError(getString(R.string.string_connection_error), (Settings.DEBUG) ? tournaments.toString() : null);
            return false;
        }

        float pr = (endProgress - startProgress) / ((intArray.length == 0) ? 1 : intArray.length);
        for (int i = 0; i < intArray.length; i++) {
            int id = intArray[i];
            MyNetwork.queryApiObject(id, new TournamentHandler());
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
            TwitterHelper aoh = new TwitterHelper(FormulaTXApplication.getDatabaseOpenHelper());
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
        FormulaTXApplication.setParameter(Settings.LOADING_TYPE, Settings.LOADING_ERROR);
        FormulaTXApplication.setParameter(Settings.LOADING_ERROR, error);
        FormulaTXApplication.setParameter(Settings.LOADING_CUSTOM_MESSAGE, custom_message);

        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(Settings.LOADING_TYPE, Settings.LOADING_ERROR);
        intent.putExtra(Settings.LOADING_ERROR, error);
        intent.putExtra(Settings.LOADING_CUSTOM_MESSAGE, custom_message);
        sendBroadcast(intent);


    }


    private void publishProgress(int progress, String custom_message) {
        Log.d(TAG, "publishProgress: " + progress);
        FormulaTXApplication.setParameter(Settings.LOADING_TYPE, Settings.LOADING_PROGRESS);
        FormulaTXApplication.setParameter(Settings.LOADING_PROGRESS, progress);
        FormulaTXApplication.setParameter(Settings.LOADING_CUSTOM_MESSAGE, custom_message);

        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(Settings.LOADING_TYPE, Settings.LOADING_PROGRESS);
        intent.putExtra(Settings.LOADING_PROGRESS, progress);
        intent.putExtra(Settings.LOADING_CUSTOM_MESSAGE, custom_message);
        sendBroadcast(intent);
    }

}