package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.cursor.TwitterItemCursor;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.helper.TwitterHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.network.MyNetwork;
import org.lorecraft.phparser.SerializedPhpParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class TwitterAsyncLoader extends AsyncTaskLoader<TwitterItemCursor> {

    private String TAG = getClass().getSimpleName();
    private long api_object_id;

    public TwitterAsyncLoader(Context context, long api_object_id) {
        super(context);
        this.api_object_id = api_object_id;
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
        TwitterHelper aoh=new TwitterHelper(doh);
        deliverResult(aoh.getAllItems());
    }

    @Override
    public TwitterItemCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");

        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        TwitterItemCursor cursor = null;
        Tournament tournament = th.get(api_object_id);
        Log.d(TAG, String.valueOf(tournament));
        if (tournament != null) {
            String references_include = tournament.references_include;
            Log.d(TAG, String.valueOf(references_include));
            SerializedPhpParser php = new SerializedPhpParser(references_include);
            Map parse = (Map) php.parse();
            TwitterHelper aoh = new TwitterHelper(doh);
            for (Object item : parse.keySet()) {
                Log.d(TAG, "key:'" + String.valueOf(item) + "'");
                try {
                    String value = parse.get(item).toString();
                    Log.d(TAG, "value:'" + String.valueOf(value) + "'");
                    long l = Long.parseLong(value);
                    MyNetwork.queryTwitter(l);
                    aoh.attachReference(api_object_id, l);
                } catch (Exception ignored) {
                }
            }
            cursor = aoh.getAllItems();
        }

        return cursor;
    }
}
