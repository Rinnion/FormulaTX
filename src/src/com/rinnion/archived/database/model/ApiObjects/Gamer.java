package com.rinnion.archived.database.model.ApiObjects;

import com.rinnion.archived.database.model.ApiObject;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tretyakov on 28.12.2015.
 */
public class Gamer extends ApiObject {

    public String name;
    public String surname;
    public String full_name;
    public float rating;
    public String country;
    public String flag;

    public Gamer(long id) {
        super(id, ApiObjectTypes.EN_Gamer);
    }
}
