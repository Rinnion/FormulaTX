package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.helper.ApiObjectHelper;
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
    private String TAG = getClass().getCanonicalName();
    private ApiObject mApiObject;

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
        WebView myWebView = (WebView) view.findViewById(R.id.tv_about);

        Bundle args = getArguments();
        String type = args.getString(TYPE);

        ApiObjectHelper th = new ApiObjectHelper(ArchivedApplication.getDatabaseOpenHelper());

        mApiObject = th.getByPostName(type);

        if (mApiObject == null) {
            myWebView.loadData("<html><style>body {color:#FFF;}</style><body align='center'><h2></h2>Нет описания</body></html>", "text/html; charset=UTF-8", null);
        } else {
            if (mApiObject.content.isEmpty()) {
                myWebView.loadData("<html><style>body {color:#FFF;}</style><body align='center'><h2>" + mApiObject.title + "</h2>Нет описания</body></html>", "text/html; charset=UTF-8", null);
            } else {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
                if (cm != null) {
                    NetworkInfo ani = cm.getActiveNetworkInfo();
                    if (ani != null && ani.isConnected()) {
                        myWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                    }else{
                        myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                    }
                }
                else{
                    myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                }

                myWebView.loadData("<html><style>p {color:#FFF;}</style><body>" + mApiObject.content + "</body></html>", "text/html; charset=UTF-8", null);
            }

        }

        myWebView.setBackgroundColor(Color.TRANSPARENT);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            if (mApiObject != null) {
                ab.setTitle(mApiObject.title);
            } else {
                ab.setTitle(getArguments().getString(TYPE));
            }
            ab.setIcon(R.drawable.ic_action_previous_item);
        }

    }
}

