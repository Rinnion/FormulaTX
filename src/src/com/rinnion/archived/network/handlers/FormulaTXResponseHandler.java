package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 05.01.2016.
 */
public abstract class FormulaTXResponseHandler extends JSONObjectHandler{

    public static final String STATUS = "STATUS";

    public abstract Bundle onTrueStatur(JSONObject message, Bundle bundle) throws JSONException;

    public abstract Bundle onFalseStatus(JSONObject message, Bundle bundle);

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        JSONObject message = object.getJSONObject("message");
        Bundle bundle = new Bundle();
        bundle.putBoolean(STATUS, status);
        if (status) { return onTrueStatur(message, bundle); }else{return onFalseStatus(message, bundle);}
    }
}
