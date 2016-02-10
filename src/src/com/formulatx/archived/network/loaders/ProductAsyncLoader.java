package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.ProductCursor;
import com.formulatx.archived.database.helper.ProductHelper;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.utils.Log;

/**
 * Created by tretyakov on 08.07.2015.
 * Load product asychronosly from loaders
 */
public class ProductAsyncLoader extends AsyncTaskLoader<ProductCursor> {

    private String TAG = getClass().getSimpleName();

    public ProductAsyncLoader(Context context, Bundle args) {
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
        ProductHelper aoh=new ProductHelper(doh);
        ProductCursor all = aoh.getAll();
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

        /*
        int[] iaProductList = MyNetwork.getIntArray(MyNetwork.queryProductList());
        if (iaProductList != null) {
            for (int i : iaProductList) {
                MyNetwork.queryProduct(i);
            }
        }
        */

        MyNetwork.queryProduct();

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        ProductHelper aoh=new ProductHelper(doh);
        return aoh.getAll();
    }

}
