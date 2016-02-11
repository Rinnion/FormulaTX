package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.ProductHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.database.model.ApiObjects.Card;
import com.formulatx.archived.database.helper.CardHelper;
import com.formulatx.archived.database.model.ApiObjects.Product;
import com.formulatx.archived.utils.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class CardHandler extends FormulaTXArrayResponseHandler {


    public CardHandler() {
        super(new CardItemHandler());
    }

    private static class CardItemHandler extends ApiObjectItemHandler {

        private static final String TAG = "ProductItemHandler";
        private final CardHelper gh;

        public CardItemHandler() {
            super(ApiObject.CARD);
            gh = new CardHelper();
        }

        @Override
        public void onBeforeSaveObject(ApiObject ao) {
            super.onBeforeSaveObject(ao);
        }

        @Override
        public Bundle Handle(JSONObject object) throws JSONException {
            try {
                Bundle bundle = super.Handle(object);
                ApiObject obj = (ApiObject) bundle.getSerializable(ApiObjectItemHandler.OBJECT);

                long id = object.getLong("id");
                String status = object.getString("status");
                String link = object.getString("link");

                Card ti = new Card(id);
                ti.link = link;
                ti.status = status;
                ti.thumb = obj.thumb;
                ti.title = obj.title;

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

