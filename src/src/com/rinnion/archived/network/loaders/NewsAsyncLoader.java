package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.database.cursor.TournamentCursor;
import com.rinnion.archived.database.helper.NewsHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.fragment.MainTournamentFragment;
import com.rinnion.archived.fragment.adapter.NewsAdapter;
import com.rinnion.archived.network.handlers.NewsHandler;
import com.rinnion.archived.network.handlers.TournamentHandler;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.network.MyNetwork;

import java.util.Arrays;

/**
 * Created by tretyakov on 08.07.2015.
 */
public class NewsAsyncLoader extends AsyncTaskLoader<ApiObjectCursor> {

    private String tournament_name;
    private String TAG = getClass().getSimpleName();

    public NewsAsyncLoader(Context context, String tournament_name) {
        super(context);
        this.tournament_name = tournament_name;
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
        NewsHelper nh = new NewsHelper(ArchivedApplication.getDatabaseOpenHelper());
        deliverResult(nh.getByParent(new String[]{tournament_name}));
    }

    @Override
    public ApiObjectCursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();

        int[] ints;
        String[] tn = new String[]{tournament_name};
        if (tournament_name == null){
            TournamentHelper th = new TournamentHelper(doh);
            Tournament ladies = th.getByPostName(TournamentHelper.TOURNAMENT_LADIES_TROPHY);
            Tournament open = th.getByPostName(TournamentHelper.TOURNAMENT_OPEN);

            int[] iaLadiesNews = MyNetwork.getIntArray(MyNetwork.queryTournamentNewsList(ladies.id));
            int[] iaOpenNews = MyNetwork.getIntArray(MyNetwork.queryTournamentNewsList(open.id));

            ints = Arrays.copyOf(iaLadiesNews, iaLadiesNews.length + iaOpenNews.length);

            System.arraycopy(iaOpenNews, 0, ints, iaLadiesNews.length, iaOpenNews.length);

            tn = new String[] {ladies.post_name, open.post_name};

        }else{
            TournamentHelper th = new TournamentHelper(doh);
            Tournament tournament = th.getByPostName(tournament_name);
            ints = MyNetwork.getIntArray(MyNetwork.queryTournamentNewsList(tournament.id));
        }

        for (int i : ints) {
            MyNetwork.queryNews(i);
        }

        NewsHelper nh = new NewsHelper(doh);
        NewsCursor newsCursor = nh.getByParent(tn);
        return newsCursor;
    }
}
