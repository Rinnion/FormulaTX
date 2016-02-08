
package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.AreaCursor;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.AreaHelper;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.database.model.ApiObjects.Area;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.utils.WebViewWithCache;
import com.rinnion.archived.R;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class AreaFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.OnTabChangeListener {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    public static final String MAP = "map";
    public static final String AUTOBUS = "autobus";
    private String TAG = getClass().getCanonicalName();
    private View mAutobusView;
    private WebViewWithCache mWebContent;
    private ListView mList;
    private SimpleCursorAdapter mAdapter;
    private FrameLayout mViewFlipper;
    private TabHost mTabHost;

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
                if (mTabHost.getCurrentTabTag().equals(AUTOBUS) && mWebContent.getVisibility() == View.VISIBLE) {
                    mViewFlipper.bringChildToFront(mList);
                    mWebContent.setVisibility(View.GONE);
                    mAutobusView.setVisibility(View.VISIBLE);
                }else{
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
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.area_layout, container, false);
        mTabHost = (TabHost) view.findViewById(R.id.tabHost);
        setupTabHost(mTabHost);

        mViewFlipper = (FrameLayout) mTabHost.findViewById(R.id.list_detail);
        mAutobusView = mViewFlipper.findViewById(R.id.autobus);
        mWebContent = (WebViewWithCache) mViewFlipper.findViewById(R.id.al_web_content);
        mWebContent.setBackgroundColor(Color.TRANSPARENT);
        mList = (ListView) mAutobusView.findViewById(R.id.al_schedule);
        View v = mViewFlipper.findViewById(R.id.al_tv_empty);
        mList.setEmptyView(v);

        String[] from = new String[]{AreaHelper.COLUMN_TITLE};
        int[] to = new int[] {R.id.itml_text};
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_area_layout, null, from, to, 0);
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(this);

        UptdateAutobusView();

        /*

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament trnmt = th.getByPostName(getArguments().getString(AreaFragment.TOURNAMENT_POST_NAME));

        AreaHelper ah = new AreaHelper(doh);
        AreaCursor area = ah.getAllByParent(trnmt.id);

        if (area.getCount() == 0) {
            Toast.makeText(getActivity(), FormulaTXApplication.getResourceString(R.string.string_no_area_found), Toast.LENGTH_LONG).show();
            return;
        }
        Area item = area.getItem();

        if (item == null){
            Toast.makeText(getActivity(), "Area not available...", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            String[] split = TextUtils.split(String.valueOf(item.map), ",");
            if (split.length != 2 ){
                Toast.makeText(getActivity(), "Error with lat,lng", Toast.LENGTH_LONG).show();
                return;
            }
            Float lat = Float.parseFloat(split[0]);
            Float lng = Float.parseFloat(split[1]);

            String coords = String.valueOf(lat) + "," + String.valueOf(lng);
            String uriString = "geo:"+ coords +"?q=" + Uri.encode(String.valueOf(trnmt.title));

            Uri uri = Uri.parse(uriString);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (ActivityNotFoundException e){
            Toast.makeText(getActivity(), FormulaTXApplication.getResourceString(R.string.string_no_installer_map_app), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        */

        return view;
    }

    private void setupTabHost(TabHost tabHost) {
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec(MAP);
        tabSpec.setIndicator(FormulaTXApplication.getResourceString(R.string.string_map));
        tabSpec.setContent(R.id.map);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(AUTOBUS);
        tabSpec.setIndicator(FormulaTXApplication.getResourceString(R.string.string_autobus));
        tabSpec.setContent(R.id.list_detail);
        tabHost.addTab(tabSpec);

        tabHost.setOnTabChangedListener(this);
        tabHost.setCurrentTabByTag(AUTOBUS);
    }

    private void UptdateAutobusView() {
        if (mAutobusView == null) return;

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament trnmt = th.getByPostName(getArguments().getString(TOURNAMENT_POST_NAME));

        AreaHelper ah = new AreaHelper(doh);
        AreaCursor area = ah.getAllByParent(trnmt.id);

        Cursor cursor = mAdapter.swapCursor(area);
        if (cursor != null) cursor.close();

        if (area.getCount() == 0) {
            return;
        }

        mViewFlipper.bringChildToFront(mAutobusView);
        mWebContent.setVisibility(View.INVISIBLE);
        mAutobusView.setVisibility(View.VISIBLE);

    }


    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_tournament_findway);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Area item = ((AreaCursor) (adapterView.getItemAtPosition(i))).getItem();
        mWebContent.loadData(item.content, "text/html; charset=UTF-8", null);
        mViewFlipper.bringChildToFront(mWebContent);
        mWebContent.setVisibility(View.VISIBLE);
        mAutobusView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTabChanged(String s) {
        //mViewFlipper.cle
    }
}

