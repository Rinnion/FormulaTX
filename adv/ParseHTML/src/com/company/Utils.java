package com.company;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 01.02.14
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static String getStringOrNull(JSONObject in, String key) throws JSONException {
        return in.has(key) ? in.getString(key) : null;
    }

    public static Boolean getBooleanOrNull(JSONObject in, String key) throws JSONException {
        return in.has(key) ? in.getBoolean(key) : null;
    }
}

