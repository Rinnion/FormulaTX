package com.rinnion.archived.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.rinnion.archived.activity.MainActivity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Eloy on 24.12.2015.
 */
public class TestRest extends AsyncTask<String,Void,Void>{

    final HttpClient httpClient=new DefaultHttpClient();
    String content;
    String error;

    ProgressDialog progressDialog;
    String data;

    public TestRest(Context context) {
        //progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        URL url;
        try {
            url=new URL(params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
