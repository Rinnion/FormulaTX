package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.activity.MainLadiesActivity;
import com.formulatx.archived.activity.MainOpenActivity;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.AreaCursor;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.R;
import com.formulatx.archived.database.helper.AreaHelper;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.database.model.ApiObjects.Area;
import com.formulatx.archived.utils.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class MainTournamentFragment extends Fragment{

    public static final String TYPE = "TYPE";
    public static final String ABOUT = "ABOUT";
    public static final String NEWS = "NEWS";
    public static final String GAMERS = "GAMERS";
    public static final String PROGRAM = "PROGRAM";
    public static final String SCHEDULE = "SCHEDULE";
    public static final String GRIDS = "GRIDS";
    public static final String LIVESCORE = "LIVESCORE";
    public static final String VIDEO = "VIDEO";
    public static final String FINDWAY = "FINDWAY";

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
                if (getActivity().getFragmentManager().getBackStackEntryCount() == 0){
                    if (getActivity() instanceof MainLadiesActivity || getActivity() instanceof MainOpenActivity){
                        getActivity().finish();
                    }
                }else {
                    getActivity().getFragmentManager().popBackStack();
                }
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
        View view = inflater.inflate(R.layout.main_tournament_list_menu_layout, container, false);
        Bundle bundle = getArguments();
        String type = bundle.getString(TYPE);

        TextView tv = (TextView) view.findViewById(R.id.mtl_tv_name);

        ActionBar ab = getActivity().getActionBar();

        if (ab != null){
            ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            ab.setIcon(R.drawable.ic_action_previous_item);
            ab.setTitle(R.string.string_turnir);
        }

        ImageView iv1 = (ImageView) view.findViewById(R.id.mtl_iv_partner_1);
        ImageView iv2 = (ImageView) view.findViewById(R.id.mtl_iv_partner_2);
        if (type.equals(TournamentHelper.TOURNAMENT_LADIES_TROPHY)){
             iv1.setImageResource(R.drawable.st_pet2);
            iv2.setImageResource(R.drawable.wta_logo);
        }
        if (type.equals(TournamentHelper.TOURNAMENT_OPEN)){
            iv1.setImageResource(R.drawable.st_open);
            iv2.setImageResource(R.drawable.bitmap);
        }



        TournamentHelper th = new TournamentHelper(FormulaTXApplication.getDatabaseOpenHelper());
        Tournament trnmt = th.getByPostName(type);
        if(trnmt!=null) {
            tv.setText(trnmt.title);
        }

        int[] ints = new int[]{R.id.itml_image, R.id.itml_text};
        String[] names = new String[]{"resource", "text", "type"};
        String[] columns = new String[]{"_id", "resource", "text", "type"};
        MatrixCursor mc = new MatrixCursor(columns);
        mc.addRow(new Object[]{1, R.drawable.match_about_icon,  getResources().getString(R.string.string_tournament_about), ABOUT});
        mc.addRow(new Object[]{2, R.drawable.match_news_icon,  getResources().getString(R.string.string_news), NEWS});
        mc.addRow(new Object[]{3, R.drawable.match_players_icon, getResources().getString(R.string.string_gamers), GAMERS});
        mc.addRow(new Object[]{4, R.drawable.match_program_icon,  getResources().getString(R.string.string_tournament_program), PROGRAM});
        mc.addRow(new Object[]{5, R.drawable.match_table_icon,  getResources().getString(R.string.string_tournament_schedule), SCHEDULE});
        mc.addRow(new Object[]{6, R.drawable.match_grids_icon,  getResources().getString(R.string.string_tournament_grids), GRIDS});
        mc.addRow(new Object[]{7, R.drawable.match_livescore_icon,  getResources().getString(R.string.string_tournament_liveScore), LIVESCORE});
        mc.addRow(new Object[]{8, R.drawable.match_media_icon,  getResources().getString(R.string.string_photogallery), VIDEO});
        mc.addRow(new Object[]{9, R.drawable.match_map_icon,  getResources().getString(R.string.string_tournament_findway), FINDWAY});
        ListView listView = (ListView) view.findViewById(R.id.mtl_lv_menu);
        SimpleCursorAdapter sca = new SimpleCursorAdapter(getActivity(), R.layout.item_tournament_menu_layout, mc, names, ints, 0){
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                final ImageView image = (ImageView) view.findViewById(R.id.itml_image);
                final TextView text = (TextView) view.findViewById(R.id.itml_text);

                int resource = cursor.getInt(1);
                String string = cursor.getString(2);
                String type = cursor.getString(3);
                image.setImageResource(resource);
                text.setText(string);
                view.setTag(type);
            }
        };
        listView.setAdapter(sca);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tag = (String) view.getTag();
                if (tag.equals(ABOUT)) showAboutFragment();
                if (tag.equals(NEWS)) showNewsFragment();
                if (tag.equals(GAMERS)) showGamersFragment();
                if (tag.equals(PROGRAM)) showProgramFragment();
                if (tag.equals(SCHEDULE)) showScheduleFragment();
                if (tag.equals(GRIDS)) showGridsFragment();
                if (tag.equals(LIVESCORE)) showLiveScoreFragment();
                if (tag.equals(VIDEO)) showGalleryFragment();
                if (tag.equals(FINDWAY)) showMapFragment();
            }
        });

