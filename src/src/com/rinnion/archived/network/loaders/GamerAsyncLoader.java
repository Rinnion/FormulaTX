package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.database.cursor.GamerCursor;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.network.MyNetwork;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class GamerAsyncLoader extends AsyncTaskLoader<GamerCursor> {

    private String TAG = getClass().getSimpleName();
    private String parent;

    public GamerAsyncLoader(Context context, String parent) {
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
        deliverResult(aoh.getAll());
    }

    @Override
    public GamerCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        int[] ints = MyNetwork.queryGamerList(parent);
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        GamerHelper aoh=new GamerHelper(doh);
        return aoh.getAllByParent(parent);
    }
}
