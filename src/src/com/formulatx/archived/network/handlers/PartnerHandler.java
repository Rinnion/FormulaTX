package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.Utils;
import com.formulatx.archived.database.helper.PartnerHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.database.model.ApiObjects.Partner;
import com.formulatx.archived.utils.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class PartnerHandler extends FormulaTXArrayResponseHandler {

    static class PartnerItemHandler extends ApiObjectItemHandler {
        private static final String TAG = "PartnerItemHandler";
        private final PartnerHelper mPartnerHelper;

        public PartnerItemHandler() {
            super(ApiObject.PARTNER);
            mPartnerHelper = new PartnerHelper();
        }

        @Override
        public Bundle Handle(JSONObject object) throws JSONException {
            try {
                Bundle handle = super.Handle(object);
                long id = object.getLong("id");
                String link = object.getString("link");
                String status = Utils.getStringOrNull(object, "status");
                Partner p = mPartnerHelper.getPartner(id);
                if (p == null) p = new Partner(id);
                p.link = link;
                p.status = status;
                mPartnerHelper.merge(p);
                return handle;
            }catch(Exception ignored){
                Log.e(TAG, ignored.getMessage());
            }
            return Bundle.EMPTY;
        }
    }

    public PartnerHandler() {
        super(new PartnerItemHandler());
    }

    @Override
    public Bundle onErrorStatus(JSONObject message, Bundle bundle) {
        return Bundle.EMPTY;
    }


}
/*
public class PartnerHandler extends JSONObjectHandler {


    private PartnerHelper th;

    public PartnerHandler(){
        this.th = new PartnerHelper();
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");

            Partner ao = null;
            for (int i = 0; i < message.length(); i++) {
                JSONObject obj = (JSONObject) message.get(i);
                String id = obj.getString("post_id");
                String key = obj.getString("key");
                String value = obj.getString("value");

                if (ao == null) ao = th.getPartner(Long.parseLong(id));
                if (ao == null) ao = new Partner(Long.parseLong(id));
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
*/

