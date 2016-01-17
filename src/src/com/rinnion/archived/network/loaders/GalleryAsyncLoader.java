package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import com.rinnion.archived.database.cursor.GalleryDescriptionCursor;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.network.MyNetwork;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class GalleryAsyncLoader extends AsyncTaskLoader<GalleryDescriptionCursor> {

    private String TAG = getClass().getSimpleName();
    private Bundle args;
    private String type;

    public GalleryAsyncLoader(Context context, Bundle args, String type) {
        super(context);
        this.args = args;
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
        int[] ints = args.getIntArray("ints");
        if (ints != null) {
            deliverResult(aoh.getAllGalleries(ints));
        }else {
            deliverResult(aoh.getAllGalleries());
        }
    }

    @Override
    public GalleryDescriptionCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        int[] ints = args.getIntArray("ints");
        if (ints == null){
            ints = MyNetwork.queryGalleryList();
        }
        for (int gid : ints) {
            MyNetwork.queryGallery(gid);
        }

        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        GalleryHelper gh = new GalleryHelper(doh);

        return gh.getAllGalleries(ints);
    }
}
