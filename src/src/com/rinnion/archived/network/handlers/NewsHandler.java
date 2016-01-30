package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.database.model.ApiObjects.News;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class NewsHandler extends ApiObjectHandler {

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");

            Bundle bundle = new Bundle();
            for (int i = 0; i < message.length(); i++) {
                JSONObject obj = (JSONObject) message.get(i);
                bundle.putString("ApiObject", obj.toString());
                News ao = new News(obj);
                ao.content = changeLinksWithinHtml(ao);
                aoh.add(ao);
            }

            return bundle;
        }
        return Bundle.EMPTY;
    }

}


