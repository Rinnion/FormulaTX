package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Utils;
import com.rinnion.archived.database.helper.NewsHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Parse News from server
 */
public class NewsHandler extends JSONObjectHandler {
    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        long id = object.getLong("MessageId");
        String content = object.getString("Content");
        String background = object.getString("Content");
        long date_post = Utils.getTimeStamp(object, "Date");
        long likes = object.getLong("Likes");
        boolean like = object.getBoolean("Like");
        Long comments = object.getLong("Comments");
        JSONArray jsonTags = object.getJSONArray("Tags");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonTags.length(); i++) {
            JSONObject jsonTag = (JSONObject) jsonTags.get(i);
            String name = jsonTag.getString("Name");
            if (sb.length() > 0) sb.append(",");
            sb.append(name);
        }
        long date_receive = Calendar.getInstance().getTimeInMillis();
        NewsHelper mh = new NewsHelper(ArchivedApplication.getDatabaseOpenHelper());
//Здусь должно бвыть сохрание в БД.
        //Message message = new Message(id, content, background, date_post, likes, like, comments, sb.toString(), date_receive);
        //mh.add(message);
        return Bundle.EMPTY;
    }

}
