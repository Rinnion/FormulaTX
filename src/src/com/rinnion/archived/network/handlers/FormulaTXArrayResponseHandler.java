package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 05.01.2016.
 */
public abstract class FormulaTXArrayResponseHandler extends JSONObjectHandler{

    public static final String STATUS = "STATUS";

    public abstract Bundle onTrueStatus(JSONArray message, Bundle bundle) throws JSONException;

    public abstract Bundle onErrorStatus(JSONObject message, Bundle bundle);

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        Bundle bundle = new Bundle();
        bundle.putBoolean(STATUS, status);
        if (status) {
            JSONArray message = object.getJSONArray("message");
            return onTrueStatus(message, bundle);
        }else{
            JSONObject message = object.getJSONObject("message");
            return onErrorStatus(message, bundle);
        }
    }
}
