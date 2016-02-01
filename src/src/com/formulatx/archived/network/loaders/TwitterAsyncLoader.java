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
        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TwitterHelper aoh=new TwitterHelper(doh);
        deliverResult(aoh.getAllItems());
    }

    @Override
    public TwitterItemCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TwitterHelper aoh = new TwitterHelper(doh);
        TournamentHelper th = new TournamentHelper(doh);
        ApiObjectCursor all = th.getAll();
        while (!all.isAfterLast()) {
            ApiObject tournament = all.getItem();
            Log.d(TAG, String.valueOf(tournament));
            if (tournament != null) {
                aoh.detachAllReferences(tournament.id);
                try {
                    int[] ints = Utils.getIntListFromJSONArray(tournament.references_include);
                    for (int i : ints) {
                        aoh.attachReference(tournament.id,i);
                        MyNetwork.queryTwitter(i);
                    }
                }catch (Exception ignored){
                    Log.d(TAG, "Couldn't parse references for " + tournament + ". " + ignored.getMessage());
                }
            }
            all.moveToNext();
        }
        TwitterItemCursor cursor;
        cursor = aoh.getAllItems();
        return cursor;
    }
}
