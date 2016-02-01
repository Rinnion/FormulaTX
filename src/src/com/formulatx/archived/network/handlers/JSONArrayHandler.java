package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import android.os.Parcelable;
import com.formulatx.archived.utils.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Handles JSON Array. Uses custom JSON Object Handler for each object within
 */
public class JSONArrayHandler extends JSONHandler {
    public static final String EXTRA_ARRAY = "array";
    public static final String EXTRA_ARRAY_LENGTH = "array length";
    private static final String TAG = "JSONArrayHandler";
    private JSONObjectHandler mHandler;

    public JSONArrayHandler(JSONObjectHandler handler) {
        mHandler = handler;
    }

    public void beforeArrayHandle() {
    }

    public void afterArrayHandle() {
    }

    @Override
    protected Bundle onStringHandle(String content) throws JSONException {
        JSONArray json = new JSONArray(content);
        return Handle(json);
    }

    public Bundle Handle(JSONArray array) throws JSONException {
        Log.d(TAG, String.format("array.length = '%s'", array.length()));
        beforeArrayHandle();
        Bundle bundle = new Bundle();
        ArrayList<Bundle> arrayBundles = new ArrayList<Bundle>(array.length());
        if (array.length() > 0) {
            for (int j = 0; j < array.length(); j++) {
                JSONObject obj = (JSONObject) array.get(j);
                arrayBundles.add(mHandler.Handle(obj));
            }
        }
        bundle.putParcelableArray(EXTRA_ARRAY, arrayBundles.toArray(new Parcelable[arrayBundles.size()]));
        bundle.putInt(EXTRA_ARRAY_LENGTH, arrayBundles.size());
        afterArrayHandle();
        return bundle;
    }
}
