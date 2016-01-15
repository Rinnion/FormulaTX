package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.rinnion.archived.R;
import com.rinnion.archived.Settings;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.utils.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class NavigationFragment extends Fragment {

    private String TAG = getClass().getCanonicalName();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_layout, container, false);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.string_navigation);
        ab.setIcon(R.drawable.ic_action_cancel);

        view.findViewById(R.id.nav_today).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTodayFragment();
            }
        });
        view.findViewById(R.id.nav_mt_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewsFragment();
            }
        });
        view.findViewById(R.id.nav_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
            }
        });
        view.findViewById(R.id.nav_StPetersburgLadiesTrophy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainTournmentFragment(TournamentHelper.TOURNAMENT_LADIES_TROPHY);
            }
        });
        view.findViewById(R.id.nav_StPetersburgOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainTournmentFragment(TournamentHelper.TOURNAMENT_OPEN);
            }
        });
        view.findViewById(R.id.nav_other_tournaments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherTournmentFragment();
            }
        });
        view.findViewById(R.id.nav_photogallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmptyFragment();
            }
        });
        view.findViewById(R.id.nav_social_networks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialFragment();
            }
        });

        view.findViewById(R.id.nav_tickets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardFragment();
            }
        });
        view.findViewById(R.id.nav_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShopFragment();
            }
        });
        view.findViewById(R.id.nav_radio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmptyFragment();
            }
        });
        view.findViewById(R.id.nav_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFeedBackFragment();
            }
        });

        return view;
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

    private void showNewsFragment() {
        NewsListFragment mlf = new NewsListFragment();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    private void showCardFragment() {
        CardFragment mlf = new CardFragment();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    private void showShopFragment() {
        ShopFragment mlf = new ShopFragment();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showGalleryFragment() {
        GalleryFragment mlf = new GalleryFragment();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showFeedBackFragment() {
        FeedBackFragment mlf = new FeedBackFragment();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showAboutFragment() {
        AboutFragment mlf = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AboutFragment.TYPE, Settings.ABOUT_API_OBJECT_ALIAS);
        mlf.setArguments(bundle);
        getFragmentManager().popBackStack();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showSocialFragment() {
        SocialFragment mlf = new SocialFragment();
        getFragmentManager().popBackStack();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showOtherTournmentFragment() {
        OtherTournamentListFragment mlf = new OtherTournamentListFragment();
        getFragmentManager().popBackStack();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    public void showTodayFragment() {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getFragmentManager().popBackStack();
    }

    public void showMainTournmentFragment(String type) {
        MainTournamentFragment mlf = new MainTournamentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MainTournamentFragment.TYPE, type);
        mlf.setArguments(bundle);
        getFragmentManager().popBackStack();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

}

