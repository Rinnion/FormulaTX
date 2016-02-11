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