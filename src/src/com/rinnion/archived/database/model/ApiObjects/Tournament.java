package com.rinnion.archived.database.model.ApiObjects;

import com.rinnion.archived.database.model.ApiObject;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tretyakov on 28.12.2015.
 */
public class Tournament extends ApiObject {
    public Tournament(long id) {
        super(id, ApiObjectTypes.EN_Object);
    }

    public Tournament(JSONObject jsonObject) throws JSONException {
       super(jsonObject,ApiObjectTypes.EN_Object);

    }

    



}
