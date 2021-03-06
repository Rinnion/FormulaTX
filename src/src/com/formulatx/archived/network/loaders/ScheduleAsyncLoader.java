package com.formulatx.archived.network.loaders;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.os.Bundle;
import com.formulatx.archived.Utils;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.fragment.Schedule;
import com.formulatx.archived.network.HttpRequester;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.network.handlers.JSONObjectHandler;
import com.formulatx.archived.parsers.Match;
import com.formulatx.archived.utils.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 03.02.2016.
 */
public class ScheduleAsyncLoader extends AsyncTaskLoader<Schedule> {
    private static final String TAG = "ScheduleAsyncLoader";
    private final String string;
    private Schedule cursor;

    public ScheduleAsyncLoader(Activity activity, String string) {
        super(activity);
        this.string = string;
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
    public Schedule loadInBackground() {
        if (string.equals(TournamentHelper.TOURNAMENT_LADIES_TROPHY)){
            return unWrapSchedule(MyNetwork.getLadiesSchedule());
        }
        if (string.equals(TournamentHelper.TOURNAMENT_OPEN)){
            return unWrapSchedule(MyNetwork.getOpenSchedule());
        }
        return new Schedule();
    }

    private Schedule unWrapSchedule(Bundle ladiesSchedule) {
        if (ladiesSchedule == null) return new Schedule();
        Bundle http = ladiesSchedule.getBundle(HttpRequester.RESULT_HTTP);
        if (http == null) return new Schedule();
        Bundle parse = http.getBundle(HttpRequester.RESULT_HTTP_PARSE);
        if (parse == null) return new Schedule();
        String string = parse.getString(JSONObjectHandler.VALUE);
        try {
            JSONObject json = new JSONObject(string);
            Schedule schedule = new Schedule();
            JSONObject corts = json.getJSONObject("corts");
            for (int i =0; i<corts.length(); i++) {
                JSONObject jsonCort = corts.getJSONObject(String.valueOf(i));
                Schedule.Cort cort = schedule.addCort(jsonCort.getString("cortName"));
                for(int k=0; k<jsonCort.length()-1; k++){
                    JSONObject jsonMatch = jsonCort.getJSONObject(String.valueOf(k));
                    Match match = Match.parseJSONObject(jsonMatch.getJSONObject("teams"));
                    //match.cort=cort.cortName;
                    match.header= Utils.getStringOrNull(jsonMatch, "startTime");
                    cort.addMatch(match);
                }
            }
            return schedule;

        } catch (JSONException e) {
            Log.e(TAG, "error", e);
        }
        return new Schedule();
    }
}
