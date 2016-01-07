package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
import com.rinnion.archived.database.model.ApiObjects.Tournament;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class OtherTournamentFragment extends Fragment {

    public static final String TYPE = "TYPE";
    private String TAG = getClass().getCanonicalName();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: 'home' selected");
                getActivity().getFragmentManager().popBackStack();
                return true;
            default:
                Log.d(TAG, "onOptionsItemSelected: default section");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_tournament_layout, container, false);
        TextView tv = (TextView)view.findViewById(R.id.mtl_tv_name);

        TournamentHelper th = new TournamentHelper(ArchivedApplication.getDatabaseOpenHelper());
        Tournament tournament = th.getByPostName(getArguments().getString(TYPE), ApiObjectTypes.EN_Object);
        tv.setText(tournament.title);

        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(String.valueOf(tournament.title));
            ab.setIcon(R.drawable.ic_action_previous_item);
        }

        view.findViewById(R.id.nav_mt_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
            }
        });

        view.findViewById(R.id.nav_mt_schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmptyFragment();
            }
        });

        view.findViewById(R.id.nav_mt_grids).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmptyFragment();
            }
        });

        view.findViewById(R.id.nav_mt_livescore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmptyFragment();
            }
        });

        view.findViewById(R.id.nav_mt_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGalleryFragment();
            }
        });

        view.findViewById(R.id.nav_mt_findpath).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmptyFragment();
            }
        });

        return view;
    }

    public void showGalleryFragment() {
        GalleryFragment mlf = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GalleryFragment.TOURNAMENT_POST_NAME, getArguments().getString(MainTournamentFragment.TYPE));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    private void showAboutFragment() {
        AboutFragment mlf = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AboutFragment.TYPE, getArguments().getString(TYPE));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showEmptyFragment() {
        EmptyFragment mlf = new EmptyFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

}

