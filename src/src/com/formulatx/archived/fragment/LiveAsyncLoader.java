package com.formulatx.archived.fragment;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.os.Bundle;
import android.os.Parcelable;
import com.formulatx.archived.Utils;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.network.HttpRequester;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.network.handlers.JSONArrayHandler;
import com.formulatx.archived.network.handlers.JSONObjectHandler;
import com.formulatx.archived.parsers.Match;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 03.02.2016.
 */
public class LiveAsyncLoader extends AsyncTaskLoader<MatchCursor> {
    private final String string;

    public LiveAsyncLoader(Activity activity, String string) {
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
        if (string.equals(TournamentHelper.TOURNAMENT_LADIES_TROPHY)) {
            return unWrapSchedule(MyNetwork.getLadiesLiveScore());
        }
        if (string.equals(TournamentHelper.TOURNAMENT_OPEN)) {
            return unWrapSchedule(MyNetwork.getOpenLiveScore());
        }
        return new MatchCursor();
    }

    private MatchCursor unWrapSchedule(Bundle ladiesSchedule) {

        if (ladiesSchedule == null) return new MatchCursor();
        Bundle http = ladiesSchedule.getBundle(HttpRequester.RESULT_HTTP);
        if (http == null) return new MatchCursor();
        Bundle parse = http.getBundle(HttpRequester.RESULT_HTTP_PARSE);
        if (parse == null) return new MatchCursor();
        Parcelable[] array = parse.getParcelableArray(JSONArrayHandler.EXTRA_ARRAY);
        if (array == null) return new MatchCursor();
        try {
            MatchCursor mc = new MatchCursor();
            for (int i = 0; i < array.length; i++) {
                if (isAbandoned()) return new MatchCursor();
                String string = ((Bundle) array[i]).getString(JSONObjectHandler.VALUE);
                Match match = Match.parseJSONObject(new JSONObject(string));
                mc.add("", i, match.getJSONObject().toString(), "", "");
            }
            return mc;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MatchCursor();
    }

}
