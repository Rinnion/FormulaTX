package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Utils;
import com.rinnion.archived.database.helper.CommentHelper;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.database.model.Comment;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.database.model.User;
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
    public Bundle onTrueStatur(JSONObject message, Bundle bundle) throws JSONException {
        GalleryHelper gh = new GalleryHelper(ArchivedApplication.getDatabaseOpenHelper());
        JSONObject gallery = message.getJSONObject("gallery");
        JSONArray pictureArray = gallery.getJSONArray("picture");
        for (int i =0; i<pictureArray.length(); i++){
            JSONObject item = (JSONObject) pictureArray.get(i);
            long id = item.getLong("id");
            String type = item.getString("type");
            String url = "http://app.formulatx.com" + item.getString("picture");
            GalleryItem gi = new GalleryItem(id, mId, type, url);
            gh.add(gi);
        }
        bundle.putLong("Picture", pictureArray.length());
        return bundle;
    }

    @Override
    public Bundle onFalseStatus(JSONObject message, Bundle bundle) {
        return Bundle.EMPTY;
    }


}
