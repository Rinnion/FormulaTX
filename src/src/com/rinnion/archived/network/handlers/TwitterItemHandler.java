package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.helper.TwitterHelper;
import com.rinnion.archived.database.model.TwitterItem;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitterItemHandler extends JSONObjectHandler {

    private static final String TAG = "TwitterHandler";
    private final TwitterHelper gh;
    private long mId;

    public TwitterItemHandler(long id) {
        mId = id;
        gh = new TwitterHelper(ArchivedApplication.getDatabaseOpenHelper());
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        String platform = object.getString("platform");
        if (!platform.equals(TwitterHelper.TYPE)) return Bundle.EMPTY;

        String text = object.getString("title");
        String link = object.getString("link");
        String date = object.getString("date");
        long id = object.getLong("id");
        TwitterItem ti = new TwitterItem(id, mId, text, link, date);
        gh.merge(ti);

        return super.Handle(object);
    }
}
