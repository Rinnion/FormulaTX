package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import android.util.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.database.helper.TwitterHelper;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.database.model.TwitterItem;
import com.rinnion.archived.network.MyNetworkContentContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitterHandler extends FormulaTXArrayResponseHandler {

    private static final String TAG = "TwitterHandler";
    private long mId;

    public TwitterHandler(long id) {
        mId = id;
    }

    @Override
    public Bundle onTrueStatus(JSONArray messages, Bundle bundle) throws JSONException {
        TwitterHelper gh = new TwitterHelper(ArchivedApplication.getDatabaseOpenHelper());

        int k = 0;
        for (int i = 0; i < messages.length(); i++) {
            JSONObject message= (JSONObject) messages.get(i);
            String platform = message.getString("platform");
            if (!platform.equals(TwitterHelper.TYPE)) continue;

            String text = message.getString("title");
            String link = message.getString("link");
            String date = message.getString("date");
            long id = message.getLong("id");
            TwitterItem ti = new TwitterItem(id, mId, text, link, date);
            gh.merge(ti);
            k++;
        }

        bundle.putLong("COUNT", k);
        Log.d(TAG, "COUNT: " + String.valueOf(k));
        return bundle;
    }

    private int importPictureArray(GalleryHelper gh, JSONArray array) throws JSONException {
    int k =0;
        for (int i =0; i<array.length(); i++){
            JSONObject item = (JSONObject) array.get(i);
            long id = item.getLong("id");
            String type = item.getString("type");
            String url = item.getString("picture");
            if (url.startsWith("/")) url = MyNetworkContentContract.URL + url.substring(1);
            String link = item.getString("link");
            GalleryItem gi = new GalleryItem(id, mId, type, url, link);
            if (gh.merge(gi)) k++;
        }
        return k;
    }

    @Override
    public Bundle onErrorStatus(JSONArray message, Bundle bundle) {
        return Bundle.EMPTY;
    }


}
