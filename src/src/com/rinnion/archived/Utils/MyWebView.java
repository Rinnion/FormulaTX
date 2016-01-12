package com.rinnion.archived.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.WebView;

import java.io.File;

/**
 * Created by alekseev on 12.01.2016.
 */
public class MyWebView extends WebView {


    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
        super(context, attrs, defStyle, privateBrowsing);
    }

}
