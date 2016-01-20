package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.database.helper.NewsHelper;
import com.rinnion.archived.fragment.adapter.NewsAdapter;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.network.MyNetwork;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class NewsAsyncLoader extends AsyncTaskLoader<ApiObjectCursor> {

    private String TAG = getClass().getSimpleName();

    public NewsAsyncLoader(Context context) {
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
        NewsHelper nh = new NewsHelper(ArchivedApplication.getDatabaseOpenHelper());
        deliverResult(nh.getAll());
    }

    @Override
    public ApiObjectCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        NewsHelper nh = new NewsHelper(ArchivedApplication.getDatabaseOpenHelper());
        NewsCursor newsCursor = nh.getAll();
        return newsCursor;
    }
}
