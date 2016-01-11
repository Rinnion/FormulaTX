package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Utils;
import com.rinnion.archived.database.helper.CommentHelper;
import com.rinnion.archived.database.model.Comment;
import com.rinnion.archived.database.model.User;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tretyakov on 07.07.2015.
 */
public class CommentHandler extends JSONObjectHandler {

    private long mMessageId;

    public CommentHandler(long messageId) {

        mMessageId = messageId;
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        long id = object.getLong("CommentId");
        String content = object.getString("Content");
        long date_post = Utils.getTimeStamp(object, "Date");
        JSONObject jsonUser = object.getJSONObject("User");
        UserHandler uh = new UserHandler();
        Bundle userBundle = uh.Handle(jsonUser);
        User user = (User) userBundle.getSerializable(JSONObjectHandler.VALUE);

        CommentHelper mh = new CommentHelper(ArchivedApplication.getDatabaseOpenHelper());
        Comment comment = new Comment(id, content, date_post, user.id, 0, user.avatar, user.name, mMessageId);
        mh.add(comment);

        Bundle bundle = new Bundle();
        bundle.putSerializable(JSONObjectHandler.VALUE, comment);

        return bundle;
    }



}
