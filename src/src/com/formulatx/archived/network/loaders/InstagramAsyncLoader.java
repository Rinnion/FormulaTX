package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.GalleryItemCursor;
import com.formulatx.archived.database.cursor.InstagramItemCursor;
import com.formulatx.archived.database.cursor.TwitterItemCursor;
import com.formulatx.archived.database.helper.InstagramHelper;
import com.formulatx.archived.database.helper.TwitterHelper;
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
public class InstagramAsyncLoader extends AsyncTaskLoader<InstagramItemCursor> {

    private String TAG = getClass().getSimpleName();

    public InstagramAsyncLoader(Context context) {
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
        InstagramHelper aoh=new InstagramHelper();
        deliverResult(aoh.getAllItems());
    }

    @Override
    public InstagramItemCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        MyNetwork.queryInstagram(1);

        InstagramHelper th = new InstagramHelper();
        InstagramItemCursor cursor;
        cursor = th.getAllItems();
        return cursor;
    }
}
