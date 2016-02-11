package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Settings;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.ProductCursor;
import com.formulatx.archived.database.helper.ProductHelper;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.utils.Log;

import java.util.Calendar;

/**
 * Created by tretyakov on 08.07.2015.
 * Load product asychronosly from loaders
 */
public class ProductAsyncLoader extends AsyncTaskLoader<ProductCursor> {

    private String TAG = getClass().getSimpleName();
    private boolean favorites = false;

    public ProductAsyncLoader(Context context, Bundle args) {
        super(context);
        favorites = ((args == null) || (args.getBoolean("favorites")));
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
        ProductHelper aoh=new ProductHelper(doh);

            ProductCursor all = (favorites) ? aoh.getFavorites() : aoh.getAll();
        if (all.getCount() == 0)
        {
            deliverResult(null);
            return;
        }
        deliverResult(all);
    }

    @Override
    public ProductCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        if (FormulaTXApplication.getLongParameter("ProductAsyncLoader", 0) + Settings.OUT_OF_DATE_LOADER_PRODUCTS < Calendar.getInstance().getTimeInMillis()) {
            FormulaTXApplication.setParameter("ProductAsyncLoader", Calendar.getInstance().getTimeInMillis());
            MyNetwork.queryProduct();
        }

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        ProductHelper aoh=new ProductHelper(doh);
        ProductCursor all = (favorites) ? aoh.getFavorites() : aoh.getAll();

        return all;
    }

}
