package com.rinnion.archived.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.MessageCursor;
import com.rinnion.archived.fragment.*;
import com.rinnion.archived.network.loaders.MessageAsyncLoader;

public class MainActivity extends Activity
        implements LoaderManager.LoaderCallbacks<MessageCursor> {


    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.order_list_drawer);


        findViewById(R.id.nav_today).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTodayFragment();
                mDrawerLayout.closeDrawers();
            }
        });

        findViewById(R.id.nav_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });

        findViewById(R.id.nav_StPetersburgLadiesTrophy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainTournmentFragment(MainTournamentFragment.TOURNAMENT_LADIES_TROPHY);
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_StPetersburgOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainTournmentFragment(MainTournamentFragment.TOURNAMENT_OPEN);
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_other_tournaments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_photogallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_social_networks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_tickets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_volvo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_event_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_radio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });
        findViewById(R.id.nav_catalog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
                mDrawerLayout.closeDrawers();
            }
        });

        ActionBar ab = getActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);


        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            showTodayFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: 'home' selected");
                if (!mDrawerLayout.isDrawerOpen(Gravity.START))
                    mDrawerLayout.openDrawer(Gravity.START);
                else
                    mDrawerLayout.closeDrawer(Gravity.START);
                break;
            default:
                Log.d(TAG, "onOptionsItemSelected: default section");
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAboutFragment() {
        AboutFragment mlf = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AboutFragment.TYPE, AboutFragment.TYPE_COMPANY);
        bundle.putString(AboutFragment.ENTITY, null);
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showTodayFragment() {
        TodayFragment mlf = new TodayFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .commit();
    }

    public void showMainTournmentFragment(String type) {
        MainTournamentFragment mlf = new MainTournamentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MainTournamentFragment.TYPE, type);
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
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

