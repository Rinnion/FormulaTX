package com.rinnion.archived.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.rinnion.archived.R;
import com.rinnion.archived.service.DownloadService;

public class SplashActivity extends Activity {


    private final String TAG = getClass().getSimpleName();
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.getString(DownloadService.TYPE).equals(DownloadService.PROGRESS)) {
                    int anInt = bundle.getInt(DownloadService.PROGRESS);
                    String mosInt = bundle.getString(DownloadService.MESSAGE);
                    Toast.makeText(SplashActivity.this,
                            "Progress: " + ((mosInt==null)? "": "("+mosInt+")"),
                            Toast.LENGTH_SHORT).show();
                    if (anInt == 100) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                                SplashActivity.this.startActivity(mainIntent);
                                SplashActivity.this.finish();
                            }
                        });
                    }
                }
            }
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        unregisterReceiver(receiver);
        super.onResume();
    }


}

