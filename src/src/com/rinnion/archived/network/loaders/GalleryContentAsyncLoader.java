package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.GalleryDescriptionCursor;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.utils.Log;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class GalleryContentAsyncLoader extends AsyncTaskLoader<GalleryItemCursor> {

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
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        long gid = args.getLong("GALLERY");
        GalleryHelper gh=new GalleryHelper(doh);
        deliverResult(gh.getAllItemsByGalleryIdAndType(gid, type));
    }

    @Override
    public GalleryItemCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        long gid = args.getLong("GALLERY");

        MyNetwork.queryGallery(gid);

//        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
//        TournamentHelper th = new TournamentHelper(doh);
//        GalleryItemCursor cursor = null;
//        Tournament tournament = th.get(args);
//        if (tournament != null) {
//            GalleryHelper aoh = new GalleryHelper(doh);
//            try {
//                String gallery_include = tournament.gallery_include;
//                SerializedPhpParser php = new SerializedPhpParser(gallery_include);
//                Map parse = (Map) php.parse();
//                for (Object item : parse.keySet()) {
//                    long l = Long.parseLong(parse.get(item).toString());
//                    MyNetwork.queryGallery(l);
//                    aoh.attachGallery(args, l);
//                }
//            }catch(Exception ignored){
//                Log.w(TAG, ignored.getMessage());
//            }
//            cursor = aoh.getAllByApiObjectAndItemTypeId(args, type);
//        }


        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        GalleryHelper gh = new GalleryHelper(doh);
        return gh.getAllItemsByGalleryIdAndType(gid, type);
    }
}
