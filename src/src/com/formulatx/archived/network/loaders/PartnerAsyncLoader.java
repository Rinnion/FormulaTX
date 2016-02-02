package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.formulatx.archived.database.cursor.PartnerCursor;
import com.formulatx.archived.database.helper.PartnerHelper;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.utils.Log;

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
        PartnerCursor allPartner = aoh.getAllPartner();
        if (allPartner.getCount() == 0){
            deliverResult(null);
            return;
        }
        deliverResult(allPartner);
    }

    @Override
    public PartnerCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        MyNetwork.queryPartnerList();

        PartnerHelper aoh=new PartnerHelper();
        return aoh.getAllPartner();
    }

}
