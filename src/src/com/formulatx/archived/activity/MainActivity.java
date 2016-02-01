package com.formulatx.archived.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import com.formulatx.archived.utils.Log;
import com.rinnion.archived.R;
import com.formulatx.archived.fragment.TodayFragment;

public class MainActivity extends Activity {


    private static final String TAG = "MainActivity";

    /**
     * Called when the activity is first created.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);

        ActionBar ab = getActionBar();
        ab.setHomeButtonEnabled(true);

        showTodayFragment();
    }

    public void showTodayFragment() {
        TodayFragment mlf = new TodayFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .commit();
    }


}

