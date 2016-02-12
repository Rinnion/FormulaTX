package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.GalleryItemCursor;
import com.formulatx.archived.database.helper.GalleryHelper;
import com.formulatx.archived.utils.Log;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class GalleryContentAsyncLoader extends AsyncTaskLoader<GalleryItemCursor> {

    public static final String GALLERY = "GALLERY";
    private String TAG = getClass().getSimpleName();
    private Bundle args;
    private String type;

    public GalleryContentAsyncLoader(Context context, Bundle args, String type) {
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
        long gid = args.getLong(GALLERY);
        GalleryHelper gh=new GalleryHelper(doh);
        deliverResult(gh.getAllItemsByGalleryIdAndType(gid, type));
    }

    @Override
    public GalleryItemCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        long gid = args.getLong(GALLERY);

        MyNetwork.queryGallery(gid);

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        GalleryHelper gh = new GalleryHelper(doh);
        return gh.getAllItemsByGalleryIdAndType(gid, type);
    }
}
