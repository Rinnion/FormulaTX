package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.database.helper.NewsHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.database.model.ApiObjects.News;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class NewsHandler extends ApiObjectHandler {

    private NewsHelper th;

    public NewsHandler(NewsHelper th){
        super(th, ApiObjectTypes.EN_News);
        this.th = th;
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");
            if (message.length() != 1) throw new JSONException("Get message with not only one element" + message.length());

            JSONObject obj = (JSONObject) message.get(0);

            Bundle bundle = new Bundle();
            bundle.putString("ApiObject", obj.toString());
            News ao = new News(obj);
            th.add(ao);

            return bundle;
        }
        return Bundle.EMPTY;
    }

}


