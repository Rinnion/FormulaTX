package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.model.User;
import com.formulatx.archived.database.helper.UserHelper;
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

        UserHelper mh = new UserHelper(FormulaTXApplication.getDatabaseOpenHelper());
        User user = new User(id, name, avatar, 0, token, message_token);
        mh.add(user);

        Bundle bundle = new Bundle();
        bundle.putSerializable(VALUE, user);

        return bundle;
    }

}
