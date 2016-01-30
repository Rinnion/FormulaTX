package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.database.helper.CardHelper;
import com.rinnion.archived.database.helper.ProductHelper;
import com.rinnion.archived.database.model.ApiObjects.Card;
import com.rinnion.archived.database.model.ApiObjects.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class CardHandler extends JSONObjectHandler {

    private CardHelper th;

    public CardHandler(){
        this.th = new CardHelper();
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");

            Card ao = null;
            for (int i = 0; i < message.length(); i++) {
                JSONObject obj = (JSONObject) message.get(i);
                String id = obj.getString("post_id");
                String key = obj.getString("key");
                String value = obj.getString("value");

                if (ao == null) ao = th.getCard(Long.parseLong(id));
                if (ao == null) ao = new Card(Long.parseLong(id));
                if (key.equals("Status")) {
                    ao.status = value;
                }
                if (key.equals("Link")) {
                    ao.link = value;
                }
            }

            if (ao == null) return Bundle.EMPTY;

            th.merge(ao);

            return super.Handle(object);
        }
        return Bundle.EMPTY;
    }
}


