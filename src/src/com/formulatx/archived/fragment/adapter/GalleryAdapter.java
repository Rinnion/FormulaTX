package com.formulatx.archived.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import com.formulatx.archived.FormulaTXApplication;

import com.formulatx.archived.utils.Files;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.utils.OkHttp3Downloader;
import com.rinnion.archived.R;
import com.squareup.picasso.Callback;

import com.squareup.picasso.OkHttpDownloader;


import com.squareup.picasso.Picasso;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;


import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;

/**
 * Created by Lenovo on 03.02.2016.
 */
public class GalleryAdapter extends SimpleCursorAdapter {
    private static final String TAG = "GalleryAdapter";
    private final int mWidth;
    private boolean mShadowed;
    private Picasso picasso=null;

    public GalleryAdapter(Activity activity, String[] names, int[] to, final Cursor cursor, boolean mShadowed) {
        super(activity, R.layout.image_layout, cursor, names, to, 0);


        OkHttp3Downloader okHttp3Downloader=new  OkHttp3Downloader(this.mContext);


         picasso = new Picasso.Builder(this.mContext)
                .downloader(okHttp3Downloader)
                .build();

        this.mShadowed = mShadowed;
        DisplayMetrics dm = FormulaTXApplication.getAppContext().getResources().getDisplayMetrics();
        Log.d(TAG, String.format("[wp:%s] [d:%s] [nc:%s]", dm.widthPixels, dm.density, 2));
        mWidth = Math.abs(dm.widthPixels / 2);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = super.newView(context, cursor, parent);
        View iv = view.findViewById(R.id.il_iv_image);

        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        lp.height= mWidth;
        lp.width= mWidth;
        if (mShadowed) {
            View v = view.findViewById(R.id.il_v_shadow);
            lp = v.getLayoutParams();
            lp.height = mWidth;
            lp.width = mWidth;
        }
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        super.bindView(view, context, cursor);
    }




    @Override
    public void setViewImage(ImageView v, final String value) {
        View view=(View)v.getParent();




        final View progress=view.findViewById(R.id.progressBar);
        Log.d(TAG, "value: " + value);

            progress.setTag(R.id.product_tag_img, value);

            progress.setVisibility(View.VISIBLE);


        try {

            //File cache=new File(new File(Files.getCacheDir()),"picasso-gallery");
            //if(!cache.exists())
              //  cache.mkdir();
            //Files combo=new File,"picasso-gallery");
            /*
            OkHttpClient okHttpClient =  new OkHttpClient.Builder()
                    .cache(new okhttp3.Cache(cache, OkHttp3Downloader.calculateDiskCacheSize(cache)))
                    .build();
            okHttpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
                }
            });*/


            //okHttpClient.setCache(new Cache(Files.getCacheDir(), Integer.MAX_VALUE));
 //           OkHttpDownloader



            Callback callback =new Callback() {
                @Override
                public void onSuccess() {
                    Log.e(TAG,"Picasso callback onSuccess");

                    try {
                        String tag = (String) progress.getTag(R.id.product_tag_img);

                        //if (tag.equals(value)) {

                            progress.setVisibility(View.GONE);

                        //}
                        //else



                    }
                    catch (Exception ex)
                    {
                        Log.e(TAG,"Picasso callback",ex);

                    }

                }

                @Override
                public void onError() {
                    Log.e(TAG,"Picasso callback onError");

                    try {


                        String tag = (String) progress.getTag(R.id.product_tag_img);

                     //   if (tag.equals(value)) {
                            progress.setVisibility(View.GONE);

                       // }

                    }
                    catch (Exception ex)
                    {
                        Log.e(TAG,"Picasso callback",ex);
                    }

                }
            };

//with(this.mContext)
            //okHttp3Downloader.SetCallback(callback);
v.setTag(callback);
if(!value.isEmpty()) {
    Picasso.with(v.getContext())
            .load(value)
            .centerCrop()
            .placeholder(R.drawable.logo_splash_screen)
            .error(R.drawable.logo_splash_screen)
            .fit()
            .into(v, callback);
}
            else
{
    progress.setVisibility(View.GONE);


}
        }catch(Exception ignored){
            progress.setVisibility(View.GONE);

            Log.e(TAG, "Picasso", ignored);



        }
    }

}
