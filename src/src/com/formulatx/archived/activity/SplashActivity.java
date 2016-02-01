package com.formulatx.archived.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Settings;
import com.formulatx.archived.service.DownloadService;
import com.formulatx.archived.utils.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.rinnion.archived.R;

public class SplashActivity extends Activity {


    private final String TAG = getClass().getSimpleName();
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Receive data");
            Bundle bundle = intent.getExtras();
            Log.d(TAG, String.valueOf(bundle));
            if (bundle != null) {
                String type = bundle.getString(Settings.LOADING_TYPE);
                if (type == null) return;
                Log.d(TAG, "bundle.type: " + type);

                if (type.equals(Settings.LOADING_PROGRESS)) {
                    int anInt = bundle.getInt(Settings.LOADING_PROGRESS);
                    tvProgress.setText(String.valueOf(anInt));
                    pbProgress.setProgress(anInt);
                    Log.d(TAG, Settings.LOADING_PROGRESS + ": " + anInt);
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

                if (type.equals(Settings.LOADING_ERROR)) {
                    //noinspection ConstantConditions
                    final String error = bundle.getString((Settings.DEBUG) ? Settings.LOADING_CUSTOM_MESSAGE : Settings.LOADING_ERROR);
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
                                AlertDialog dialog = ab.create();
                                dialog.show();
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
        TextView version = (TextView) findViewById(R.id.splash_tv_version);
        version.setText(Settings.VERSION);

        tvProgress = (TextView)findViewById(R.id.sl_tv_progress);
        pbProgress = (ProgressBar)findViewById(R.id.progressBar);

        FormulaTXApplication.setParameter(Settings.LOADING_TYPE, Settings.LOADING_PROGRESS);
        FormulaTXApplication.setParameter(Settings.LOADING_PROGRESS, 0);
        FormulaTXApplication.setParameter(Settings.LOADING_CUSTOM_MESSAGE, "");

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION));

        String type = FormulaTXApplication.getStringParameter(Settings.LOADING_TYPE);
        int progress = FormulaTXApplication.getIntParameter(Settings.LOADING_PROGRESS, 0);
        String error = FormulaTXApplication.getStringParameter(Settings.LOADING_ERROR);
        String custom_message = FormulaTXApplication.getStringParameter(Settings.LOADING_CUSTOM_MESSAGE);

        Intent intent = new Intent(DownloadService.NOTIFICATION);
        intent.putExtra(Settings.LOADING_TYPE, type);
        intent.putExtra(Settings.LOADING_PROGRESS, progress);
        intent.putExtra(Settings.LOADING_ERROR, error);
        intent.putExtra(Settings.LOADING_CUSTOM_MESSAGE, custom_message);
        sendBroadcast(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        unregisterReceiver(receiver);
    }


}

