package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.GalleryItemCursor;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.database.helper.GalleryHelper;
import com.formulatx.archived.database.helper.TournamentHelper;
import org.lorecraft.phparser.SerializedPhpParser;

import java.util.Map;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class InstagramAsyncLoader extends AsyncTaskLoader<GalleryItemCursor> {

    private String TAG = getClass().getSimpleName();
    private String parent;
    private long api_object_id;
    private String type;

    public InstagramAsyncLoader(Context context, long api_object_id, String type) {
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
        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        GalleryHelper aoh=new GalleryHelper(doh);
        deliverResult(aoh.getAllByApiObjectAndItemTypeId(api_object_id, type));
    }

    @Override
    public GalleryItemCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        GalleryItemCursor cursor = null;
        Tournament tournament = th.get(api_object_id);
        if (tournament != null) {
            String gallery_include = tournament.gallery_include;
            SerializedPhpParser php = new SerializedPhpParser(gallery_include);
            Map parse = (Map) php.parse();
            GalleryHelper aoh = new GalleryHelper(doh);
            for (Object item : parse.keySet()) {
                long l = Long.parseLong(parse.get(item).toString());
                MyNetwork.queryGallery(l);
                aoh.attachGallery(api_object_id, l);
            }
            cursor = aoh.getAllByApiObjectAndItemTypeId(api_object_id, type);
        }

        return cursor;
    }
}
