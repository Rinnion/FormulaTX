package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Utils;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.ApiObjectCursor;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.database.cursor.TwitterItemCursor;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.database.helper.TwitterHelper;

public class TwitterAsyncLoader extends AsyncTaskLoader<TwitterItemCursor> {

    private String TAG = getClass().getSimpleName();

    public TwitterAsyncLoader(Context context) {
        super(context);
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
        TwitterHelper aoh=new TwitterHelper();
        deliverResult(aoh.getAllItems());
    }

    @Override
    public TwitterItemCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        MyNetwork.queryTwitter(1);

        TwitterHelper th = new TwitterHelper();
        TwitterItemCursor cursor;
        cursor = th.getAllItems();
        return cursor;
    }
}
