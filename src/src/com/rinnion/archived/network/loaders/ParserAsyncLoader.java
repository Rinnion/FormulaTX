package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ParserCursor;
import com.rinnion.archived.database.helper.ParserHelper;
import com.rinnion.archived.database.model.Parser;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.utils.Log;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class ParserAsyncLoader extends AsyncTaskLoader<ParserCursor> {

    private final Context context;
    private final int[] ints;
    private String TAG = getClass().getSimpleName();

    public ParserAsyncLoader(Context context, int[] ints) {
        super(context);
        this.context = context;
        this.ints = ints;
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
        ParserHelper ph = new ParserHelper(doh);
        deliverResult(ph.getAllWithType(ints, Parser.SPBOPEN_TIMETABLE));
    }

    @Override
    public ParserCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        for (int gid : ints) {
            MyNetwork.queryParser(gid);
        }
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        ParserHelper ph = new ParserHelper(doh);
        return ph.getAllWithType(ints, Parser.SPBOPEN_TIMETABLE);
    }
}
