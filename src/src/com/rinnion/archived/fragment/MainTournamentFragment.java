package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.database.model.ApiObjects.ApiObjectTypes;
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
        View view = inflater.inflate(R.layout.main_tournament_list_menu_layout, container, false);
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

        int[] ints = new int[]{R.id.itml_image, R.id.itml_text};
        String[] names = new String[]{"resource", "text", "type"};
        String[] columns = new String[]{"_id", "resource", "text", "type"};
        MatrixCursor mc = new MatrixCursor(columns);
        mc.addRow(new Object[]{1, android.R.drawable.stat_sys_warning,  getResources().getString(R.string.string_tournament_about), ABOUT});
        mc.addRow(new Object[]{2, R.drawable.menu_news_icon,  getResources().getString(R.string.string_news), NEWS});
        mc.addRow(new Object[]{2, R.drawable.menu_other_mutch_icon, getResources().getString(R.string.string_gamers), GAMERS});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_tournament_program), PROGRAM});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_tournament_schedule), SCHEDULE});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_tournament_grids), GRIDS});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_tournament_liveScore), LIVESCORE});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_photogallery), VIDEO});
        mc.addRow(new Object[]{2, R.drawable.ic_action_person,  getResources().getString(R.string.string_tournament_findway), FINDWAY});
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
                if (tag.equals(SCHEDULE)) showEmptyFragment();
                if (tag.equals(GRIDS)) showEmptyFragment();
                if (tag.equals(LIVESCORE)) showEmptyFragment();
                if (tag.equals(VIDEO)) showGalleryFragment();
                if (tag.equals(FINDWAY)) showEmptyFragment();
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

    public void showGalleryFragment() {
        GalleryFragment mlf = new GalleryFragment();
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
        TournamentHelper th = new TournamentHelper(ArchivedApplication.getDatabaseOpenHelper());
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
        bundle.putString(AboutFragment.TYPE, getArguments().getString(TYPE));
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
}



