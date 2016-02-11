package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.ProductHelper;
import com.formulatx.archived.database.helper.TwitterHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.database.model.ApiObjects.Product;
import com.formulatx.archived.database.model.TwitterItem;
import com.formulatx.archived.utils.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by alekseev on 29.12.2015.
 */

public class ProductHandler extends FormulaTXArrayResponseHandler {


    public ProductHandler() {
        super(new ProductItemHandler());
    }

    private static class ProductItemHandler extends ApiObjectItemHandler {

        private static final String TAG = "ProductItemHandler";
        private final ProductHelper gh;

        public ProductItemHandler() {
            super(ApiObject.PRODUCT);
            gh = new ProductHelper(FormulaTXApplication.getDatabaseOpenHelper());
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

                String price = object.getString("price");
                String top = object.getString("TOP");
                long id = object.getLong("id");
                Product ti = new Product(id);
                ti.price = price;
                ti.top = top;
                ti.content = obj.content;
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
