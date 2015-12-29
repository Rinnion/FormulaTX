package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import com.rinnion.archived.R;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class MainTournamentFragment extends Fragment{

    public static final String TYPE = "TYPE";
    public static final String TOURNAMENT_LADIES_TROPHY = "LADIES TROPHY";
    public static final String TOURNAMENT_OPEN = "OPEN";
    private String TAG = getClass().getCanonicalName();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_message, menu);
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
        View view = inflater.inflate(R.layout.main_tournament_layout, container, false);

        Bundle bundle = getArguments();

        TextView tv = (TextView) view.findViewById(R.id.mtl_tv_name);
        tv.setText("ST.PETERBURG " + bundle.getString(TYPE));

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle("St.Petersburg" + bundle.getString(TYPE));
        ab.setIcon(R.drawable.ic_action_previous_item);

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
                showEmptyFragment();
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
        Bundle bundle = new Bundle();
        bundle.putString(AboutFragment.TYPE, getArguments().getString("TYPE"));
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
        bundle.putString(AboutFragment.TYPE, getArguments().getString("TYPE"));
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
        bundle.putString(AboutFragment.TYPE, getArguments().getString("TYPE"));
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
        bundle.putString(AboutFragment.TYPE, AboutFragment.TYPE_TOURNAMENT_MAIN);
        bundle.putString(AboutFragment.ENTITY, getArguments().getString("TYPE"));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }
}



