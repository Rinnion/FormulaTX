package com.formulatx.archived.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import com.formulatx.archived.fragment.MainTournamentFragment;
import com.formulatx.archived.utils.Log;
import com.rinnion.archived.R;

public class MainOpenActivity extends Activity {

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
        MainTournamentFragment mlf = new MainTournamentFragment();
        Bundle extras = getIntent().getExtras();
        Bundle bundle = new Bundle();
        bundle.putString(MainTournamentFragment.TYPE, extras.getString(MainTournamentFragment.TYPE));
        mlf.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .commit();
    }


}

