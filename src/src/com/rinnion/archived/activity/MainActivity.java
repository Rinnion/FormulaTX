package com.rinnion.archived.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.MessageCursor;
import com.rinnion.archived.fragment.TodayFragment;
import com.rinnion.archived.network.loaders.MessageAsyncLoader;

public class MainActivity extends Activity
        implements LoaderManager.LoaderCallbacks<MessageCursor> {


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


    @Override
    public Loader<MessageCursor> onCreateLoader(int id, Bundle args) {
        return new MessageAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<MessageCursor> loader, MessageCursor data) {

    }

    @Override
    public void onLoaderReset(Loader<MessageCursor> loader) {

    }
}

