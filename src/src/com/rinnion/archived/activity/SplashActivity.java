package com.rinnion.archived.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.rinnion.archived.R;
import com.rinnion.archived.service.DownloadService;

public class SplashActivity extends Activity {


    private final String TAG = getClass().getSimpleName();
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.d(TAG, bundle.toString());
            if (bundle != null) {
                String type = bundle.getString(DownloadService.TYPE);
                Log.d(TAG, "bundle.type: " + type);
                if (type.equals(DownloadService.PROGRESS)) {
                    int anInt = bundle.getInt(DownloadService.PROGRESS);
                    tvProgress.setText(String.valueOf(anInt));
                    pbProgress.setProgress(anInt);
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
                    if (type.equals(DownloadService.ERROR)) {
                        final String error = bundle.getString(DownloadService.ERROR);
                        Log.d(TAG, "error string: " + error);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "create dialog: ");
                                try {
                                    AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
                                    ab.setTitle("Error");
                                    ab.setMessage(error);
                                    ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                                            SplashActivity.this.startActivity(mainIntent);
                                            SplashActivity.this.finish();
                                        }
                                    });
                                    Log.d(TAG, "start dialog: ");
                                    ab.create().show();
                                } catch (Exception ex) {
                                    Log.e(TAG, "error: ", ex);
                                }

                            }
                        });

                    }

                }
            }
    };
    private TextView tvProgress;
    private ProgressBar pbProgress;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        tvProgress = (TextView)findViewById(R.id.sl_tv_progress);
        pbProgress = (ProgressBar)findViewById(R.id.progressBar);
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        unregisterReceiver(receiver);
    }


}

