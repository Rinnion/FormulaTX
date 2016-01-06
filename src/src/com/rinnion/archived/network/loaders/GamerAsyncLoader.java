package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.database.cursor.GamerCursor;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.network.MyNetwork;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class GamerAsyncLoader extends AsyncTaskLoader<GamerCursor> {

    private String TAG = getClass().getSimpleName();
    private long parent;

    public GamerAsyncLoader(Context context, long parent) {
        super(context);
        this.parent = parent;
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
        GamerHelper aoh=new GamerHelper(doh);
        deliverResult(aoh.getAllByParent(parent));
    }

    @Override
    public GamerCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        int[] iaGamerList = MyNetwork.getIntArray(MyNetwork.queryGamerList(parent));
        if (iaGamerList == null) return null;
        for (int i : iaGamerList) {
            MyNetwork.queryGamer(i);
        }

        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        GamerHelper aoh=new GamerHelper(doh);
        return aoh.getAllByParent(parent);
    }

}
