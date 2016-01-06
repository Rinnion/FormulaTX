package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class ApiObjectHandler extends JSONObjectHandler {

    private ApiObjectHelper aoh;
    private int type;

    public ApiObjectHandler(ApiObjectHelper aoh, int type){
        this.aoh = aoh;
        this.type = type;
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");

            Bundle bundle = new Bundle();
            bundle.putString("ApiObject", message.get(0).toString());

            ApiObject apiObject = new ApiObject((JSONObject) message.get(0), type);
            aoh.add(apiObject);

            return bundle;
        }
        return Bundle.EMPTY;
    }

}


