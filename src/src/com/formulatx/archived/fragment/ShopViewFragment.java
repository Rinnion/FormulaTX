package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.formulatx.archived.database.model.ApiObjects.Product;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.utils.WebViewWithCache;
import com.rinnion.archived.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.w3c.dom.Text;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class ShopViewFragment extends Fragment {

    private String TAG = getClass().getCanonicalName();
    public static final String EN_SHOP_ITEM="item";

    private WebViewWithCache mWebImgView;

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
        View view = inflater.inflate(R.layout.shop_desc_view, container, false);
        //mWebImgView = (WebViewWithCache) view.findViewById(R.id.wl_webview);

        TextView mTitle=(TextView)view.findViewById(R.id.textTitle);
        WebViewWithCache mContentWebView=(WebViewWithCache)view.findViewById(R.id.wl_webview);
        ImageView mImage=(ImageView)view.findViewById(R.id.image);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.string_imgwebview);
        ab.setIcon(R.drawable.ic_action_previous_item);

       //String url=  (String) getArguments().get(EN_SHOP_ITEM);

        final Product content=(Product)getArguments().getSerializable(EN_SHOP_ITEM);
        mTitle.setText(content.title);
        //mDesc.setText(content.content);
        //mContentWebView
        mContentWebView.loadData("<html><style>body {color:#FFF;}</style><body align='center'><h2>" + content.content + "</body></html>", "text/html; charset=UTF-8", null);
        mContentWebView.setBackgroundColor(Color.TRANSPARENT);

        final ProgressBar progress=(ProgressBar)view.findViewById(R.id.progressBar);
        final View shadow=view.findViewById(R.id.il_v_shadow );
        progress.setTag(R.id.product_tag_img, content.thumb);

        progress.setVisibility(View.VISIBLE);
        shadow.setVisibility(View.VISIBLE);

        try {

            Log.d(TAG,String.format("Picasso try load: %s",content.thumb));
            Picasso.with(getActivity())
                    .load(content.thumb)
                    .placeholder(R.drawable.logo_splash_screen)
                    .error(R.drawable.logo_splash_screen)
                    .resize(270,270)
                    .into(mImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            String tag= (String)progress.getTag(R.id.product_tag_img);

                            if(tag.equals(content.thumb)) {
                                progress.setVisibility(View.GONE);
                                shadow.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {
                            String tag= (String)progress.getTag(R.id.product_tag_img);

                            if(tag.equals(content.thumb)) {
                                progress.setVisibility(View.GONE);
                                shadow.setVisibility(View.GONE);
                            }
                        }
                    });
        }
        catch (Exception ex)
        {
            Log.d(TAG,"Picasso",ex);
            progress.setVisibility(View.GONE);
            shadow.setVisibility(View.GONE);
        }





        /*mWebImgView.loadData("<html><style>body {color:#FFF;}</style><body align='center'><img width=\"100%\" height=\"auto\" src='" +  url + "'></body></html>", "text/html; charset=UTF-8", null);
        mWebImgView.getSettings().setBuiltInZoomControls(true);
        mWebImgView.getSettings().setDisplayZoomControls(true);
        mWebImgView.setBackgroundColor(Color.TRANSPARENT);
        */

        return view;
    }

}

