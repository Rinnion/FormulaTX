package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.network.MyNetworkContentContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alekseev on 29.12.2015.
 */

public class ApiObjectHandler extends JSONObjectHandler {

    public static final String OBJECT = "object";
    public static final String API_OBJECT = "ApiObject";

    Pattern ptrnImgSrc = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
    protected ApiObjectHelper aoh;

    public ApiObjectHandler(){
        aoh = new ApiObjectHelper(FormulaTXApplication.getDatabaseOpenHelper());
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");

            Bundle bundle = new Bundle();
            bundle.putString("ApiObject", message.get(0).toString());

            ApiObject ao = new ApiObject((JSONObject) message.get(0));
            ao.content = changeLinksWithinHtml(ao);
            aoh.add(ao);
            bundle.putSerializable(OBJECT, ao);

            return bundle;
        }
        return Bundle.EMPTY;
    }

    protected String changeLinksWithinHtml(ApiObject ao) {
        Matcher imgs = ptrnImgSrc.matcher(ao.content);
        StringBuffer sb = new StringBuffer();
        while (imgs.find()){
            String group = imgs.group();
            String text = imgs.group(1);
            if (text.startsWith("/")) group = group.replace(text, MyNetworkContentContract.URL + text.substring(1));
            if (text.startsWith(".")) group = group.replace(text, MyNetworkContentContract.URL + text);
            imgs.appendReplacement(sb, group);
        }
        imgs.appendTail(sb);
        return sb.toString();
    }
}


