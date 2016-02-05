package com.formulatx.archived.network.loaders;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Utils;
import com.formulatx.archived.database.helper.ParserHelper;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.formulatx.archived.database.model.Parser;
import com.formulatx.archived.database.model.Table;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.ParserCursor;
import com.formulatx.archived.database.helper.ParserMatchHelper;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.network.loaders.cursor.ParserDataCursor;
import com.formulatx.archived.parsers.Match;
import com.formulatx.archived.parsers.ParserFactory;
import com.formulatx.archived.utils.Log;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class ParserAsyncLoader extends AsyncTaskLoader<ParserDataCursor> {

    public static final String TYPE = "TYPE";
    public static final String SETTING = "SETTING";
    public static final String PAGE = "PAGE";
    public static final String FORCED = "FORCED";

    private final Context context;
    private final String post_name;
    private final String type;
    private final String settings;
    private final boolean forced;
    private String page;
    private String TAG = getClass().getSimpleName();

    public ParserAsyncLoader(Context context, String post_name, String type, String settings, String page, boolean forced) {
        super(context);
        this.context = context;
        this.post_name = post_name;
        this.type = type;
        this.settings = settings;
        this.page = page;
        this.forced = forced;
        Log.d(TAG, ".ctor");
    }

    public ParserAsyncLoader(Activity activity, String post_name, Bundle args) {
        super(activity);
        this.context = activity;
        this.post_name = post_name;
        this.type = args.getString(TYPE);
        this.settings = args.getString(SETTING);
        this.page = args.getString(PAGE);
        this.forced = args.getBoolean(FORCED);
        Log.d(TAG, ".ctor with bundle");

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    private int[] getParsersArrayFromTournament() {
        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament tournament = th.getByPostName(post_name);

        int[] intArray;
        intArray = tournament == null ? new int[0] : Utils.getIntListFromJSONArray(tournament.parsers_include);

        return intArray;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament t = th.getByPostName(post_name);
        if (t == null) {
            deliverResult(null);
        } else {
            int[] ints = getParsersArrayFromTournament();
            ParserMatchHelper pmh = new ParserMatchHelper(doh);
            ParserDataCursor all = getParserDataCursor(ints, pmh);
            if (all.getCount() == 0) {
                deliverResult(null);
                return;
            } else {
                deliverResult(all);
            }
        }
    }

    @Override
    public ParserDataCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        //FIXME: Load only parser that matches type, settings
        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament t = th.getByPostName(post_name);
        if (t != null) {
            int[] ints = getParsersArrayFromTournament();
            ParserHelper ph = new ParserHelper(doh);
            for (int gid : ints) {
                Parser parser = ph.get(gid);
                if (parser == null || (parser.system.equals(type) && parser.settings.equals(settings)  && (forced || parser.isOutOfDate()))) {
                    MyNetwork.queryParser(gid);
                }
            }
            ParserCursor pc = ph.getAllWithSystemAndSettings(ints, type, settings);
            ParserFactory pf =new ParserFactory();
            ParserMatchHelper pmh = new ParserMatchHelper(doh);

            ArrayList<Parser> prs = new ArrayList<Parser>();
            while (!pc.isAfterLast()) {
                Parser item = pc.getItem();
                prs.add(item);
                pc.moveToNext();
            }

            for (Parser parser: prs){
                if (!parser.isOutOfDateParsed()) continue;
                Match[] parse = pf.parse(parser.data);
                long pid = parser.id;
                pmh.add(parse, pid);
                parser.parsed = Calendar.getInstance().getTimeInMillis();
                ph.merge(parser);
                pc.moveToNext();
            }
            ParserDataCursor all = getParserDataCursor(ints, pmh);
            return all;
        }
        return null;
    }

    private ParserDataCursor getParserDataCursor(int[] ints, ParserMatchHelper pmh) {
        ParserDataCursor openPage = pmh.getAll(ints, type, settings, page);
        if (openPage.getCount() == 0){
            ParserDataCursor pages = pmh.getPages(ints, type, settings);
            page = (pages.getCount()==0) ? page : pages.getPage();
        }

        return pmh.getAll(ints, type, settings, page);
    }

}
