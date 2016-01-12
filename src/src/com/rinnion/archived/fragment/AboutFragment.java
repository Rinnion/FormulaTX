package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.utils.Files;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.utils.MyWebViewClient;

import java.io.File;

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
        final WebView myWebView = (WebView) view.findViewById(R.id.tv_about);

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
                        Log.d(TAG, "getActiveNetworkInfo isConnected == true");

                        myWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 60);
                        myWebView.getSettings().setAppCachePath(Files.getCacheDir());
                        myWebView.getSettings().setAllowFileAccess(true);
                        myWebView.getSettings().setAppCacheEnabled(true);
                        myWebView.getSettings().setDomStorageEnabled(true);
                        myWebView.getSettings().setJavaScriptEnabled(true);
                        /*myWebView.getSettings().setLoadsImagesAutomatically(true);*/
                        myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                        Log.d(TAG, "WebView set cache success");

                        myWebView.setWebViewClient(new MyWebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);

                                view.saveWebArchive(Files.getExternalDir("web", mApiObject.type + "." + mApiObject.id +  ".mht"));
                            }
                        });

                        myWebView.loadData("<html><style>p {color:#FFF;}</style><body>" + mApiObject.content + "</body></html>", "text/html; charset=UTF-8", null);

                    }else{
                        Log.d(TAG,"getActiveNetworkInfo isConnected == false or ani == null");

                        myWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 60);
                        myWebView.getSettings().setAppCachePath(Files.getCacheDir());
                        myWebView.getSettings().setAllowFileAccess(true);
                        myWebView.getSettings().setAppCacheEnabled(true);
                        myWebView.getSettings().setDomStorageEnabled(true);
                        myWebView.getSettings().setJavaScriptEnabled(true);
                        /*myWebView.getSettings().setLoadsImagesAutomatically(true);*/
                        myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
                        Log.d(TAG, "WebView set cache success");

                        myWebView.loadUrl("file:///" + Files.getExternalDir("web", mApiObject.type + "." + mApiObject.id +  ".mht"));

                    }
                }
                else{
                    myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                }



                /*myWebView.setWebViewClient(new MyWebViewClient());
                myWebView.loadUrl("http://yandex.ru");
                myWebView.requestFocus();
                */

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

