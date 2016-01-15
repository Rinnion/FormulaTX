package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ProductCursor;
import com.rinnion.archived.database.helper.ProductHelper;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.utils.Log;

/**
 * Created by tretyakov on 08.07.2015.
 * Load product asychronosly from loaders
 */
public class ProductAsyncLoader extends AsyncTaskLoader<ProductCursor> {

    private String TAG = getClass().getSimpleName();

    public ProductAsyncLoader(Context context) {
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
        ProductHelper aoh=new ProductHelper(doh);
        deliverResult(aoh.getAll());
    }

    @Override
    public ProductCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        int[] iaProductList = MyNetwork.getIntArray(MyNetwork.queryProductList());
        if (iaProductList != null) {
            for (int i : iaProductList) {
                MyNetwork.queryProduct(i);
                //MyNetwork.queryGamer(i);
            }
        }
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        ProductHelper aoh=new ProductHelper(doh);
        return aoh.getAll();
    }

}
