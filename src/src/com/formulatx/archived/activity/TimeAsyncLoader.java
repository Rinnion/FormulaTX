package com.formulatx.archived.activity;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.formulatx.archived.utils.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeAsyncLoader extends AsyncTaskLoader<String> {
    public final static String ARGS_TIME_FORMAT = "time_format";
    public final static String TIME_FORMAT_SHORT = "h:mm:ss a";
    public final static String TIME_FORMAT_LONG = "yyyy.MM.dd G 'at' HH:mm:ss";
    final String LOG_TAG = "myLogs";
    final int PAUSE = 10;
    String format;

    public TimeAsyncLoader(Context context, Bundle args) {
        super(context);
        Log.d(LOG_TAG, hashCode() + " create TimeAsyncLoader");
        if (args != null) format = args.getString(ARGS_TIME_FORMAT);
        if (TextUtils.isEmpty(format)) format = TIME_FORMAT_SHORT;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        Log.d(LOG_TAG, hashCode() + " loadInBackground start");
        try {
            TimeUnit.SECONDS.sleep(PAUSE);
        } catch (InterruptedException e) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }
}
