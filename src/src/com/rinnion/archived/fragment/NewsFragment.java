package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObject;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_layout, container, false);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.string_news);
        ab.setIcon(R.drawable.ic_action_previous_item);

        Bundle args = getArguments();
        long anInt = args.getLong(NewsFragment.ID);
        ApiObjectHelper aoh = new ApiObjectHelper(ArchivedApplication.getDatabaseOpenHelper());
        ApiObject apiObject = aoh.get(anInt);

        ImageView image = (ImageView) view.findViewById(R.id.nl_iv_image);
        TextView title = (TextView) view.findViewById(R.id.nl_tv_title);
        TextView date = (TextView) view.findViewById(R.id.nl_tv_date);
        WebView content = (WebView) view.findViewById(R.id.nl_tv_content);

        String thumb = apiObject.thumb;
        if (thumb != null) {
            image.setImageBitmap(BitmapFactory.decodeFile(thumb));
        } else {
            image.setImageResource(R.drawable.logo_splash_screen);
        }

        content.loadData("<html><style>body {padding:0px; color:#FFF;}</style><body>" + apiObject.content + "</body></html>", "text/html; charset=UTF-8", null);
                content.setBackgroundColor(Color.TRANSPARENT);
        date.setText(apiObject.date);
        title.setText(apiObject.title);


        return view;
    }

}

