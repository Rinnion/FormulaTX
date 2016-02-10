package com.formulatx.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import com.formulatx.archived.database.cursor.NewsCursor;
import com.formulatx.archived.utils.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.formulatx.archived.database.cursor.GamerCursor;
import com.formulatx.archived.database.model.ApiObjects.Gamer;
import com.squareup.picasso.Picasso;

public class GamerAdapter extends SimpleCursorAdapter {
    private final String TAG = getClass().getSimpleName();
    public static String[] fromSpinner = {
            "_id",
            "_id",
            "_id",
            "_id"
    };
    private static int[] toSpinner = {
            R.id.inl_iv_thumb,
            R.id.inl_tv_caption,
            R.id.inl_tv_data
    };
    private final View.OnClickListener clickEvent;
    private GamerOnClickListener mListener;

    public GamerAdapter(Context context, NewsCursor mc, GamerOnClickListener listener) {
        super(context, R.layout.item_gamer_layout, mc, fromSpinner, toSpinner, 0);
        mListener = listener;
        clickEvent = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Gamer gamer = (Gamer) view.getTag(R.id.tag_gamer_object);
                if (mListener != null) mListener.likeClick(gamer);
            }
        };
    }

    public interface GamerOnClickListener{
        void likeClick(Gamer gamer);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        //super.bindView(view, context, cursor);

        final ImageView imlThumb = (ImageView) view.findViewById(R.id.igl_iv_thumb);
        final TextView tvName = (TextView) view.findViewById(R.id.igl_tv_name);
        final TextView tvCountry = (TextView) view.findViewById(R.id.igl_tv_country);
        final TextView tvRating = (TextView) view.findViewById(R.id.igl_tv_rating);
        final ImageView tvFavorite = (ImageView) view.findViewById(R.id.igl_iv_like);
        final ImageView ivFlag = (ImageView) view.findViewById(R.id.igl_iv_flag);

        Gamer item = ((GamerCursor) cursor).getItem();

        try {
            Picasso.with(context)
                    .load(item.thumb)
                    .placeholder(R.drawable.logo_splash_screen)
                    .error(R.drawable.logo_splash_screen)
                    .resize(80, 80)
                    .centerCrop()
                    .into(imlThumb);
        }catch(Exception ignore){
            Log.e(TAG, ignore.getLocalizedMessage());
            imlThumb.setImageDrawable(null);
        }

        try {
            Picasso.with(context)
                    .load(item.flag)
                    .placeholder(R.drawable.logo_splash_screen)
                    .error(R.drawable.logo_splash_screen)
                    .resize(80, 80)
                    .centerCrop()
                    .into(ivFlag);
        }catch(Exception ignore){
            Log.e(TAG, ignore.getLocalizedMessage());
            ivFlag.setImageDrawable(null);
        }
        tvName.setText(item.title);
        tvCountry.setText(item.country);
        tvRating.setText(String.valueOf((int)item.rating));

        tvFavorite.setImageResource(item.favorite ? R.drawable.like_selected_icon : R.drawable.like_noselected_icon);

        tvFavorite.setTag(R.id.tag_gamer_object, item);
        tvFavorite.setOnClickListener(clickEvent);
    }
}
