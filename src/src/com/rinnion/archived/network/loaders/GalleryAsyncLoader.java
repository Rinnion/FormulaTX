package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.cursor.GamerCursor;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.network.MyNetwork;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class GalleryAsyncLoader extends AsyncTaskLoader<GalleryItemCursor> {

    private String TAG = getClass().getSimpleName();
    private String parent;
    private long api_object_id;
    private String type;

    public GalleryAsyncLoader(Context context, long api_object_id, String type) {
        super(context);
        this.api_object_id = api_object_id;
        this.type = type;
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
        GalleryHelper aoh=new GalleryHelper(doh);
        deliverResult(aoh.getAllByApiObjectAndItemTypeId(api_object_id, type));
    }

    @Override
    public GalleryItemCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        //FIXME: get and parse gallery
        //int[] ints = MyNetwork.queryGamerList(parent);
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        GalleryHelper aoh=new GalleryHelper(doh);
        return aoh.getAllByApiObjectAndItemTypeId(api_object_id, type);
    }
}
