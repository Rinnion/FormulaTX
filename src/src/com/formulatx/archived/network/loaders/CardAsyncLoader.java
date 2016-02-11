package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Settings;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.CardCursor;
import com.formulatx.archived.database.cursor.ProductCursor;
import com.formulatx.archived.database.helper.ProductHelper;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.database.helper.CardHelper;
import com.formulatx.archived.utils.Log;

import java.util.Calendar;

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
        CardHelper aoh=new CardHelper();
        CardCursor all = aoh.getAll();
        if (all.getCount() == 0){
            deliverResult(null);
            return;
        }
        deliverResult(all);
    }

    @Override
    public CardCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        if (FormulaTXApplication.getLongParameter("CardAsyncLoader", 0) + Settings.OUT_OF_DATE_LOADER_PRODUCTS < Calendar.getInstance().getTimeInMillis()) {
            FormulaTXApplication.setParameter("CardAsyncLoader", Calendar.getInstance().getTimeInMillis());
            MyNetwork.queryProduct();
            MyNetwork.queryCard();
        }

        CardHelper aoh=new CardHelper();
        CardCursor all = aoh. getAll();

        return all;
    }

}
