package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.database.helper.TwitterHelper;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.database.model.TwitterItem;
import com.rinnion.archived.network.MyNetworkContentContract;
import org.json.JSONArray;
import org.json.JSONException;
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
