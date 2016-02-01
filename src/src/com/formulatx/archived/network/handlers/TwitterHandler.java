package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import org.json.JSONObject;

public class TwitterHandler extends FormulaTXArrayResponseHandler {


    public TwitterHandler(long id) {
        super(new TwitterItemHandler(id));
    }

    @Override
    public Bundle onErrorStatus(JSONObject message, Bundle bundle) {
        return Bundle.EMPTY;
    }
}
