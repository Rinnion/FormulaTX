
package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ParserCursor;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.database.model.Parser;
import com.rinnion.archived.network.loaders.ParserAsyncLoader;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.utils.WebViewWithCache;
import org.lorecraft.phparser.SerializedPhpParser;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class TablesFragment extends Fragment {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    private String TAG = getClass().getCanonicalName();
    private WebViewWithCache webView;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: 'home' selected");
                getActivity().getFragmentManager().popBackStack();
                return true;
            default:
                Log.d(TAG, "onOptionsItemSelected: default section");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        webView = (WebViewWithCache) inflater.inflate(R.layout.webview_layout, container, false);
        webView.setBackgroundColor(Color.TRANSPARENT);

        Bundle bundle = getArguments();
        getLoaderManager().initLoader(R.id.tables_loader, bundle, new ParserLoaderCallback());

        return webView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_gallery);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
    }

    private class ParserLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<ParserCursor> {
        @Override
        public Loader<ParserCursor> onCreateLoader(int id, Bundle args) {
            int[] ints = getParsersArrayFromTournament(args);
            return new ParserAsyncLoader(getActivity(), ints);
        }

        private int[] getParsersArrayFromTournament(Bundle args) {
            if (args == null) return null;
            String post_name = args.getString(TOURNAMENT_POST_NAME);
            DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
            TournamentHelper th = new TournamentHelper(doh);
            Tournament tournament = th.getByPostName(post_name);
            ArrayList<Long> intArray = new ArrayList<Long>();
            if (tournament != null) {
                try {
                    String gallery_include = tournament.parsers_include;
                    SerializedPhpParser php = new SerializedPhpParser(gallery_include);
                    Map parse = (Map) php.parse();
                    for (Object item : parse.keySet()) {
                        long l = Long.parseLong(parse.get(item).toString());
                        intArray.add(l);
                    }
                } catch (Exception ignored) {
                    Log.w(TAG, ignored.getMessage());
                }
            }

            int[] ret = new int[intArray.size()];
            for (int i = 0; i < intArray.size(); i++) {
                ret[i] = intArray.get(i).intValue();
            }

            return ret;
        }

        @Override
        public void onLoadFinished(Loader<ParserCursor> loader, ParserCursor data) {
            if (data.getCount() == 0) {
                webView.loadData("<html><body>empty</body></html>", "text/html; charset=UTF-8", null);
                return;
            }
            StringBuilder sb = new StringBuilder();
            while (!data.isAfterLast()){
                Parser item = data.getItem();
                sb.append(item.data);
                data.moveToNext();
            }
            String htmlData = sb.toString();
            String html = "<html>" +
                    "<style>" +
                    "body, table {width:100%;}" +
                    "table {border:1px solid red;}" +
                    "</style><body width='100%'>" + htmlData +"</body></html>";
            String filename = getArguments().getString(TOURNAMENT_POST_NAME);
            webView.loadDataOrCache(getActivity(), "parser-" + filename, htmlData, html, "<html><body>empty</body></html>");
        }

        @Override
        public void onLoaderReset(Loader<ParserCursor> loader) {

        }
    }

}

