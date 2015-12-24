package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.helper.UserHelper;
import com.rinnion.archived.database.model.User;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tretyakov on 07.07.2015.
 */
public class UserHandler extends JSONObjectHandler {
    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        long id = object.getLong("UserId");
        String name = object.getString("Name");
        String avatar = object.getString("Photo");

        UserHelper mh = new UserHelper(ArchivedApplication.getDatabaseOpenHelper());
        User user = new User(id, name, avatar, 0);
        mh.add(user);

        Bundle bundle = new Bundle();
        bundle.putSerializable(JSONObjectHandler.VALUE, user);

        return bundle;
    }

}
