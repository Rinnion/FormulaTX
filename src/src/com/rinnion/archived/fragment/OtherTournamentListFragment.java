package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.rinnion.archived.utils.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.TournamentCursor;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.squareup.picasso.Picasso;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class OtherTournamentListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;
    private TextView mTextView;
    private AdapterView.OnItemClickListener mListener;
    private View mEmpty;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        String[] from = new String[]{
                TournamentHelper.COLUMN_TITLE,
                TournamentHelper.COLUMN_THUMB
        };

        int[] to = new int[]{
                R.id.itl_tv_caption,
                R.id.itl_iv_thumbs
        };

        TournamentHelper th = new TournamentHelper(ArchivedApplication.getDatabaseOpenHelper());
        TournamentCursor cursor = th.getAllOther();

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_tournament_layout, cursor, from, to, 0) {
            @Override
            public void setViewImage(ImageView v, String value) {

                v.setImageResource(R.drawable.logo_splash_screen);
                /*Log.d(TAG, String.valueOf(value));

                if (value == null || value.isEmpty()) super.setViewImage(v,value);
                Picasso.with(getActivity())
                        .load(value)
                        .resize(75,75).centerCrop()
                        .placeholder(R.drawable.logo_splash_screen)
                         .error(R.drawable.logo_splash_screen)
                        .into(v);

                */
            }
        };

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, null, false);
        ListView mListView = (ListView) view;
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return view;
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

    private void showOtherTournamentFragment(String post_name) {
        OtherTournamentFragment mlf = new OtherTournamentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(OtherTournamentFragment.TYPE, post_name);
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TournamentHelper th = new TournamentHelper(ArchivedApplication.getDatabaseOpenHelper());
        Tournament tournament = th.get(id);
        showOtherTournamentFragment(tournament.post_name);
    }

    @Override
    public void onStart() {
        super.onStart();

        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_other_tounaments);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
    }

}

