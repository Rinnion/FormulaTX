package com.rinnion.archived.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.rinnion.archived.ArchivedApplication;

/**
 * Created by alekseev on 12.01.2016.
 */
public class MyWebViewClient extends WebViewClient {
    private static final String TAG="MyWebViewClient";
    private Activity mActivity;

    public MyWebViewClient(Activity activity) {
        mActivity=activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mActivity.startActivity(i);
        }
        catch (Exception _ex)
        {
            Log.e(TAG,"MyWebViewClient Exception: " + url,_ex);
        }
        return true;
    }

}
