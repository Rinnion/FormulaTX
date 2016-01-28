package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Utils;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ParserCursor;
import com.rinnion.archived.database.helper.ParserHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.utils.Log;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class GridsAsyncLoader extends AsyncTaskLoader<ParserCursor> {

    private final Context context;
    private String post_name;
    private final String type;
    private final String settings;
    private static String TAG = "GridsAsyncLoader";

    public GridsAsyncLoader(Context context, String post_name, String type, String settings) {
        super(context);
        this.context = context;
        this.post_name = post_name;
        this.type = type;
        this.settings = settings;
        Log.d(TAG, ".ctor");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    private int[] getParsersArrayFromTournament() {
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament tournament = th.getByPostName(post_name);

        int[] intArray;
        intArray = tournament == null ? new int[0] : Utils.getIntListFromJSONArray(tournament.parsers_include);

        return intArray;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament t = th.getByPostName(post_name);
        ParserCursor all = null;
        if (t != null) {
            int[] ints = getParsersArrayFromTournament();
            ParserHelper ph = new ParserHelper(doh);
            //all = ph.getAllWithType(ints, type);
        }
        deliverResult(null);
    }

    @Override
    public ParserCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        int[] ints = getParsersArrayFromTournament();
        for (int gid : ints) {
            MyNetwork.queryParser(gid);
        }
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        ParserHelper ph = new ParserHelper(doh);
        //ParserCursor all = ph.getAllWithType(ints, type);

        return null;
    }
}
