package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.GamerHelper;
import com.formulatx.archived.database.helper.ProductHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.database.model.ApiObjects.Gamer;
import com.formulatx.archived.database.model.ApiObjects.Product;
import com.formulatx.archived.utils.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */


public class GamerHandler extends FormulaTXArrayResponseHandler {


    public GamerHandler(String parent) {
        super(new GamerItemHandler(parent));
    }

    private static class GamerItemHandler extends ApiObjectItemHandler {

        private static final String TAG = "GamerItemHandler";
        private final GamerHelper gh;
        private String parent;

        public GamerItemHandler(String parent) {
            super(ApiObject.GAMER);
            this.parent = parent;
            gh = new GamerHelper();
        }

        @Override
        public void onBeforeSaveObject(ApiObject ao) {
            ao.parent=parent;
            super.onBeforeSaveObject(ao);
        }

        @Override
        public Bundle Handle(JSONObject object) throws JSONException {
            try {
                Bundle bundle = super.Handle(object);
                ApiObject obj = (ApiObject) bundle.getSerializable(ApiObjectItemHandler.OBJECT);

                float rating = (float) object.getDouble("rating");
                String country = object.getString("country");
                String name = object.getString("name");
                String surname = object.getString("surname");
                String flag = object.getString("flag");
                String thumb = object.getString("thumb");
                Gamer ti = new Gamer(obj.id);

                ti.rating = rating;
                ti.country = country;
                ti.name = name;
                ti.surname = surname;
                ti.flag = flag;
                ti.thumb = thumb;
                ti.title= String.valueOf(ti.name) + " " + String.valueOf(ti.surname);

                gh.merge(ti);

                return bundle;
            }catch(Exception ignored){
                Log.w(TAG, ignored.getMessage());
            }
            return Bundle.EMPTY;
        }
    }

    @Override
    public Bundle onErrorStatus(JSONObject message, Bundle bundle) {
        return Bundle.EMPTY;
    }
}

/*

public class GamerHandler extends JSONObjectHandler {

    private GamerHelper th;

    public GamerHandler(){
        this.th = new GamerHelper();
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");

            Gamer ao = null;
            for (int i = 0; i < message.length(); i++) {
                JSONObject obj = (JSONObject) message.get(i);
                String id = obj.getString("post_id");
                String key = obj.getString("key");
                String value = obj.getString("value");

                if (ao == null) ao = th.getGamer(Long.parseLong(id));
                if (ao == null) ao = new Gamer(Long.parseLong(id));
                if (key.equals("Rating")) {
                    ao.rating = Float.parseFloat(value);
                }
                if (key.equals("Country")) {
                    ao.country = value;
                }
                if (key.equals("Name")) {
                    ao.name = value;
                }
                if (key.equals("Surname")) {
                    ao.surname = value;
                }
                if (key.equals("Flag")) {
                    ao.flag = value;
                }
            }

            if (ao == null) return Bundle.EMPTY;

            ao.full_name = String.valueOf(ao.name) + " " + String.valueOf(ao.surname);

            th.merge(ao);

            return super.Handle(object);
        }
        return Bundle.EMPTY;
    }
}


*/