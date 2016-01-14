package com.rinnion.archived.network;

import android.os.Bundle;
import com.rinnion.archived.database.helper.*;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Settings;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.network.handlers.*;
import com.rinnion.archived.utils.MyLocale;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
        String credentials = ArchivedApplication.getStringParameter(Settings.CREDENTIALS);

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
    public static Bundle queryGamerList(long id) {
        Log.d(TAG, String.format("query queryGamerList"));

        ApiObjectListHandler handler = new ApiObjectListHandler();

        if (Settings.NETDEBUG) {
            String fileName = "json/" + String.valueOf(id) + "gamer.json";
            Bundle result = processFile(fileName, handler);
            return result;
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = null;
        try {
            fetcher = builder.setName("queryTournamentNewsList")
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

    //Загрузка списка новостей турнира
    public static Bundle queryTournamentNewsList(long id) {
        Log.d(TAG, String.format("query queryTournamentNewsList"));

        if (Settings.NETDEBUG) {
            String fileName = "json/" + String.valueOf(id) + "parent.json";
            IResponseHandler mHandler = new ApiObjectListHandler();
            Bundle result = processFile(fileName, mHandler);
            return result;
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();

        HttpRequester fetcher = null;
        try {
            fetcher = builder.setName("queryTournamentNewsList")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparent.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getallstaticpagefromparent.getParent(id))
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

        if (Settings.NETDEBUG) {
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
        switch (type) {
            case ApiObjectTypes.EN_Object:
                handler = new TournamentHandler(new TournamentHelper(doh));
                break;
            case ApiObjectTypes.EN_News:
                handler = new NewsHandler(new NewsHelper(doh));
                break;
            default:
                handler = new ApiObjectHandler(new ApiObjectHelper(ArchivedApplication.getDatabaseOpenHelper()), type);
        }

        return queryApiObject(id, handler);
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

        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        ApiObjectHandler handlerObject = new ApiObjectHandler(new ApiObjectHelper(doh), ApiObjectTypes.EN_Gamer);
        GamerHandler handlerGamer = new GamerHandler(new GamerHelper(doh));

        return getObjectWithAdditionalFields(id, handlerObject, handlerGamer);
    }

    public static Bundle queryProduct(int id) {
        Log.d(TAG, String.format("query product"));

        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        ApiObjectHandler handlerObject = new ApiObjectHandler(new ApiObjectHelper(doh), ApiObjectTypes.EN_Product);
        ProductHandler productHandler = new ProductHandler(new ProductHelper(doh));

        return getObjectWithAdditionalFields(id, handlerObject, productHandler);
    }

    private static Bundle getObjectWithAdditionalFields(int id, ApiObjectHandler handlerObject, JSONObjectHandler handlerProduct) {
        Log.d(TAG, String.format("query object with additional fields"));

        if (Settings.NETDEBUG) {
            String fileName = "json/" + String.valueOf(id) + "ru.json";
            processFile(fileName, handlerObject);

            fileName = "json/" + String.valueOf(id) + "add.json";
            processFile(fileName, handlerProduct);
            return Bundle.EMPTY;
        }

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher;

        try {

            fetcher = builder.setName("queryApiObject")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getpage.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getpage.getObject(String.valueOf(id)))
                    .setHandler(handlerObject)
                    .create();

            fetcher.execute();

            fetcher = builder.setName("queryApiObject")
                    .setPostRequest(MyNetworkContentContract.FormulaTXApi.StaticPage.getadditionalfields.URL_METHOD)
                    .setContent(MyNetworkContentContract.FormulaTXApi.StaticPage.getadditionalfields.getUrl(id))
                    .setHandler(handlerProduct)
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
        String credentials = ArchivedApplication.getStringParameter(Settings.CREDENTIALS);

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

}
