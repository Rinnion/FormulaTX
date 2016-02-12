package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Settings;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.GalleryDescriptionCursor;
import com.formulatx.archived.database.helper.GalleryHelper;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.utils.Log;

import java.util.Arrays;
import java.util.Calendar;

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
        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        GalleryHelper aoh=new GalleryHelper(doh);
        int[] ints = args.getIntArray("ints");
        if (ints != null) {
            deliverResult(aoh.getAllGalleriesWithContent(ints, type));
        }else {
            deliverResult(aoh.getAllGalleries(type));
        }
    }

    @Override
    public GalleryDescriptionCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        int[] ints = args.getIntArray("ints");
        if (ints == null){
            ints = MyNetwork.queryGalleryList();
        }

        Arrays.sort(ints);
        String sints = Arrays.toString(ints);

        String parameter = "gallery-list-cache-gallery";
        String gallert_list_cache = FormulaTXApplication.getStringParameter(parameter);
        if (gallert_list_cache==null || !gallert_list_cache.equals(sints)) {
            for (int gid : ints) {
                MyNetwork.queryGallery(gid);
            }
            FormulaTXApplication.setParameter(parameter,sints);
        }

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        GalleryHelper gh = new GalleryHelper(doh);

        return gh.getAllGalleriesWithContent(ints, type);
    }
}
