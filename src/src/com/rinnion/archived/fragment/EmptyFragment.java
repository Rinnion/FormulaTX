package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebView;
import com.rinnion.archived.R;
import com.rinnion.archived.utils.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class EmptyFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.about_layout, container, false);
        mTextViewAbout = (WebView) view.findViewById(R.id.tv_about);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.string_stub);
        ab.setIcon(R.drawable.ic_action_previous_item);

        mTextViewAbout.loadData("<html><style>body {color:#FFF;}</style><body align='center'><h2>Заглушка</h2>Здесь пока ничего нет</body></html>", "text/html; charset=UTF-8", null);
        mTextViewAbout.setBackgroundColor(Color.TRANSPARENT);

        return view;
    }

}

