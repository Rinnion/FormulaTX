package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Utils;
import com.formulatx.archived.database.model.User;
import com.formulatx.archived.database.helper.CommentHelper;
import com.formulatx.archived.database.model.Comment;
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
        User user = (User) userBundle.getSerializable(VALUE);

        CommentHelper mh = new CommentHelper(FormulaTXApplication.getDatabaseOpenHelper());
        Comment comment = new Comment(id, content, date_post, user.id, 0, user.avatar, user.name, mMessageId);
        mh.add(comment);

        Bundle bundle = new Bundle();
        bundle.putSerializable(VALUE, comment);

        return bundle;
    }



}
