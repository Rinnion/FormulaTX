
package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import com.rinnion.archived.R;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class GalleryFragment extends Fragment {

    private String TAG = getClass().getCanonicalName();
    private WebView mTextViewAbout;

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
        TabHost tabHost = (TabHost) inflater.inflate(R.layout.gallery_tabs_layout, container, false);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        // создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec("photo");
        tabSpec.setIndicator("Фото");
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("video");
        tabSpec.setIndicator("Видео");
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        // вторая вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag("photo");

        String[] columns = new String[]{"_id", "path"};
        MatrixCursor mc = new MatrixCursor(columns);
        mc.addRow(new Object[]{1, R.drawable.general_bg});
        mc.addRow(new Object[]{2, R.drawable.st_lady_bg});
        mc.addRow(new Object[]{3, R.drawable.st_open_bg});

        GridView gv = (GridView) tabHost.findViewById(R.id.gtl_gv_photo);

        String[] names = new String[]{"path"};
        int[] to = new int[] {R.id.il_iv_image};
        gv.setAdapter(new SimpleCursorAdapter(getActivity(), R.layout.image_layout, mc, names, to, 0 ){
            @Override
            public void setViewImage(ImageView v, String value) {
                v.setImageResource(R.drawable.logo_splash_screen);
            }
        });



        return tabHost;
    }


    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_gallery);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
    }

}

