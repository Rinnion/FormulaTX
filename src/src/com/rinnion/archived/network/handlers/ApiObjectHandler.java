package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */


    public class ApiObjectHandler extends JSONObjectHandler {
        @Override
        public Bundle Handle(JSONObject object) throws JSONException {
            boolean status = object.getBoolean("status");
            if (status) {
                JSONArray message = object.getJSONArray("message");

                Bundle bundle = new Bundle();
                bundle.putString("ApiObject",message.get(0).toString());
                return  bundle;
            }
            return Bundle.EMPTY;
        }

    }


