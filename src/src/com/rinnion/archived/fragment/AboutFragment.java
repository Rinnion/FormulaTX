package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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
import com.rinnion.archived.utils.Network;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
                setWebViewSettings(myWebView);
                loadFromCache(myWebView);
                if (cm != null) {



                    NetworkInfo ani = cm.getActiveNetworkInfo();

                    Log.d(TAG, "Online ani == " + String.valueOf(ani));
                    if (ani != null && ani.isConnected()) {
                        Network network=new Network(){
                            @Override
                            protected void onPostExecute(Boolean aBoolean) {
                                super.onPostExecute(aBoolean);
                                if(aBoolean)
                                    loadDataToCache(myWebView);
                                //else

                            }
                        };

                        network.execute();

                    }



                    /*else{
                        loadFromCache(myWebView);

                    }*/
                }
               /* else{
                    Log.d(TAG, "WORK OFFLINE");
                    myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                    myWebView.loadData("<html><style>p {color:#FFF;}</style><body>" + mApiObject.content + "</body></html>", "text/html; charset=UTF-8", null);
                }*/

                /*loadDataToCache(myWebView);*/

                /*myWebView.setWebViewClient(new MyWebViewClient());
                myWebView.loadUrl("http://yandex.ru");
                myWebView.requestFocus();
                */

            }

        }

        myWebView.setBackgroundColor(Color.TRANSPARENT);
        return view;
    }

    private void loadDataToCache(WebView myWebView) {
        Log.d(TAG, "getActiveNetworkInfo isConnected == true");
        myWebView.stopLoading();

        myWebView.setWebViewClient(new MyWebViewClient(getActivity()) {
           @Override
           public void onPageFinished(WebView view, String url) {
               super.onPageFinished(view, url);

               view.saveWebArchive(Files.getExternalDir("web", mApiObject.type + "." + mApiObject.id + ".mht"));
           }
       });

        myWebView.loadData("<html><style>p {color:#FFF;}</style><body>" + mApiObject.content + "</body></html>", "text/html; charset=UTF-8", null);
    }


    public byte[] getFileAllBytes(String filePath)
    {
        File file = new File(filePath);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    private void loadFromCache(WebView myWebView) {
        Log.d(TAG, "getActiveNetworkInfo isConnected == false or ani == null");
        //myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        Log.d(TAG, "WebView set cache success");
        String filePath= Files.getExternalDir("web", mApiObject.type + "." + mApiObject.id + ".mht");
        File file=new File(filePath);
        if(file.exists()) {
            //myWebView.loadUrl("file:///" + filePath);
            byte []fileBytes=getFileAllBytes(filePath);
            String str = null;
            try {
                str = new String(fileBytes, "UTF-8");
                if(str.startsWith("<?xml"))
                    myWebView.loadData(str, "application/x-webarchive-xml", "UTF-8");
                else
                    myWebView.loadUrl("file:///" + filePath);
                Log.d(TAG,"LoadData size: " + fileBytes.length);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e(TAG,"UnsupportedEncodingException",e);
            }
            /**/

            //Map<String,String> map=new HashMap<String, String>();
            //map.put("content-type","content=\"application/x-webarchive-xml\"");

            //myWebView.loadUrl("file:///" + filePath,map);
        }
        else
            myWebView.loadData("<html><style>p {color:#FFF;}</style><body>" + mApiObject.content + "</body></html>", "text/html; charset=UTF-8", null);
                    myWebView.loadData("<html><style>p {color:#FFF;}</style><body>" + mApiObject.content + "</body></html>", "text/html; charset=UTF-8", null);
    }

    private void setWebViewSettings(WebView myWebView) {
        myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        WebSettings mWebViewSettings = myWebView.getSettings();
        mWebViewSettings.setJavaScriptEnabled(true);
        mWebViewSettings.setAllowFileAccess(true);
        mWebViewSettings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT < 18)
            mWebViewSettings.setAppCacheMaxSize(8 * 1024 * 1024);
        mWebViewSettings.setLoadsImagesAutomatically(true);
        mWebViewSettings.setAppCachePath(Files.getCacheDir());



        myWebView.setWebViewClient(new MyWebViewClient(getActivity()));

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

