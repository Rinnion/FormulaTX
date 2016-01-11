package com.rinnion.archived.network;

import android.os.Bundle;
import android.util.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Settings;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.helper.NewsHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.network.handlers.*;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
                .setGetRequest("http://api.openweathermap.org/data/2.5/weather?q=" + country + ",ru&units=metric&appid=d20301f9f0795290b4e28b322f0f355d&lang=ru")
                .setHandler(new WeatherHandler(country))
                .create();

        return fetcher.execute();
    }

    //Загрузка списка турниров
    public static Bundle queryTournamentList() {
        return queryApiObjectsList(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagebydisplaymethod.DISPLAY_METHOD_OBJECT);
    }

    //Загрузка списка турниров
    public static Bundle queryApiObjectsList(ArrayList<NameValuePair> objectType) {
        Log.d(TAG, String.format("query tournaments"));

        if (Settings.NETDEBUG){
            String type = objectType.get(0).getValue();
            String fileName = "json/"+type+".json";
            IResponseHandler mHandler = new ApiObjectListHandler();
            Bundle result = processFile(fileName, mHandler);
            return result;
        }

        final DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = null;
        try {
            fetcher = builder.setName("queryApiObjectsList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagebydisplaymethod.URL_METHOD)
                    .setContent(objectType)
                    .setHandler(new ApiObjectListHandler())
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
    public static Bundle queryGamerList(long parent) {
        Log.d(TAG, String.format("query queryTournamentNewsList"));
        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = null;
        try {
            fetcher = builder.setName("queryTournamentNewsList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparentdisplaymethod.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparentdisplaymethod.getGamer(parent))
                    .setHandler(new ApiObjectListHandler())
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
    public static Bundle queryTournamentNewsList(long tournamentTranslation) {
        Log.d(TAG, String.format("query queryTournamentNewsList"));

        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = null;
        try {
            fetcher = builder.setName("queryTournamentNewsList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparent.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparent.getParent(tournamentTranslation))
                    .setHandler(new ApiObjectListHandler())
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
        final DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
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

    //Загрузка списка новостей турнира
    public static Bundle queryTwitter(long id) {

        if (Settings.NETDEBUG){
            String fileName = "json/references-57-1.json";
            TwitterHandler mHandler = new TwitterHandler(id);
            Bundle result = processFile(fileName, mHandler);
            return result;
        }

        Log.d(TAG, String.format("queryGallery"));
        final DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher;
        try {
            fetcher = builder.setName("queryTournamentNewsList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.References.getreferencebyidapproved.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.References.getreferencebyidapproved.getUrl(id, 1))
                    .setHandler(new TwitterHandler(id))
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

    private static Bundle processFile(String fileName, IResponseHandler mHandler) {
        String response = getStringFromAsset(fileName);
        Bundle result = new Bundle();
        try{
            Bundle bundle = new Bundle();
            bundle.putInt(HttpRequester.STATUS_CODE, 200);

            result.putSerializable(HttpRequester.RESULT, HttpRequester.RESULT_HTTP);
            result.putBundle(HttpRequester.RESULT_HTTP, bundle);
            bundle.putBundle(HttpRequester.RESULT_HTTP_PARSE, mHandler.Handle(response));

        } catch (Exception e) {
            result.putSerializable(HttpRequester.RESULT, HttpRequester.RESULT_EXCEPTION);
            result.putSerializable(HttpRequester.RESULT_EXCEPTION, e);
        }
        return result;
    }

    private static String getStringFromAsset(String fileName) {
        String string = null;
        try {
            InputStreamReader in = new InputStreamReader(ArchivedApplication.getAppContext().getAssets().open(fileName));

            BufferedReader reader = new BufferedReader(in);
            String mLine;

            StringBuffer sb = new StringBuffer();
            while ((mLine = reader.readLine()) != null) {
                sb.append(mLine);
            }

            string = sb.toString();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return string;
    }

    //Загрузка объектов
    public static Bundle queryApiObject(int id, int type) {

        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        ApiObjectHandler handler = null;
        switch (type){
            case ApiObjectTypes.EN_Object: handler = new TournamentHandler(new TournamentHelper(doh));
                break;
            case ApiObjectTypes.EN_News: handler = new NewsHandler(new NewsHelper(doh));
                break;
            default:
                handler = new ApiObjectHandler(new ApiObjectHelper(ArchivedApplication.getDatabaseOpenHelper()), type);
        }

        return queryApiObject(id, handler);
    }

    public static Bundle queryApiObject(int id, ApiObjectHandler handler) {

        if (Settings.NETDEBUG){
            String fileName = "json/" + String.valueOf(id) +"ru.json";
            Bundle result = processFile(fileName, handler);
            return result;
        }

        Log.d(TAG, String.format("query tournament"));
        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;
        try {

            fetcher = builder.setName("queryApiObject")
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


    public static int[] getIntArray(Bundle bundleTurnirList) {
        int[] intArray = null;
        String result = bundleTurnirList.getString(HttpRequester.RESULT);
        if (result.equals(HttpRequester.RESULT_HTTP)){
            Bundle tmpTurnirList = bundleTurnirList.getBundle(HttpRequester.RESULT_HTTP);
            if (tmpTurnirList != null)
            {
                tmpTurnirList = tmpTurnirList.getBundle(HttpRequester.RESULT_HTTP_PARSE);
                if (tmpTurnirList != null){
                    intArray = tmpTurnirList.getIntArray("ID[]");
                }
            }
        }
        return intArray;
    }

    public static JSONObject getApiObjectJSON(Bundle objectBundle) throws JSONException {
        String result = objectBundle.getString(HttpRequester.RESULT);
        if (result.equals(HttpRequester.RESULT_HTTP)) {
            Bundle tmpTurnirList = objectBundle.getBundle(HttpRequester.RESULT_HTTP);
            if (tmpTurnirList != null) {
                tmpTurnirList = tmpTurnirList.getBundle(HttpRequester.RESULT_HTTP_PARSE);
                if (tmpTurnirList != null) {
                    return new JSONObject(tmpTurnirList.getString("ApiObject"));
                }
            }
        }
        return null;
    }

    public static <T extends ApiObject> T getApiObjectCasted(Class<T> clazz, Bundle objectBundle) throws JSONException {
        String result = objectBundle.getString(HttpRequester.RESULT);
        if (result.equals(HttpRequester.RESULT_HTTP)) {
            Bundle tmpTurnirList = objectBundle.getBundle(HttpRequester.RESULT_HTTP);
            if (tmpTurnirList != null) {
                tmpTurnirList = tmpTurnirList.getBundle(HttpRequester.RESULT_HTTP_PARSE);
                if (tmpTurnirList != null) {
                    try {
                        Object obj = tmpTurnirList.getSerializable(ApiObjectHandler.OBJECT);
                        return clazz.cast(obj);
                    }catch(ClassCastException cce){
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public static Bundle queryGamer(int id) {
        Log.d(TAG, String.format("query gamer"));
        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;
        try {

            DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
            fetcher = builder.setName("queryApiObject")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getpage.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getpage.getObject(String.valueOf(id)))
                    .setHandler(new ApiObjectHandler(new ApiObjectHelper(ArchivedApplication.getDatabaseOpenHelper()), ApiObjectTypes.EN_Gamer))
                    .create();

            fetcher.execute();

            fetcher = builder.setName("queryApiObject")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getadditionalfields.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getadditionalfields.getUrl(id))
                    .setHandler(new GamerHandler(new GamerHelper(doh)))
                    .create();

            fetcher.execute();

        } catch (Exception e) {
            Log.d(TAG, "Error while server request", e);
            Bundle bundle = new Bundle();
            bundle.putString("RESULT", "EXCEPTION");
            bundle.putSerializable("EXCEPTION", e);
            return bundle;
        }

        return fetcher.execute();
    }
}
