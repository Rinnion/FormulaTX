package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Settings;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.network.MyNetworkContentContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tretyakov on 07.07.2015.
 */
public class GalleryHandler extends FormulaTXResponseHandler {

    private long mId;

    public GalleryHandler(long id) {

        mId = id;
    }

    @Override
    public Bundle onTrueStatus(JSONObject message, Bundle bundle) throws JSONException {
        GalleryHelper gh = new GalleryHelper(ArchivedApplication.getDatabaseOpenHelper());
        JSONObject gallery = message.getJSONObject("gallery");
        JSONArray array;
        array = gallery.getJSONArray(GalleryHelper.TYPE_AUDIO);
        bundle.putLong(GalleryHelper.TYPE_AUDIO, importPictureArray(gh, array));
        array = gallery.getJSONArray(GalleryHelper.TYPE_PICTURE);
        bundle.putLong(GalleryHelper.TYPE_PICTURE, importPictureArray(gh, array));
        array = gallery.getJSONArray(GalleryHelper.TYPE_VIDEO);
        bundle.putLong(GalleryHelper.TYPE_VIDEO, importPictureArray(gh, array));
        array = gallery.getJSONArray(GalleryHelper.TYPE_WATERMARK);
        bundle.putLong(GalleryHelper.TYPE_WATERMARK, importPictureArray(gh, array));
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
    public Bundle onErrorStatus(JSONObject message, Bundle bundle) {
        return Bundle.EMPTY;
    }


}
