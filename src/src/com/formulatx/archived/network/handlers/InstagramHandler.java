package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import org.json.JSONObject;

public class InstagramHandler extends FormulaTXArrayResponseHandler {


    public InstagramHandler(long id) {
        super(new InstagramItemHandler(id));
    }

    @Override
    public Bundle onErrorStatus(JSONObject message, Bundle bundle) {
        return Bundle.EMPTY;
    }
}
