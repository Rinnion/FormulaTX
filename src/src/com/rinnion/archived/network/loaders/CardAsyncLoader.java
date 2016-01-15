package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.CardCursor;
import com.rinnion.archived.database.helper.CardHelper;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.utils.Log;

/**
 * Created by tretyakov on 08.07.2015.
 * Load product asychronosly from loaders
 */
public class CardAsyncLoader extends AsyncTaskLoader<CardCursor> {

    private String TAG = getClass().getSimpleName();

    public CardAsyncLoader(Context context) {
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
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        CardHelper aoh=new CardHelper(doh);
        deliverResult(aoh.getAll());
    }

    @Override
    public CardCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        int[] iaCardList = MyNetwork.getIntArray(MyNetwork.queryCardList());
        if (iaCardList != null) {
            for (int i : iaCardList) {
                MyNetwork.queryCard(i);
            }
        }
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        CardHelper aoh=new CardHelper(doh);
        return aoh.getAll();
    }

}
