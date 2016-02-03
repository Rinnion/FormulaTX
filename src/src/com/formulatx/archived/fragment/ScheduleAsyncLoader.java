package com.formulatx.archived.fragment;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.MatrixCursor;
import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.formulatx.archived.network.HttpRequester;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.network.handlers.JSONObjectHandler;
import com.formulatx.archived.network.handlers.TournamentHandler;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 03.02.2016.
 */
public class ScheduleAsyncLoader extends AsyncTaskLoader<Schedule> {
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
            Schedule scheculde = unWrapSchedule(MyNetwork.getLadiesSchedule());
            return scheculde;
        }
        if (string.equals(TournamentHelper.TOURNAMENT_LADIES_TROPHY)){
            MyNetwork.getOpenSchedule();
            return new Schedule();
        }
        return new Schedule();
    }

    private Schedule unWrapSchedule(Bundle ladiesSchedule) {
        if (ladiesSchedule == null) return null;
        Bundle http = ladiesSchedule.getBundle(HttpRequester.RESULT_HTTP);
        if (http == null) return null;
        Bundle parse = http.getBundle(HttpRequester.RESULT_HTTP_PARSE);
        if (parse == null) return null;
        String string = parse.getString(JSONObjectHandler.VALUE);
        try {
            JSONObject json = new JSONObject(string);
            Schedule schedule = new Schedule();
            //schedule.
            //json.getJSONArray("corts").getJSONObject(0).getJSONObject("1").getJSONObject("teams").getJSONObject("team1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

                 return null;
    }
}
