package com.formulatx.archived.network;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.fragment.Schedule;
import com.formulatx.archived.network.handlers.*;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.Settings;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.utils.MyLocale;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Describes all Network operations
 */
public final class MyNetwork {

    private static final String TAG = "MyNetwork";

    //Загрузка данных с сервера
    public static Bundle queryWeather(String country) {
        Log.d(TAG, String.format("query wheather: %s", country));
        final DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        String credentials = FormulaTXApplication.getStringParameter(Settings.CREDENTIALS);

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher = builder.setName("queryMessages")
                .setGetRequest("http://api.openweathermap.org/data/2.5/weather?q=" + country + "," + MyLocale.getCurrent() + "&units=metric&appid=d20301f9f0795290b4e28b322f0f355d&lang=" + MyLocale.getCurrent())
                .setHandler(new WeatherHandler(country))
                .create();

        return fetcher.execute();
    }

    //Загрузка списка турниров
    public static Bundle queryTournamentList() {
        return queryApiObjectsList(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagebydisplaymethod.DISPLAY_METHOD_OBJECT);
    }

    public static Bundle queryAreasList() {
        return queryApiObjectsList(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagebydisplaymethod.DISPLAY_METHOD_AREA);
    }

    //Загрузка списка турниров
    public static Bundle queryApiObjectsList(ArrayList<NameValuePair> objectType) {
        Log.d(TAG, String.format("query tournaments"));

        if (Settings.NETDEBUG) {
            String type = objectType.get(0).getValue();
            String fileName = "json/" + type + ".json";
            IResponseHandler mHandler = new ApiObjectListHandler();
            Bundle result = processFile(fileName, mHandler);
            return result;
        }

        final DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
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
    public static Bundle queryGamerList(long id) {
        Log.d(TAG, String.format("queryGamerList"));

        ApiObjectListHandler handler = new ApiObjectListHandler();

        if (Settings.NETDEBUG) {
            String fileName = "json/" + String.valueOf(id) + "gamer.json";
            Bundle result = processFile(fileName, handler);
            return result;
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = null;
        try {
            fetcher = builder.setName("queryGamerList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparentdisplaymethod.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparentdisplaymethod.getGamer(id))
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

    public static Bundle queryProductList() {
        Log.d(TAG, String.format("query queryProductList"));

        ApiObjectListHandler handler = new ApiObjectListHandler();

        if (Settings.NETDEBUG) {
            String fileName = "json/0-product.json";
            Bundle result = processFile(fileName, handler);
            return result;
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = builder.setName("queryTournamentNewsList")
                .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparentproduct.URL_METHOD)
                .setHandler(handler)
                .create();

        return fetcher.execute();
    }

    public static Bundle queryCardList() {
        Log.d(TAG, String.format("queryCardList"));

        ApiObjectListHandler handler = new ApiObjectListHandler();

        if (Settings.NETDEBUG) {
            String fileName = "json/0-card.json";
            Bundle result = processFile(fileName, handler);
            return result;
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = builder.setName("query all cards")
                .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparentcard.URL_METHOD)
                .setHandler(handler)
                .create();

        return fetcher.execute();
    }

    public static Bundle queryPartnerList() {
        Log.d(TAG, String.format("queryPartnerList"));

        IResponseHandler mHandler = new PartnerHandler();

        if (Settings.NETDEBUG) {
            String fileName = "json/0-partners.json";
            return processFile(fileName, mHandler);
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = builder.setName("queryTournamentNewsList")
                .setGetRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparentpartner.URL_METHOD)
                .setHandler(mHandler)
                .create();


        return fetcher.execute();
    }

    //Загрузка списка новостей турнира
    public static Bundle queryTournamentNewsList(long id, String parent) {
        Log.d(TAG, String.format("query queryTournamentNewsList"));

        IResponseHandler mHandler = new NewsHandler(parent);

        if (Settings.NETDEBUG) {
            String fileName = "json/" + String.valueOf(id) + "-news.json";
            Bundle result = processFile(fileName, mHandler);
            return result;
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = builder.setName("queryTournamentNewsList")
                .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparentnews.getParent(id))
                .setHandler(mHandler)
                .create();


        return fetcher.execute();
    }

    //Загрузка списка новостей турнира
    public static Bundle queryGallery(long id) {
        Log.d(TAG, String.format("queryGallery"));
        GalleryHandler handler = new GalleryHandler(id);

        if (Settings.NETDEBUG) {
            String fileName = "json/gallery-" + String.valueOf(id) + ".json";
            Bundle result = processFile(fileName, handler);
            return result;
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;
        try {
            fetcher = builder.setName("queryTournamentNewsList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.Gallery.getgallery.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.Gallery.getgallery.getUrl(id))
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

    //Загрузка списка галлерей турнира
    public static int[] queryGalleryList() {
        Log.d(TAG, String.format("queryGalleryList"));

        ApiObjectListHandler handler = new ApiObjectListHandler();

        if (Settings.NETDEBUG) {
            String fileName = "json/gallery-getlistgallery-gallery.json";
            Bundle result = processFile(fileName, handler);
            return MyNetwork.getIntArray(result);
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;
        fetcher = builder.setName("queryGalleryList")
                .setPostRequest(MyNetworkContentContract.FormulaTXApi.Gallery.getListGallery.URL_GALLERY)
                .setHandler(handler)
                .create();

        Bundle execute = fetcher.execute();

        return getIntArray(execute);
    }

    //Загрузка списка галлерей турнира
    public static int[] queryPodcastList() {
        Log.d(TAG, String.format("queryPodcastList"));

        ApiObjectListHandler handler = new ApiObjectListHandler();
        if (Settings.NETDEBUG) {
            String fileName = "json/gallery-getlistgallery-podcast.json";
            Bundle result = processFile(fileName, handler);
            return MyNetwork.getIntArray(result);
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher;
        fetcher = builder.setName("queryGalleryList")
                .setPostRequest(MyNetworkContentContract.FormulaTXApi.Gallery.getListGallery.URL_PODCAST)
                .setHandler(handler)
                .create();

        Bundle execute = fetcher.execute();

        return getIntArray(execute);
    }
    public static Bundle queryTwitter(int page) {
        Log.d(TAG, "queryGallery");

        TwitterHandler mHandler = new TwitterHandler(0);
        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher;
        fetcher = builder.setName("queryTournamentNewsList")
                .setPostRequest(MyNetworkContentContract.FormulaTXApi.References.getreferencebyidapproved.getUrl("twitter", page))
                .setHandler(mHandler)
                .create();

        return fetcher.execute();
    }

    private static Bundle processFile(String fileName, IResponseHandler mHandler) {
        Log.d(TAG, "processFile: " + fileName);
        String response = getStringFromAsset(fileName);
        Bundle result = new Bundle();

        try {
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
        Log.d(TAG, "getStringFromAsset: " + fileName);
        String string = null;
        try {
            InputStreamReader in = new InputStreamReader(FormulaTXApplication.getAppContext().getAssets().open(fileName));

            BufferedReader reader = new BufferedReader(in);
            String mLine;

            StringBuffer sb = new StringBuffer();
            while ((mLine = reader.readLine()) != null) {
                sb.append(mLine);
            }

            string = sb.toString();

        } catch (Exception e) {
            Log.e(TAG, "Cannot find file: " + fileName);
        }
        return string;
    }

    public static Bundle queryApiObject(int id, ApiObjectHandler handler) {

        if (Settings.NETDEBUG) {
            String fileName = "json/" + String.valueOf(id) + "ru.json";
            Bundle result = processFile(fileName, handler);
            return result;
        }

        Log.d(TAG, String.format("query tournament"));
        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;

        fetcher = builder.setName("queryApiObject")
                .setGetRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getpage.getObject(id))
                .setHandler(handler)
                .create();

        return fetcher.execute();
    }


    public static int[] getIntArray(Bundle bundleTurnirList) {
        int[] intArray = new int[0];
        String result = bundleTurnirList.getString(HttpRequester.RESULT);
        if (result.equals(HttpRequester.RESULT_HTTP)) {
            Bundle tmpTurnirList = bundleTurnirList.getBundle(HttpRequester.RESULT_HTTP);
            if (tmpTurnirList != null) {
                tmpTurnirList = tmpTurnirList.getBundle(HttpRequester.RESULT_HTTP_PARSE);
                if (tmpTurnirList != null) {
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
                    } catch (ClassCastException cce) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public static Bundle queryGamer(int id) {
        Log.d(TAG, String.format("query gamer"));
        GamerHandler handlerGamer = new GamerHandler();
        return getObjectWithAdditionalFields(id,  handlerGamer);
    }

    public static Bundle queryArea(int id) {
        Log.d(TAG, String.format("query areas"));
        AreaHandler handlerGamer = new AreaHandler();
        return getObjectWithAdditionalFields(id, handlerGamer);
    }

    public static Bundle queryProduct(int id) {
        Log.d(TAG, String.format("query product"));
        ProductHandler productHandler = new ProductHandler();
        return getObjectWithAdditionalFields(id, productHandler);
    }

    public static Bundle queryCard(int id) {
        Log.d(TAG, String.format("query card"));
        CardHandler cardHandler = new CardHandler();
        return getObjectWithAdditionalFields(id,  cardHandler);
    }

    public static Bundle queryPartner(int id) {
        Log.d(TAG, String.format("query partner"));
        PartnerHandler cardHandler = new PartnerHandler();
        return getObjectWithAdditionalFields(id,  cardHandler);
    }

    private static Bundle getObjectWithAdditionalFields(int id, JSONObjectHandler additionalHandler) {
        Log.d(TAG, String.format("query object with additional fields"));

        ApiObjectHandler handlerObject = new ApiObjectHandler();
        if (Settings.NETDEBUG) {
            String fileName = "json/" + String.valueOf(id) + "ru.json";
            processFile(fileName, handlerObject);

            fileName = "json/" + String.valueOf(id) + "add.json";
            processFile(fileName, additionalHandler);
            return Bundle.EMPTY;
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;

        try {

            fetcher = builder.setName("queryApiObject")
                    .setGetRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getpage.getObject(id))
                    .setHandler(handlerObject)
                    .create();

            fetcher.execute();

            fetcher = builder.setName("queryApiObject")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getadditionalfields.getParent(id))
                    .setHandler(additionalHandler)
                    .create();

            fetcher.execute();

        } catch (Exception e) {
            Log.d(TAG, "Error while server request", e);
            Bundle bundle = new Bundle();
            bundle.putString("RESULT", "EXCEPTION");
            bundle.putSerializable("EXCEPTION", e);
            return bundle;
        }

        return Bundle.EMPTY;
    }

    public static void sendComment(String author, String comment, String email, String phone) throws Exception {
        String credentials = FormulaTXApplication.getStringParameter(Settings.CREDENTIALS);

        ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>(4);
        dm_partner.add(new BasicNameValuePair("author", String.valueOf(author)));
        dm_partner.add(new BasicNameValuePair("comment", String.valueOf(comment)));
        dm_partner.add(new BasicNameValuePair("email", String.valueOf(email)));
        dm_partner.add(new BasicNameValuePair("phone", String.valueOf(phone)));

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester sender = builder.setName("sendComment")
                .setPostRequest(MyNetworkContentContract.FormulaTXApi.Feedback.URL)
                .setCredentials(credentials)
                .setContent(dm_partner)
                .setHandler(new AnyPostHandler())
                .create();

        sender.execute();
    }

    public static Bundle queryParser(int id) {
        Log.d(TAG, String.format("queryParser"));

        ParserHandler handler = new ParserHandler();

        if (Settings.NETDEBUG) {
            String fileName = "json/parsers-" + String.valueOf(id) + ".json";
            processFile(fileName, handler);

            return Bundle.EMPTY;
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;
        try {
            fetcher = builder.setName("queryParser")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.Parser.getparser.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.Parser.getparser.getUrl(id))
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

    public static Bundle getLadiesSchedule() {
        Log.d(TAG, String.format("getLadiesSchedule"));

        JSONObjectHandler handler = new JSONObjectHandler();

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;

        fetcher = builder.setName("getLadiesSchedule")
                .setGetRequest(MyNetworkContentContract.FormulaTXApi.Schedules.getLadies())
                .setHandler(handler)
                .create();

        return fetcher.execute();
    }

    public static Bundle getOpenSchedule() {
        Log.d(TAG, String.format("getOpenSchedule"));

        JSONObjectHandler handler = new JSONObjectHandler();

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;

        fetcher = builder.setName("getOpenSchedule")
                .setGetRequest(MyNetworkContentContract.FormulaTXApi.Schedules.getOpen())
                .setHandler(handler)
                .create();

        return fetcher.execute();
    }

    public static Bundle getLadiesLiveScore() {
        Log.d(TAG, String.format("getLadiesLiveScore"));

        JSONObjectHandler handler = new JSONObjectHandler();

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;

        fetcher = builder.setName("getLadiesLiveScore")
                .setGetRequest(MyNetworkContentContract.FormulaTXApi.LiveScores.getLadies())
                .setHandler(handler)
                .create();

        return fetcher.execute();
    }

    public static Bundle getOpenLiveScore() {
        Log.d(TAG, String.format("getOpenLiveScore"));

        JSONObjectHandler handler = new JSONObjectHandler();


        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;

        fetcher = builder.setName("getOpenLiveScore")
                .setGetRequest(MyNetworkContentContract.FormulaTXApi.LiveScores.getOpen())
                .setHandler(handler)
                .create();

        return fetcher.execute();
    }

    public static Bundle getLadiesGrids(int i) {
        Log.d(TAG, "getLadiesGrids");

        JSONObjectHandler handler = new JSONObjectHandler();

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;

        String url;
        switch (i){
            case MyNetworkContentContract.FormulaTXApi.Grids.QUALIFICATION:
                url = MyNetworkContentContract.FormulaTXApi.Grids.getLadiesQulification();
                break;
            case MyNetworkContentContract.FormulaTXApi.Grids.MAIN_EVENT:
                url = MyNetworkContentContract.FormulaTXApi.Grids.getLadiesMainEvent();
                break;
            case MyNetworkContentContract.FormulaTXApi.Grids.DOUBLES:
                url = MyNetworkContentContract.FormulaTXApi.Grids.getLadiesDoubles();
                break;
            default: return null;
        }

        fetcher = builder.setName("getLadiesLiveScore")
                .setGetRequest(url)
                .setHandler(handler)
                .create();

        return fetcher.execute();
    }

    public static Bundle getOpenGrids(int i) {
        Log.d(TAG, "getOpenGrids");

        JSONObjectHandler handler = new JSONObjectHandler();

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;

        String url;
        switch (i){
            case MyNetworkContentContract.FormulaTXApi.Grids.QUALIFICATION:
                url = MyNetworkContentContract.FormulaTXApi.Grids.getOpenQulification();
                break;
            case MyNetworkContentContract.FormulaTXApi.Grids.MAIN_EVENT:
                url = MyNetworkContentContract.FormulaTXApi.Grids.getOpenMainEvent();
                break;
            case MyNetworkContentContract.FormulaTXApi.Grids.DOUBLES:
                url = MyNetworkContentContract.FormulaTXApi.Grids.getOpenDoubles();
                break;
            default: return null;
        }

        fetcher = builder.setName("getLadiesLiveScore")
                .setGetRequest(url)
                .setHandler(handler)
                .create();

        return fetcher.execute();
    }

    public static boolean isRequestSuccess(Bundle bundle) {
        return bundle.getString(HttpRequester.RESULT).equals(HttpRequester.RESULT_HTTP);
    }

    public static boolean hasException(Bundle bundle) {
        return bundle.getString(HttpRequester.RESULT).equals(HttpRequester.RESULT_EXCEPTION);
    }

    public static void throwException(Bundle bundle) throws Exception {
        String result = bundle.getString(HttpRequester.RESULT);
        if (result !=null && result.equals(HttpRequester.RESULT_EXCEPTION)){
            Exception exception = (Exception) bundle.getSerializable(HttpRequester.RESULT_EXCEPTION);
            throw exception;
        }

    }

    //Загрузка списка новостей турнира
    public static Bundle queryInstagram(int page) {
        Log.d(TAG, "queryInstagram");

        InstagramHandler mHandler = new InstagramHandler(1);
        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher;
        fetcher = builder.setName("queryInstagram")
                .setPostRequest(MyNetworkContentContract.FormulaTXApi.References.getreferencebyidapproved.getUrl("instagram", page))
                .setHandler(mHandler)
                .create();

        return fetcher.execute();
    }

}
