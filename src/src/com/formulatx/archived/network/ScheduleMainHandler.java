package com.formulatx.archived.network;

import android.os.Bundle;
import com.formulatx.archived.network.handlers.JSONObjectHandler;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 03.02.2016.
 */
public class ScheduleMainHandler extends JSONObjectHandler {
    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        //object.getJSONArray("corts").getJSONObject(0).getJSONObject("1").getJSONObject("teams").getJSONObject("team1");
        return super.Handle(object);
    }
}
