package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 21.02.14
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
public class JSONObjectHandler extends JSONHandler {

    public static final String VALUE = "VALUE";

    public Bundle Handle(JSONObject object) throws JSONException {
        Bundle bundle = new Bundle();
        bundle.putString(VALUE, String.valueOf(object));
        return bundle;
    }

    public void beforeHandle() {
    }

    public void afterHandle() {
    }

    @Override
    protected Bundle onStringHandle(String content) throws JSONException {

        String sss = content.replace("\\\"", "\"");
        sss = sss.replace("\\\\", "\\");
        sss = sss.replaceAll("^\"|\"$", "");
        JSONObject jsonObject = new JSONObject(sss);
        beforeHandle();
        Bundle bundle = Handle(jsonObject);
        afterHandle();
        return bundle;
    }
}
