package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.utils.Log;
import com.rinnion.archived.R;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class LiveScoreOtherFragment extends Fragment {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    private String TAG = getClass().getCanonicalName();
    private WebView mTextViewAbout;

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
        View view = inflater.inflate(R.layout.about_layout, container, false);
        mTextViewAbout = (WebView) view.findViewById(R.id.tv_about);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.string_tournament_liveScore);
        ab.setIcon(R.drawable.ic_action_previous_item);

        mTextViewAbout.loadData("<html><style>body {color:#FFF;}</style><body align='center'><h2>"+ FormulaTXApplication.getResourceString(R.string.string_no_matches)+"</h2></body></html>", "text/html; charset=UTF-8", null);
        mTextViewAbout.setBackgroundColor(Color.TRANSPARENT);

        return view;
    }

}

