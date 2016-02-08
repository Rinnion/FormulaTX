package com.formulatx.archived.network.loaders;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.os.Bundle;
import com.formulatx.archived.Settings;
import com.formulatx.archived.Utils;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.fragment.Grids;
import com.formulatx.archived.network.HttpRequester;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.network.handlers.JSONObjectHandler;
import com.formulatx.archived.parsers.Match;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 03.02.2016.
 */
public class GridsAsyncLoader extends AsyncTaskLoader<Grids> {
    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    public static final String GRID = Settings.GRIDS;
    private final String string;
    private final int grid;


    public GridsAsyncLoader(Activity activity, Bundle args) {
        super(activity);
        string = args.getString(TOURNAMENT_POST_NAME);
        grid = args.getInt(GRID);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        deliverResult(null);
    }

    @Override
    public Grids loadInBackground() {
        if (string.equals(TournamentHelper.TOURNAMENT_LADIES_TROPHY)){
            return unWrapSchedule(MyNetwork.getLadiesGrids(grid));
        }
        if (string.equals(TournamentHelper.TOURNAMENT_OPEN)){
            return unWrapSchedule(MyNetwork.getOpenGrids(grid));
        }
        return new Grids();
    }

    private Grids unWrapSchedule(Bundle ladiesSchedule) {
        if (ladiesSchedule == null) return new Grids();
        Bundle http = ladiesSchedule.getBundle(HttpRequester.RESULT_HTTP);
        if (http == null) return new Grids();
        Bundle parse = http.getBundle(HttpRequester.RESULT_HTTP_PARSE);
        if (parse == null) return new Grids();
        String string = parse.getString(JSONObjectHandler.VALUE);
        try {
            JSONObject json = new JSONObject(string);
            Grids grids = new Grids();
            //Schedule schedule = new Schedule();
            for (int i = 0; i<json.length(); i++) {
                if (!json.has(String.valueOf(i+1))) continue;
                JSONObject round = json.getJSONObject(String.valueOf(i+1));
                Grids.Round rnd = grids.addRound("Раунд " + (i+1));
                for(int k=0; k<round.length()-1; k++){
                    JSONObject jsonMatch = round.getJSONObject(String.valueOf(k));
                    JSONObject jsonObject = new JSONObject();
                    for (int j = 0; j<jsonMatch.length(); j++) {
                        jsonObject.put("team" + (j+1), jsonMatch.getJSONObject(String.valueOf(j)));
                    }
                    Match match = Match.parseJSONObject(jsonObject);
                    match.header = rnd.cortName;
                    rnd.addMatch(match);
                }
            }
            return grids;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Grids();
    }
}
