package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class TournamentHandler extends ApiObjectHandler {


    private TournamentHelper th;

    public TournamentHandler(    ){
        super();
        this.th = new TournamentHelper(FormulaTXApplication.getDatabaseOpenHelper());
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        String status = object.getString("status");
        if (status.equals("true")) {
            JSONArray message = object.getJSONArray("message");
            if (message.length() != 1) throw new JSONException("Get message with not only one element" + message.length());

            JSONObject obj = (JSONObject) message.get(0);

            Bundle bundle = new Bundle();
            bundle.putString(API_OBJECT, obj.toString());
            Tournament ao = new Tournament(obj);

            ao.content = changeLinksWithinHtml(ao);

            th.add(ao);

            bundle.putSerializable(OBJECT, ao);

            return bundle;
        }
        return Bundle.EMPTY;
    }

}


