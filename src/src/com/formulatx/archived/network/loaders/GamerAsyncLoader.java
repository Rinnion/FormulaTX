package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.cursor.GamerCursor;
import com.formulatx.archived.database.helper.GamerHelper;
import com.formulatx.archived.database.helper.PartnerHelper;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
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
        GamerHelper aoh = new GamerHelper();
        GamerCursor all = aoh.getAllByParent(parent);
        if (all.getCount() == 0) {
            deliverResult(null);
            return;
        }
        deliverResult(all);
    }

    @Override
    public GamerCursor loadInBackground() {


        TournamentHelper th = new TournamentHelper(FormulaTXApplication.getDatabaseOpenHelper());
        Tournament trn = th.get(parent);

        Log.d(TAG, "loadInBackground");
        MyNetwork.queryGamerList(trn.id, trn.post_name);

        GamerHelper aoh=new GamerHelper();
        return aoh.getAllByParent(parent);

        /*


        Log.d(TAG, "loadInBackground");
        int[] iaGamerList = MyNetwork.getIntArray(MyNetwork.queryGamerList(parent));
        if (iaGamerList != null) {
            for (int i : iaGamerList) {
                MyNetwork.queryGamer(i);
            }
        }
        GamerHelper aoh=new GamerHelper();
        return aoh.getAllByParent(parent);
        */
    }

}
