package com.rinnion.archived.database.model;

import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import org.json.JSONException;
import org.json.JSONObject;

public class News extends ApiObject {

    public News(long id) {
        super(id, ApiObjectTypes.EN_Object);
    }

    public News(JSONObject jsonObject) throws JSONException {
        super(jsonObject,ApiObjectTypes.EN_News);

    }
}
