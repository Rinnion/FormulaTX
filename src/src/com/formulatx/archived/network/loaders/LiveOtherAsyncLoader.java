package com.formulatx.archived.network.loaders;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.os.Bundle;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.fragment.MatchCursor;
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
public class LiveOtherAsyncLoader extends AsyncTaskLoader<MatchCursor> {
    private static final String TAG = "LiveAsyncLoader";
    private final String string;

    public LiveOtherAsyncLoader(Activity activity, String string) {
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
    public MatchCursor loadInBackground() {
        return unWrapSchedule(MyNetwork.getLiveOtherScore());
    }

    private MatchCursor unWrapSchedule(Bundle ladiesSchedule) {

        if (ladiesSchedule == null) return new MatchCursor();
        Bundle http = ladiesSchedule.getBundle(HttpRequester.RESULT_HTTP);
        if (http == null) return new MatchCursor();
        Bundle parse = http.getBundle(HttpRequester.RESULT_HTTP_PARSE);
        if (parse == null) return new MatchCursor();
        String string = parse.getString(JSONObjectHandler.VALUE);
        try {
            JSONObject json = new JSONObject(string);
            MatchCursor mc = new MatchCursor();
            for (int i =0; i<json.length(); i++) {
                JSONObject jsonScore = json.getJSONObject(String.valueOf(i));
                Match match = Match.parseJSONObject(jsonScore);
                match.cort = jsonScore.getString("courtName");
                mc.add("", i, match.getJSONObject().toString(), "", "");
            }
            return mc;
        } catch (JSONException e) {
            Log.e(TAG, "Error", e);
        }
        return new MatchCursor();
    }

}
