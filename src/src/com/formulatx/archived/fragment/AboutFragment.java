package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.utils.WebViewWithCache;
import com.rinnion.archived.R;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.model.ApiObject;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class AboutFragment extends Fragment  {

    public static final String TYPE = "TYPE";
    private String TAG = getClass().getCanonicalName();
    private ApiObject mApiObject;

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
        final WebViewWithCache myWebView = (WebViewWithCache) view.findViewById(R.id.tv_about);

        Bundle args = getArguments();
        String type = args.getString(TYPE);

        ApiObjectHelper th = new ApiObjectHelper(FormulaTXApplication.getDatabaseOpenHelper());

        mApiObject = th.getByPostName(type);

        String thumb =(mApiObject.thumb.isEmpty())?"":"<div><img src='"+ mApiObject.thumb + "' style=\"width: 100%; height: auto;\"></div>";

        String webHTML="<html><style>p {color:#FFF;}</style><body>" + thumb + (((mApiObject==null)||(mApiObject.content==null))?"":mApiObject.content) + "</body></html>";
        String webHTMLEmpty="<html><style>body {color:#FFF;}</style><body align='center'><h2></h2>Нет описания</body></html>";
        if (mApiObject.content.isEmpty())
            webHTMLEmpty="<html><style>body {color:#FFF;}</style><body align='center'><h2>" + mApiObject.title + "</h2>Нет описания</body></html>";



        myWebView.loadDataOrCache(getActivity(),mApiObject,webHTML,webHTMLEmpty);


        myWebView.setBackgroundColor(Color.TRANSPARENT);
        return view;
    }








    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            if (mApiObject != null) {
                ab.setTitle(mApiObject.title);
            } else {
                ab.setTitle(getArguments().getString(TYPE));
            }
            ab.setIcon(R.drawable.ic_action_previous_item);
        }

    }
}

