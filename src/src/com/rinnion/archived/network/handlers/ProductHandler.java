package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.helper.ProductHelper;
import com.rinnion.archived.database.model.ApiObjects.Gamer;
import com.rinnion.archived.database.model.ApiObjects.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class ProductHandler extends JSONObjectHandler {

    private ProductHelper th;

    public ProductHandler(){
        this.th = new ProductHelper(ArchivedApplication.getDatabaseOpenHelper());
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");

            Product ao = null;
            for (int i = 0; i < message.length(); i++) {
                JSONObject obj = (JSONObject) message.get(i);
                String id = obj.getString("post_id");
                String key = obj.getString("key");
                String value = obj.getString("value");

                if (ao == null) ao = th.getProduct(Long.parseLong(id));
                if (ao == null) ao = new Product(Long.parseLong(id));
                if (key.equals("Price")) {
                    ao.price = value;
                }
                if (key.equals("TOP")) {
                    ao.top = value;
                }
            }

            if (ao == null) return Bundle.EMPTY;

            th.merge(ao);

            return super.Handle(object);
        }
        return Bundle.EMPTY;
    }
}


