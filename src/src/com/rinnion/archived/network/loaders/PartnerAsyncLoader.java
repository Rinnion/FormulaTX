package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.database.cursor.CardCursor;
import com.rinnion.archived.database.cursor.PartnerCursor;
import com.rinnion.archived.database.helper.CardHelper;
import com.rinnion.archived.database.helper.PartnerHelper;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.utils.Log;

/**
 * Created by tretyakov on 08.07.2015.
 * Load product asychronosly from loaders
 */
public class PartnerAsyncLoader extends AsyncTaskLoader<PartnerCursor> {

    private String TAG = getClass().getSimpleName();

    public PartnerAsyncLoader(Context context) {
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
        PartnerHelper aoh=new PartnerHelper();
        deliverResult(aoh.getAllPartner());
    }

    @Override
    public PartnerCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        MyNetwork.queryPartnerList();

        PartnerHelper aoh=new PartnerHelper();
        return aoh.getAllPartner();
    }

}
