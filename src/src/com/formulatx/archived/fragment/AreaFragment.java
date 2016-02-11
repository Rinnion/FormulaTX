
package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.AreaCursor;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.AreaHelper;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.database.model.ApiObjects.Area;
import com.formulatx.archived.database.model.ApiObjects.AreaOnline;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.utils.WebViewWithCache;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
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
    private View mWebNest;
    private MapFragment mMapFragment;
    private AreaCursor area;
    private GoogleMap mMap;
    private LatLngBounds llb;
    private AreaOnline[] mAreas;
    private boolean mCameraReady;

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
                if (mTabHost.getCurrentTabTag().equals(AUTOBUS) && mWebNest.getVisibility() == View.VISIBLE) {
                    mViewFlipper.bringChildToFront(mList);
                    mWebNest.setVisibility(View.GONE);
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

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament trnmt = th.getByPostName(getArguments().getString(TOURNAMENT_POST_NAME));

        AreaHelper ah = new AreaHelper(doh);
        area = ah.getAllByParent(trnmt.id);

        String[] from = new String[]{AreaHelper.COLUMN_TITLE};
        int[] to = new int[] {R.id.itml_text};
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_area_layout, area, from, to, 0);

        Bundle bundle = new Bundle();
        bundle.putLong(AreaLocaderCallback.TOURNAMENT_ID, trnmt.id);
        getLoaderManager().initLoader(R.id.loader_area, bundle, new AreaLocaderCallback());

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        area.close();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.area_layout, container, false);
        mTabHost = (TabHost) view.findViewById(R.id.tabHost);
        setupTabHost(mTabHost);

        mViewFlipper = (FrameLayout) mTabHost.findViewById(R.id.list_detail);
        mMapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.mapnest, mMapFragment)
                .commit();

        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                fillData();
                mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        mCameraReady = true;
                        moveCamera();
                        mMap.setOnCameraChangeListener(null);
                    }
                });
            }
        });

        mAutobusView = mViewFlipper.findViewById(R.id.autobus);
        mWebNest = (View) mViewFlipper.findViewById(R.id.al_ll_content);
        mWebContent = (WebViewWithCache) mViewFlipper.findViewById(R.id.al_web_content);
        mWebContent.setBackgroundColor(Color.TRANSPARENT);
        mList = (ListView) mAutobusView.findViewById(R.id.al_schedule);
        View v = mViewFlipper.findViewById(R.id.al_tv_empty);
        mList.setEmptyView(v);

        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(this);

        UptdateAutobusView();


        return view;
    }

    private void setupTabHost(TabHost tabHost) {
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec(MAP);
        tabSpec.setIndicator(FormulaTXApplication.getResourceString(R.string.string_map));
        tabSpec.setContent(R.id.al_ll_map);
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

        mViewFlipper.bringChildToFront(mAutobusView);
        mWebNest.setVisibility(View.INVISIBLE);
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
        mWebNest.setVisibility(View.VISIBLE);
        mAutobusView.setVisibility(View.INVISIBLE);
        mWebContent.setTag(R.id.view_area, item);
    }

    @Override
    public void onTabChanged(String s) {
        if (s.equals(MAP)){
        }
    }

    private class AreaLocaderCallback implements android.app.LoaderManager.LoaderCallbacks<AreaOnline[]> {

        public static final String TOURNAMENT_ID = ApiObjectHelper._ID;

        @Override
        public Loader<AreaOnline[]> onCreateLoader(int id, Bundle args) {
            long aLong = args.getLong(TOURNAMENT_ID);
            return new AreaAsyncLoader(getActivity(), aLong);
        }

        @Override
        public void onLoadFinished(Loader<AreaOnline[]> loader, AreaOnline[] data) {
            mAreas = data;
            fillData();
        }

        @Override
        public void onLoaderReset(Loader<AreaOnline[]> loader) {
            mAreas = null;
        }
    }

    private void fillData() {
        if (mMap == null) return;

        LatLngBounds.Builder llbb = new LatLngBounds.Builder();
        for (int i = 0; i < mAreas.length; i++) {
            AreaOnline item = mAreas[i];

            String[] split = TextUtils.split(String.valueOf(item.maps), ",");
            if (split.length != 2 ){
                Toast.makeText(getActivity(), "Error with lat,lng", Toast.LENGTH_LONG).show();
                return;
            }
            Float lat = Float.parseFloat(split[0]);
            Float lng = Float.parseFloat(split[1]);

            LatLng position = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(item.title));

            llbb.include(position);

        }

        if (mAreas.length == 0){
            return;
        }

        llb = llbb.build();

        moveCamera();
    }

    private void moveCamera() {
        if (!mCameraReady) return;
        if (mAreas == null ) return;
        if (mAreas.length > 1) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngBounds(llb, (int) (metrics.widthPixels * 0.33));
            mMap.animateCamera(yourLocation);
        }
        if (mAreas.length == 1) {
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(llb.getCenter(), 11);
            mMap.animateCamera(yourLocation);
        }
    }

}

