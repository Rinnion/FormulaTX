package com.formulatx.archived.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.formulatx.archived.database.model.ApiObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by alekseev on 18.01.2016.
 */
public class WebViewWithCache extends WebView {

    private static final String TAG = "WebViewWithCache";

    private Activity activity;
    private String mTempFileName;
    private String htmlData;
    private String htmlDataEmpty;



    public WebViewWithCache(Context context) {
        super(context);

        //context.getC

    }

    public WebViewWithCache(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewWithCache(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public WebViewWithCache(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
        super(context, attrs, defStyle, privateBrowsing);
    }

    private boolean loadFromCache() {
        Log.d(TAG, "getActiveNetworkInfo isConnected == false or ani == null");
        //myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        Log.d(TAG, "WebView set cache success");

        WebView myWebView = this;

        String filePath = Files.getExternalDir("web", mTempFileName + ".mht");
        File file = new File(filePath);
        if (file.exists()) {
            //myWebView.loadUrl("file:///" + filePath);
            byte[] fileBytes = Files.getFileAllBytes(filePath);
            String str = null;
            try {
                str = new String(fileBytes, "UTF-8");
                if (str.startsWith("<?xml"))
                    myWebView.loadData(str, "application/x-webarchive-xml", "UTF-8");
                else
                    myWebView.loadUrl("file:///" + filePath);
                Log.d(TAG, "LoadData size: " + fileBytes.length);
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e(TAG, "UnsupportedEncodingException", e);
                return false;
            }

        } else {
            return false;
        }

    }


    public void saveWebArchive() {
        super.saveWebArchive(Files.getExternalDir("web", mTempFileName + ".mht"));
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(client);
    }

    private void loadDataAndSaveToCache() {
        Log.d(TAG, "getActiveNetworkInfo isConnected == true");
        WebView myWebView = this;
        myWebView.stopLoading();


        myWebView.setWebViewClient(new MyWebViewClient(activity) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                ((WebViewWithCache) view).saveWebArchive();
            }
        });

        myWebView.loadData(htmlData, "text/html; charset=UTF-8", null);
    }


    private void setDefaultSettings() {
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        WebSettings mWebViewSettings = this.getSettings();
        mWebViewSettings.setJavaScriptEnabled(true);
        mWebViewSettings.setAllowFileAccess(true);
        mWebViewSettings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT < 18)
            mWebViewSettings.setAppCacheMaxSize(8 * 1024 * 1024);
        mWebViewSettings.setLoadsImagesAutomatically(true);
        mWebViewSettings.setAppCachePath(Files.getCacheDir());
        mWebViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebViewSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        this.setWebViewClient(new MyWebViewClient(activity));
    }


    public void loadDataOrCache(Activity activity, ApiObject apiObject, String data, String emptyData) {
        String tempFileName = apiObject.type + "." + apiObject.id;
        loadDataOrCache(activity, tempFileName, apiObject.content, data, emptyData);
    }


    public void loadDataOrCache(Activity activity, String tempFileName, String content, final String data, String emptyData) {
        setDefaultSettings();

        this.activity= activity;
        this.mTempFileName= tempFileName;
        this.htmlData=data;
        this.htmlDataEmpty=emptyData;
        if (content == null) {
            this.loadData(emptyData, "text/html; charset=UTF-8", "UTF-8");
        } else {
            if (content.isEmpty()) {
                this.loadData(emptyData, "text/html; charset=UTF-8", "UTF-8");
            } else {
                ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);



                if (cm != null) {


                    NetworkInfo ani = cm.getActiveNetworkInfo();

                    Log.d(TAG, "Online ani == " + String.valueOf(ani));
                    if (ani != null && ani.isConnected()) {
                        final Network network = new Network() {

                            @Override
                            protected void onPostExecute(Boolean aBoolean) {
                                super.onPostExecute(aBoolean);

                                //
                                if (aBoolean) {
                                    loadDataAndSaveToCache();
                                }
                                else {
                                    if (!loadFromCache())
                                        loadData(data, "text/html; charset=UTF-8", "UTF-8");
                                }

                            }
                        };

                        network.execute();

                    }
                    else
                    {
                        if (!loadFromCache())
                            loadData(data, "text/html; charset=UTF-8", "UTF-8");
                    }


                }
                else
                {
                    if (!loadFromCache())
                        loadData(data, "text/html; charset=UTF-8", "UTF-8");
                }


            }
        }
    }
}
