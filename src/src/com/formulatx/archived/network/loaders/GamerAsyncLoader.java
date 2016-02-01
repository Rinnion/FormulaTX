package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.formulatx.archived.database.cursor.GamerCursor;
import com.formulatx.archived.database.helper.GamerHelper;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.utils.Log;

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
        GamerHelper aoh=new GamerHelper();
        deliverResult(aoh.getAllByParent(parent));
    }

    @Override
    public GamerCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        int[] iaGamerList = MyNetwork.getIntArray(MyNetwork.queryGamerList(parent));
        if (iaGamerList != null) {
            for (int i : iaGamerList) {
                MyNetwork.queryGamer(i);
            }
        }
        GamerHelper aoh=new GamerHelper();
        return aoh.getAllByParent(parent);
    }

}
