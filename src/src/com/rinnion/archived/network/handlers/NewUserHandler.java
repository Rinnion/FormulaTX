package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.helper.UserHelper;
import com.rinnion.archived.database.model.User;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Async task sending comment
 */
public class NewUserHandler extends JSONObjectHandler {
    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        long id = object.getLong("UserId");
        String name = object.getString("Name");
        String avatar = object.getString("Photo");
        String token = object.getString("AuthToken");

        //TODO: Should take other field f.e. AuthMessgeToken
        String message_token = object.getString("AuthToken");

        UserHelper mh = new UserHelper(ArchivedApplication.getDatabaseOpenHelper());
        User user = new User(id, name, avatar, 0, token, message_token);
        mh.add(user);

        Bundle bundle = new Bundle();
        bundle.putSerializable(JSONObjectHandler.VALUE, user);

        return bundle;
    }

}
