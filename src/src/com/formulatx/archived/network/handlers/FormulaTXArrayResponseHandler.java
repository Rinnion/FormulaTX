package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 05.01.2016.
 */
public abstract class FormulaTXArrayResponseHandler extends JSONObjectHandler{

    public static final String STATUS = "STATUS";
    private final JSONArrayHandler jah;

    public abstract Bundle onErrorStatus(JSONObject message, Bundle bundle);

    public FormulaTXArrayResponseHandler(JSONObjectHandler oh){
        jah = new JSONArrayHandler(oh);
    }

    public FormulaTXArrayResponseHandler(JSONArrayHandler jah){
        this.jah = jah;
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        Bundle bundle = new Bundle();
        bundle.putBoolean(STATUS, status);
        if (status) {
            return jah.Handle(object.getJSONArray("message"));
        }else{
            JSONObject message = object.getJSONObject("message");
            return onErrorStatus(message, bundle);
        }
    }
}
