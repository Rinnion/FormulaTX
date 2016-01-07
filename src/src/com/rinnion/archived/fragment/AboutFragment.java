package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.widget.TextView;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObject;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class AboutFragment extends Fragment  {

    public static final String TYPE = "TYPE";
    public static final String ENTITY = "ENTITY";
    public static final String TYPE_COMPANY = "COMPANY";
    private String TAG = getClass().getCanonicalName();

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
        WebView mTextViewAbout = (WebView) view.findViewById(R.id.tv_about);

        Bundle args = getArguments();
        String type = args.getString(TYPE);

        TournamentHelper th = new TournamentHelper(ArchivedApplication.getDatabaseOpenHelper());

        ApiObject apiObject = th.getByPostName(type);

        if (apiObject.content.isEmpty()){
            mTextViewAbout.loadData("<html><style>body {color:#FFF;}</style><body align='center'><h2>О турнире</h2>Нет описания</body></html>", "text/html; charset=UTF-8", null);
        }else{
            mTextViewAbout.loadData("<html><style>p {color:#FFF;}</style><body>"+apiObject.content+"</body></html>", "text/html; charset=UTF-8", null);
        }

        mTextViewAbout.setBackgroundColor(Color.TRANSPARENT);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_about);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }

    }
}

