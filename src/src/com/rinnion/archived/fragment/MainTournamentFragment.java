package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
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
    public static final String TOURNAMENT_LADIES_TROPHY = "TOURNAMENT_LADIES_TROPHY";
    public static final String TOURNAMENT_OPEN = "TOURNAMENT_OPEN";
    private String TAG = getClass().getCanonicalName();
    private AdapterView.OnItemClickListener mListener;
    private View mEmpty;

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
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tournament_layout, container, false);
        //mEmpty = view.findViewById(R.id.ml_tv_empty);

        Bundle bundle = getArguments();
        ActionBar ab = getActivity().getActionBar();
        ab.setTitle("St.Petersburg" + bundle.getString(TYPE));

        view.findViewById(R.id.nav_mt_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
            }
        });


        return view;
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

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mListener = listener;
    }

}

