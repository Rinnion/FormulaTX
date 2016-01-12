package com.rinnion.archived.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by alekseev on 12.01.2016.
 */
public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);

        return true;
    }

}
