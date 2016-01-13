package com.rinnion.archived.utils;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * Created by Eloy on 12.01.2016.
 */
public class Network extends AsyncTask<Void,Void,Boolean> {
    public static final String TAG="Network";




    public static Boolean isOnlineWithDataConnection() {

        try {

            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is
            // established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 3000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpResponse response = httpclient.execute(new HttpGet(
                    "https://www.google.com"));
            response.getStatusLine().getStatusCode();
            return true;
        } catch (Exception e) {


            return false;

        }

    }




    @Override
    protected Boolean doInBackground(Void[] params) {
        return isOnlineWithDataConnection();
    }
}
