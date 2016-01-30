package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.network.MyNetworkContentContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alekseev on 29.12.2015.
 */

public class ApiObjectItemHandler extends JSONObjectHandler {

    public static final String OBJECT = "object";

    Pattern ptrnImgSrc = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
    protected ApiObjectHelper aoh;
    private String defaultMethod;

    public ApiObjectItemHandler(String defaultMethod){
        this.defaultMethod = defaultMethod;
        aoh = new ApiObjectHelper(ArchivedApplication.getDatabaseOpenHelper());
    }

    public void onBeforeSaveObject(ApiObject ao){}

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        Bundle bundle = new Bundle();
        bundle.putString("ApiObject", object.toString());

        ApiObject ao = new ApiObject(object);
        if (ao.display_method == null) ao.display_method = defaultMethod;
        onBeforeSaveObject(ao);
        ao.content = changeLinksWithinHtml(ao);
        aoh.add(ao);
        bundle.putSerializable(OBJECT, ao);

        return super.Handle(object);
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


