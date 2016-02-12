package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.GalleryDescriptionCursor;
import com.formulatx.archived.database.helper.GalleryHelper;
import com.formulatx.archived.utils.Log;

import java.util.Arrays;

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

        Arrays.sort(ints);
        String sints = Arrays.toString(ints);

        String parameter = "gallery-list-cache-podcast";
        String gallert_list_cache = FormulaTXApplication.getStringParameter(parameter);
        if (gallert_list_cache == null || !gallert_list_cache.equals(sints)) {
            for (int gid : ints) {
                MyNetwork.queryGallery(gid);
            }
            FormulaTXApplication.setParameter(parameter,sints);
        }

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        GalleryHelper gh = new GalleryHelper(doh);

        return gh.getAllPodcasts();
    }
}
