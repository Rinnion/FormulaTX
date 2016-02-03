package com.formulatx.archived.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.utils.Log;
import com.rinnion.archived.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Lenovo on 03.02.2016.
 */
public class GalleryAdapter extends SimpleCursorAdapter {
    private static final String TAG = "GalleryAdapter";
    private final int mWidth;

    public GalleryAdapter(Activity activity, String[] names, int[] to, final Cursor cursor) {
        super(activity, R.layout.image_layout, cursor, names, to, 0);
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
        View v = view.findViewById(R.id.il_v_shadow);
        lp = v.getLayoutParams();
        lp.height= mWidth;
        lp.width= mWidth;
        return view;
    }

    @Override
    public void setViewImage(ImageView v, String value) {
        try {
            Picasso.with(mContext)
                    .load(value)
                    .centerCrop()
                    .placeholder(R.drawable.logo_splash_screen)
                    .fit()
                    .into(v);
        }catch(Exception ignored){
            Log.e(TAG, ignored.getMessage());
        }
    }

}
