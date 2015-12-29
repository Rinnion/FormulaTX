package com.rinnion.archived.network;

import android.os.Bundle;
import android.util.Log;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 29.01.14
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
public class HttpRequester {
    public static final String STATUS_CODE = "STATUS_CODE";
    public static final String RESULT = "RESULT";
    public static final String RESULT_HTTP = "HTTP";
    public static final String RESULT_HTTP_PARSE = "HTTP_PARSE";
    public static final String RESULT_EXCEPTION = "EXCEPTION";
    private String mIdentity;
    private HttpUriRequest mRequest;
    private IResponseHandler mHandler;
    private String TAG = getClass().getSimpleName();

    protected HttpRequester(String identity, HttpUriRequest request, IResponseHandler handler) {
        mIdentity = identity;
        mRequest = request;
        mHandler = handler;
        if (mHandler == null) mHandler = new EmptyResponseHandler();
    }

    public Bundle execute() {
        Bundle result = new Bundle();
        try {
            Log.d(TAG, mIdentity + ": " + mRequest.getMethod() + " " + mRequest.getURI().toString());

            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 15000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 20000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            //TODO should work only on some requests
            HttpClient client = new DefaultHttpClient();

            long sec = Calendar.getInstance().getTimeInMillis();
            HttpResponse response = client.execute(mRequest);
            sec = (Calendar.getInstance().getTimeInMillis() - sec);

            int statusCode = response.getStatusLine().getStatusCode();
            Log.d(TAG, mIdentity + ": " + Integer.toString(statusCode) + " in " + sec + " ms");
            Bundle bundle = new Bundle();
            bundle.putInt(STATUS_CODE, statusCode);

            result.putSerializable(RESULT, RESULT_HTTP);
            result.putBundle(RESULT_HTTP, bundle);
            bundle.putBundle(RESULT_HTTP_PARSE, mHandler.Handle(response));

            Log.d(TAG, mIdentity + ": success");
        } catch (Exception e) {
            Log.w(TAG, mIdentity + ": Exception: " + e.getMessage());
            Log.d(TAG, mIdentity, e);
            result.putSerializable(RESULT, RESULT_EXCEPTION);
            result.putSerializable(RESULT_EXCEPTION, e);
        }

        return result;
    }

    public static class Builder {
        private String mIdentity;
        private HttpUriRequest mRequest;
        private IResponseHandler mHandler;
        private String mCredentials;
        private StringEntity mEntity;

        public Builder setName(String name) {
            this.mIdentity = name;
            return this;
        }

        public Builder setDeleteRequest(String url) {
            this.mRequest = new HttpDelete(url);
            return this;
        }

        public Builder setPostRequest(String url) {
            this.mRequest = new HttpPost(url);
            return this;
        }

        public Builder setGetRequest(String url) {
            this.mRequest = new HttpGet(url);
            return this;
        }

        public Builder setPutRequest(String url) {
            this.mRequest = new HttpPut(url);
            return this;
        }

        public Builder setContent(JSONObject content) throws UnsupportedEncodingException {
            String entity = content.toString();
            this.mEntity = new StringEntity(entity, "UTF-8");
            return this;
        }

        public Builder setContent(String content) throws UnsupportedEncodingException {
            this.mEntity = new StringEntity(content, "UTF-8");
            return this;
        }

        public Builder setContent(JSONArray content) throws UnsupportedEncodingException {
            String entity = content.toString();
            this.mEntity = new StringEntity(entity, "UTF-8");
            return this;
        }

        public Builder setCredentials(String credentials) {
            this.mCredentials = credentials;
            return this;
        }

        public Builder setHandler(IResponseHandler handler) {
            this.mHandler = handler;
            if (handler == null) this.mHandler = new EmptyResponseHandler();
            return this;
        }

        public HttpRequester create() {
            if (this.mCredentials != null && !this.mCredentials.isEmpty())
                this.mRequest.addHeader("Authorization", "Basic " + this.mCredentials);
            if (this.mEntity != null && this.mRequest instanceof HttpEntityEnclosingRequest) {
                this.mRequest.addHeader("content-type", "application/json");
                ((HttpEntityEnclosingRequest) this.mRequest).setEntity(this.mEntity);
            }

            return new HttpRequester(this.mIdentity, this.mRequest, this.mHandler);
        }
    }
}
