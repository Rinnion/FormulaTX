package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Utils;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ParserCursor;
import com.rinnion.archived.database.helper.ParserHelper;
import com.rinnion.archived.database.helper.ParserMatchHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.database.model.Parser;
import com.rinnion.archived.database.model.Table;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.network.loaders.cursor.TableCursor;
import com.rinnion.archived.parsers.Match;
import com.rinnion.archived.parsers.ParserFactory;
import com.rinnion.archived.utils.Log;
import org.json.JSONException;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class ParserAsyncLoader extends AsyncTaskLoader<TableCursor> {

    private final Context context;
    private final String post_name;
    private final String type;
    private final String settings;
    private String page;
    private String TAG = getClass().getSimpleName();

    public ParserAsyncLoader(Context context, String post_name, String type, String settings, String page) {
        super(context);
        this.context = context;
        this.post_name = post_name;
        this.type = type;
        this.settings = settings;
        this.page = page;
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
        if (t == null) {
            deliverResult(null);
        } else {
            int[] ints = getParsersArrayFromTournament();
            ParserMatchHelper pmh = new ParserMatchHelper(doh);
            TableCursor all = pmh.getAll(ints, type, settings, page);
            deliverResult(all);
            return;
        }
    }

    @Override
    public TableCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        //FIXME: Load only parser that matches type, settings
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament t = th.getByPostName(post_name);
        if (t != null) {
            int[] ints = getParsersArrayFromTournament();
            for (int gid : ints) {
                MyNetwork.queryParser(gid);
            }
            ParserHelper ph = new ParserHelper(doh);
            ParserCursor pc = ph.getAllWithSystemAndSettings(ints, type, settings);
            ParserMatchHelper pmh = new ParserMatchHelper(doh);
            pmh.clear();
            ParserFactory pf =new ParserFactory();
            int i = 1;
            while (!pc.isAfterLast()){
                Match[] parse = pf.parse(pc.getData());
                for (Match p:parse){
                    Table table = new Table();
                    try {
                        table.data = p.getJSONObject().toString();
                        table.number = i++;
                        table.page = p.type;
                        table.type = p.type;
                        table.parser = pc.getColId();
                    } catch (JSONException e) {
                        Log.e(TAG, "wrong data");
                        continue;
                    }
                    pmh.add(table);
                }
                pc.moveToNext();
            }
            TableCursor all = pmh.getAll(ints, type, settings, page);
            return all;
        }
        return null;

    }
}
