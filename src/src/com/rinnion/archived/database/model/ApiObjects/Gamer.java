package com.rinnion.archived.database.model.ApiObjects;

import com.rinnion.archived.database.model.ApiObject;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tretyakov on 28.12.2015.
 */
public class Gamer extends ApiObject {
    public Gamer(long id) {
        super(id, ApiObjectTypes.EN_Gamer);
    }

    public Gamer(JSONObject jsonObject) throws JSONException {
       super(jsonObject,ApiObjectTypes.EN_Gamer);

    }

    



}
