package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.GalleryDescriptionCursor;
import com.formulatx.archived.database.helper.GalleryHelper;
import com.formulatx.archived.utils.Log;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class PodcastAsyncLoader extends AsyncTaskLoader<GalleryDescriptionCursor> {

    private String TAG = getClass().getSimpleName();

    public PodcastAsyncLoader(Context context) {
        super(context);
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
        GalleryHelper aoh=new GalleryHelper(doh);
        deliverResult(aoh.getAllPodcasts());
    }

    @Override
    public GalleryDescriptionCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");


        int[] ints = MyNetwork.queryPodcastList();

        for (int gid : ints) {
            MyNetwork.queryGallery(gid);
        }

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        GalleryHelper gh = new GalleryHelper(doh);

        return gh.getAllPodcasts();
    }
}
