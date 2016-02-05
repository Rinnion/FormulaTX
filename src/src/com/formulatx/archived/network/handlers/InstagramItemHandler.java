package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.database.helper.InstagramHelper;
import com.formulatx.archived.database.helper.TwitterHelper;
import com.formulatx.archived.database.model.InstagramItem;
import com.formulatx.archived.database.model.TwitterItem;
import org.json.JSONException;
import org.json.JSONObject;

public class InstagramItemHandler extends JSONObjectHandler {

    private static final String TAG = "TwitterHandler";
    private final InstagramHelper gh;
    private long mId;

    public InstagramItemHandler(long id) {
        mId = id;
        gh = new InstagramHelper();
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        //String platform = object.getString("platform");
        //if (!platform.equals(TwitterHelper.TYPE)) return Bundle.EMPTY;

        String text = object.getString("description");
        String link = object.getString("link");
        String date = object.getString("date");
        long id = object.getLong("id");
        InstagramItem ti = new InstagramItem(id, mId, text, link, date);
        gh.merge(ti);

        return super.Handle(object);
    }
}
