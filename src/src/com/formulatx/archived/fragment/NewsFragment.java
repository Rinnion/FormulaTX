package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.FrameLayout;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.utils.WebViewWithCache;
import com.formulatx.archived.utils.Log;
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
public class NewsFragment extends Fragment {

    public static final String ID = "id";
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
        super.onCreate(savedInstanceState);
    }

    private String prepateHtml(ApiObject apiObject)
    {
        if(apiObject==null)
            return "";



        String thumb =(apiObject.thumb.isEmpty())?"":"<div><img src='"+ apiObject.thumb + "' style=\"width: 100%; height: auto;\"></div>";


        String title=(apiObject.title.isEmpty())?"":"<div style=\"color: #ffffff; font-size: 14pt; font-face: Roboto-Medium\">" + apiObject.title + "</div>";
        String date=(apiObject.date.isEmpty())?"":"<div style=\"color: #929292; font-size: 11pt; font-face: Roboto-Regular\">" + apiObject.date + "</div>";
        String content=(apiObject.content.isEmpty())?"":apiObject.content;

        Log.d(TAG,String.format("Thumb: %s\nTitle: %s\nDate: %s\nContent: %s\n\n",thumb,title,date,content));


        String strHtml="<html><style>body {padding:0px; color:#FFF;}</style><body>" + thumb + "<p>" + title  + date + "</p><div style=\"color: #EBEAEA; font-size: 12pt; font-face: Roboto-Regular\">" + content + "</div></body></html>";
        return strHtml;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.mainFrame);

        Bundle args = getArguments();
        long anInt = args.getLong(NewsFragment.ID);
        ApiObjectHelper aoh = new ApiObjectHelper(FormulaTXApplication.getDatabaseOpenHelper());
        ApiObject apiObject = aoh.get(anInt);

        WebViewWithCache myWebView = (WebViewWithCache) view.findViewById(R.id.nl_tv_content);

        String webHTML=prepateHtml(apiObject);
        String webHTMLEmpty="<html><style>body {color:#FFF;}</style><body align='center'><h2></h2>Нет описания</body></html>";
        if (apiObject.content.isEmpty())
            webHTMLEmpty="<html><style>body {color:#FFF;}</style><body align='center'><h2>" + apiObject.title + "</h2>Нет описания</body></html>";

        myWebView.loadDataOrCache(getActivity(), apiObject, webHTML, webHTMLEmpty);

        myWebView.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_layout, container, false);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.string_news);
        ab.setIcon(R.drawable.ic_action_previous_item);

        //BackgroundSelector.setProperBackground(view, type);

        return view;
    }

}

