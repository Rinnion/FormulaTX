package com.rinnion.archived.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by alekseev on 22.01.2016.
 */
public class MyDownloadBroadcastReceiver extends BroadcastReceiver {

    private Object obj;

    public Object getObject()
    {
        return obj;
    }

    public MyDownloadBroadcastReceiver(Object obj)
    {
        this.obj=obj;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
