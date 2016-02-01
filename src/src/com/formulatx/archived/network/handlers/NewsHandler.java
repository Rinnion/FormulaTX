package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.database.model.ApiObject;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class NewsHandler extends FormulaTXArrayResponseHandler {

    static class NewsItemHandler extends ApiObjectItemHandler {
        private String parent;

        public NewsItemHandler(String parent) {
            super(ApiObject.NEWS);
            this.parent = parent;
        }

        @Override
        public void onBeforeSaveObject(ApiObject ao) {
            ao.parent = parent;
            super.onBeforeSaveObject(ao);
        }
    }

    public NewsHandler(String post_name) {
        super(new NewsItemHandler(post_name));
    }

    @Override
    public Bundle onErrorStatus(JSONObject message, Bundle bundle) {
        return Bundle.EMPTY;
    }
}


