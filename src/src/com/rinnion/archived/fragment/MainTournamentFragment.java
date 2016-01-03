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
import com.rinnion.archived.database.model.ApiObjects.Tournament;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class MainTournamentFragment extends Fragment{

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
        Bundle args = getArguments();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tournament_layout, container, false);
        Bundle bundle = getArguments();
        String type = bundle.getString(TYPE);

        TextView tv = (TextView) view.findViewById(R.id.mtl_tv_name);

        ActionBar ab = getActivity().getActionBar();

        ab.setIcon(R.drawable.ic_action_previous_item);

        if (type.equals(TournamentHelper.TOURNAMENT_LADIES_TROPHY)) {
            view.findViewById(R.id.mtl_ll_background).setBackgroundResource(R.drawable.st_lady_bg);
            tv.setText(R.string.st_lady_title);
            ab.setTitle(R.string.st_lady_title);
        } else {
            view.findViewById(R.id.mtl_ll_background).setBackgroundResource(R.drawable.st_open_bg);
            tv.setText(R.string.st_open_title);
            ab.setTitle(R.string.st_open_title);
        }

        view.findViewById(R.id.nav_mt_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
            }
        });

        view.findViewById(R.id.nav_mt_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewsFragment();
            }
        });

        view.findViewById(R.id.nav_mt_gamers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGamersFragment();
            }
        });

        view.findViewById(R.id.nav_mt_program).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgramFragment();
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
        bundle.putString(NewsListFragment.TOURNAMENT_POST_NAME, getArguments().getString(MainTournamentFragment.TYPE));
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

    private void showGamersFragment() {
        GamerListFragment mlf = new GamerListFragment();
        String type = getArguments().getString(AboutFragment.TYPE);
        Bundle bundle = new Bundle();
        bundle.putString(GamerListFragment.TYPE, type);
        mlf.setArguments(bundle);
        getFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, mlf)
            .addToBackStack(null)
            .commit();
    }

    private void showProgramFragment() {
        ProgramFragment mpf = new ProgramFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AboutFragment.TYPE, getArguments().getString(MainTournamentFragment.TYPE));
        mpf.setArguments(bundle);
        getFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, mpf)
            .addToBackStack(null)
            .commit();
    }

    private void showNewsFragment() {
        NewsListFragment mlf = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NewsListFragment.TOURNAMENT_POST_NAME, getArguments().getString(MainTournamentFragment.TYPE));
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
        bundle.putString(AboutFragment.TYPE, getArguments().getString(MainTournamentFragment.TYPE));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }
}



