package com.formulatx.archived.database.model.ApiObjects;

import com.formulatx.archived.database.model.ApiObject;
import org.json.JSONException;
import org.json.JSONObject;

public class News extends ApiObject {

    public News(long id) {
        super(id);
    }

    public News(JSONObject jsonObject) throws JSONException {
        super(jsonObject);

    }
}