//        view.findViewById(R.id.nav_mt_about).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAboutFragment();
//            }
//        });
//        view.findViewById(R.id.nav_mt_news).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showNewsFragment();
//            }
//        });
//        view.findViewById(R.id.nav_mt_gamers).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showGamersFragment();
//            }
//        });
//        view.findViewById(R.id.nav_mt_program).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showProgramFragment();
//            }
//        });
//        view.findViewById(R.id.nav_mt_schedule).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showEmptyFragment();
//            }
//        });
//        view.findViewById(R.id.nav_mt_grids).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showEmptyFragment();
//            }
//        });
//        view.findViewById(R.id.nav_mt_livescore).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showEmptyFragment();
//            }
//        });
//        view.findViewById(R.id.nav_mt_video).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showGalleryFragment();
//            }
//        });
//        view.findViewById(R.id.nav_mt_findpath).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showEmptyFragment();
//            }
//        });

        return view;
    }

    private void showLiveScoreFragment() {
        LiveScoreFragment mlf = new LiveScoreFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ScheduleFragment.TOURNAMENT_POST_NAME, getArguments().getString(MainTournamentFragment.TYPE));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showGalleryFragment() {
        GalleryFragment mlf = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GalleryFragment.TOURNAMENT_POST_NAME, getArguments().getString(MainTournamentFragment.TYPE));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showScheduleFragment() {
        ScheduleFragment mlf = new ScheduleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ScheduleFragment.TOURNAMENT_POST_NAME, getArguments().getString(MainTournamentFragment.TYPE));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showGridsFragment() {
        GridsFragment mlf = new GridsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ScheduleFragment.TOURNAMENT_POST_NAME, getArguments().getString(MainTournamentFragment.TYPE));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showEmptyFragment() {
        EmptyFragment mlf = new EmptyFragment();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    private void showGamersFragment() {
        GamerListFragment mlf = new GamerListFragment();
        String type = getArguments().getString(TYPE);
        TournamentHelper th = new TournamentHelper(FormulaTXApplication.getDatabaseOpenHelper());
        Bundle bundle = new Bundle();
        Tournament t = th.getByPostName(type, ApiObject.OBJECT);
        if (t != null) {
            bundle.putLong(GamerListFragment.TOURNAMENT_ID, t.id);
        }else{
            bundle.putLong(GamerListFragment.TOURNAMENT_ID, 0);
        }
        mlf.setArguments(bundle);
        getFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
            .replace(R.id.fragment_container, mlf)
            .addToBackStack(null)
            .commit();
    }

    private void showProgramFragment() {
        ProgramFragment mpf = new ProgramFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProgramFragment.TOURNAMENT_POST_NAME, getArguments().getString(TYPE));
        mpf.setArguments(bundle);
        getFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
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
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
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
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    private void showMapFragment() {
        AreaFragment mlf = new AreaFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AreaFragment.TOURNAMENT_POST_NAME, getArguments().getString(MainTournamentFragment.TYPE));
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }
}



