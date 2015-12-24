package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.CommentCursor;
import com.rinnion.archived.database.helper.CommentHelper;
import com.rinnion.archived.network.MyNetwork;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class CommentAsyncLoader extends AsyncTaskLoader<CommentCursor> {

    public static final String MESSAGE_ID = "messageId";

    private final Bundle mBundle;
    private String TAG = getClass().getSimpleName();

    public CommentAsyncLoader(Context context, Bundle bundle) {
        super(context);
        mBundle = bundle;
        Log.d(TAG, ".ctor");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        deliverResult(getCommentCursor());
    }

    private CommentCursor getCommentCursor() {
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        CommentHelper ch = new CommentHelper(doh);
        long messageId = mBundle.getLong(MESSAGE_ID);
        return ch.getAllForMessage(messageId);
    }

    @Override
    public CommentCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        long messageId = mBundle.getLong(MESSAGE_ID);
        MyNetwork.queryComments(messageId);
        return getCommentCursor();
    }
}


