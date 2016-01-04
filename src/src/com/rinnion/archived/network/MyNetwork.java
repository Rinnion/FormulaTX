package com.rinnion.archived.network;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Settings;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.helper.CommentHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.Profile;
import com.rinnion.archived.database.model.User;
import com.rinnion.archived.network.handlers.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

/**
 * Describes all Network operations
 */
public final class MyNetwork {

    private static final String TAG = "MyNetwork";

    //Загрузка данных с сервера
    public static Bundle queryWeather(String country) {
        Log.d(TAG, String.format("query wheather: %s", country));
        final DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        String credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS);

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher = builder.setName("queryMessages")
                .setGetRequest("http://api.openweathermap.org/data/2.5/weather?q=" + country + ",ru&units=metric&appid=d20301f9f0795290b4e28b322f0f355d")
                .setHandler(new WeatherHandler(country))
                .create();

        return fetcher.execute();
    }

    //Загрузка списка турниров
    public static Bundle queryTournamentsList() {
        Log.d(TAG, String.format("query tournaments"));
        final DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = null;
        try {
            fetcher = builder.setName("queryTournamentsList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagebydisplaymethod.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagebydisplaymethod.DISPLAY_METHOD_OBJECT)
                    .setHandler(new TournamentListHandler())
                    .create();

        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Error while server request", e);
            Bundle bundle = new Bundle();
            bundle.putString("RESULT", "EXCEPTION");
            bundle.putSerializable("EXCEPTION", e);
            return bundle;
        }
        return fetcher.execute();
    }

    //Загрузка списка турниров
    public static int[] queryGamerList(String parent) {
        Log.d(TAG, String.format("query gamers"));
        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;
        Bundle execute;
        try {
            fetcher = builder.setName("queryGamerList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparentdisplaymethod.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparentdisplaymethod.getGamers(parent))
                    .setHandler(new GamerListHandler())
                    .create();
            execute = fetcher.execute();
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Error while server request", e);
            Bundle bundle = new Bundle();
            bundle.putString("RESULT", "EXCEPTION");
            bundle.putSerializable("EXCEPTION", e);
            return new int[0];
        }


        Bundle bundle = execute.getBundle(HttpRequester.RESULT_HTTP);
        if (bundle != null){
            return bundle.getIntArray(GamerListHandler.VALUE);
        }
        return new int[0];

    }


    //Загрузка списка новостей турнира
    public static Bundle queryTournamentNewsList(String tournamentTranslation) {
        Log.d(TAG, String.format("query queryTournamentNewsList"));
        final DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = null;
        String strPost;
        try {
            fetcher = builder.setName("queryTournamentNewsList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparent.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparent.getParent(tournamentTranslation))
                    .setHandler(new NewsHandler())
                    .create();

        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Error while server request", e);
            Bundle bundle = new Bundle();
            bundle.putString("RESULT", "EXCEPTION");
            bundle.putSerializable("EXCEPTION", e);
            return bundle;
        }

        return fetcher.execute();
    }

    //Загрузка списка новостей турнира
    public static Bundle queryGallery(long id) {
        Log.d(TAG, String.format("queryGallery"));
        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher;
        try {
            fetcher = builder.setName("queryTournamentNewsList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.Gallery.getgallery.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.Gallery.getgallery.getUrl(id))
                    .setHandler(new GalleryHandler(id))
                    .create();

        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Error while server request", e);
            Bundle bundle = new Bundle();
            bundle.putString("RESULT", "EXCEPTION");
            bundle.putSerializable("EXCEPTION", e);
            return bundle;
        }

        return fetcher.execute();
    }

    //Загрузка турнира
    public static Bundle queryTournament(int id) {
        Log.d(TAG, String.format("query tournament"));
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        return queryObjects(id, new TournamentHandler(th));
    }

    //Загрузка объектов
    public static Bundle queryObjects(int id, ApiObjectHandler handler) {
        Log.d(TAG, String.format("query tournament"));
        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;
        try {

            fetcher = builder.setName("queryObjects")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getpage.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getpage.getObject(String.valueOf(id)))
                    .setHandler(handler)
                    .create();
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Error while server request", e);
            Bundle bundle = new Bundle();
            bundle.putString("RESULT", "EXCEPTION");
            bundle.putSerializable("EXCEPTION", e);
            return bundle;
        }

        return fetcher.execute();
    }


}
