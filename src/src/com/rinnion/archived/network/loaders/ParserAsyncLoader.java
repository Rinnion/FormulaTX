package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ParserCursor;
import com.rinnion.archived.database.helper.ParserHelper;
import com.rinnion.archived.database.model.Parser;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.network.loaders.cursor.TableCursor;
import com.rinnion.archived.utils.Log;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class ParserAsyncLoader extends AsyncTaskLoader<TableCursor> {

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
        //DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        //ParserHelper ph = new ParserHelper(doh);
        //deliverResult(ph.getAllWithType(ints, Parser.SPBOPEN_TIMETABLE));
        deliverResult(getTableCursor());
    }

    private TableCursor getTableCursor() {
        TableCursor tc = new TableCursor();

        tc.addRow("live", 1,  "live", "{\"type\":\"live\",\"team1\":{\"gamers\":[{\"name\":\"М. Гранолльерс\",\"photo\":\"http://spbopen.ru/assets/Granollers_0499.jpg\",\"cc\":\"\"}],\"extra\":\"\",\"r1\":\"6\",\"r2\":\"6\",\"r3\":\"0\",\"shot\":true},\"team2\":{\"gamers\":[{\"name\":\"Танаси Кокинакис\",\"photo\":\"http://spbopen.ru/assets/Kokkinakis_5777.jpg\",\"cc\":\"\"}],\"extra\":\"\",\"r1\":\"3\",\"r2\":\"3\",\"r3\":\"0\",\"shot\":false}}");
        tc.addRow("live", 2,  "live", "{\"type\":\"live\",\"team1\":{\"gamers\":[{\"name\":\"М. Гранолльерс\",\"photo\":\"http://spbopen.ru/assets/Granollers_0499.jpg\",\"cc\":\"\"}],\"extra\":\"\",\"r1\":\"6\",\"r2\":\"6\",\"r3\":\"0\",\"shot\":true},\"team2\":{\"gamers\":[{\"name\":\"Танаси Кокинакис\",\"photo\":\"http://spbopen.ru/assets/Kokkinakis_5777.jpg\",\"cc\":\"\"}],\"extra\":\"\",\"r1\":\"3\",\"r2\":\"3\",\"r3\":\"0\",\"shot\":false}}");
        tc.addRow("live", 3,  "live", "{\"type\":\"live\",\"team1\":{\"gamers\":[{\"name\":\"Симоне Болелли\",\"photo\":\"http://spbopen.ru/assets/Bolelli_2907.jpg\",\"cc\":\"\"}],\"extra\":\"\",\"r1\":\"4\",\"r2\":\"6\",\"r3\":\"6\",\"shot\":true},\"team2\":{\"gamers\":[{\"name\":\"Андрей Рублёв\",\"photo\":\"http://spbopen.ru/assets/rublev.jpg\",\"cc\":\"\"}],\"extra\":\"\",\"r1\":\"6\",\"r2\":\"3\",\"r3\":\"1\",\"shot\":false}}\n");

        return tc;
    }

    @Override
    public TableCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        return getTableCursor();
        /*for (int gid : ints) {
            MyNetwork.queryParser(gid);
        }
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        ParserHelper ph = new ParserHelper(doh);
        return ph.getAllWithType(ints, Parser.SPBOPEN_TIMETABLE);
        */
    }
}
