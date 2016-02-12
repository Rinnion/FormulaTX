package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Utils;
import com.formulatx.archived.database.cursor.GalleryDescriptionCursor;
import com.formulatx.archived.database.cursor.GalleryItemCursor;
import com.formulatx.archived.database.model.GalleryItem;
import com.formulatx.archived.database.helper.GalleryHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tretyakov on 07.07.2015.
 */
public class GalleryHandler extends FormulaTXObjectResponseHandler {

    private long mId;

    public GalleryHandler(long id) {
        mId = id;
    }

    @Override
    public Bundle onTrueStatus(JSONObject message, Bundle bundle) throws JSONException {
        GalleryHelper gh = new GalleryHelper(FormulaTXApplication.getDatabaseOpenHelper());
        String title = message.getString("title");
        String type = message.getString("type");

        long id = message.getLong("id");

        gh.deleteGallery(id);

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

        GalleryDescriptionCursor.GalleryDescription gd = new GalleryDescriptionCursor.GalleryDescription(id, title, type);
        GalleryItemCursor pctPicture = gh.getAllItemsByGalleryIdAndType(id, "picture");
        gh.mergeGallery(gd);
        if (pctPicture.getCount() > 0) {
            GalleryItem item = pctPicture.getItem();
            gd.picture = item.url;
            gh.setGalleryPicture(id, gd.picture);
        }
        GalleryItemCursor pctVideo = gh.getAllItemsByGalleryIdAndType(id, "video");
        if (pctVideo.getCount() > 0) {
            GalleryItem item = pctVideo.getItem();
            gd.video = item.url;
            gh.setGalleryVideo(id, gd.video);
        }

        return bundle;
    }

    private int importPictureArray(GalleryHelper gh, JSONArray array) throws JSONException {
    int k =0;
        for (int i =0; i<array.length(); i++){
            JSONObject item = (JSONObject) array.get(i);
            long id = item.getLong("id");
            String type = item.getString("type");
            String url = item.getString("picture");
            url = Utils.fixUrlWithFullPath(url);
            String link = item.getString("link");
            link = Utils.fixUrlWithFullPath(link);
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
