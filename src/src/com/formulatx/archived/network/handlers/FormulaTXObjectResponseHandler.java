package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 05.01.2016.
 */
public abstract class FormulaTXObjectResponseHandler extends JSONObjectHandler{

    public static final String STATUS = "STATUS";

    public abstract Bundle onTrueStatus(JSONObject message, Bundle bundle) throws JSONException;

    public abstract Bundle onErrorStatus(JSONObject message, Bundle bundle);

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        JSONObject message = object.getJSONObject("message");
        Bundle bundle = new Bundle();
        bundle.putBoolean(STATUS, status);
        if (status) {
            return onTrueStatus(message, bundle);
        } else {
            return onErrorStatus(message, bundle);
        }
    }
}
