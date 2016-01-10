package com.rinnion.archived.network.handlers;

import android.nfc.FormatException;
import android.os.Bundle;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.network.MyNetworkContentContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alekseev on 29.12.2015.
 */

public class TournamentHandler extends ApiObjectHandler {


    Pattern ptrnImgSrc = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
    private TournamentHelper th;

    public TournamentHandler(TournamentHelper th){
        super(th, ApiObjectTypes.EN_Object);
        this.th = th;
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");
            if (message.length() != 1) throw new JSONException("Get message with not only one element" + message.length());

            JSONObject obj = (JSONObject) message.get(0);

            Bundle bundle = new Bundle();
            bundle.putString("ApiObject", obj.toString());
            Tournament ao = new Tournament(obj);

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
            ao.content = sb.toString();

            th.add(ao);

            return bundle;
        }
        return Bundle.EMPTY;
    }

}


