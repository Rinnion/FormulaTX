package com.rinnion.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.GamerCursor;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.model.ApiObjects.Gamer;
import com.squareup.picasso.Picasso;

public class GamerAdapter extends SimpleCursorAdapter {
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

    public GamerAdapter(Context context, NewsCursor mc) {
        super(context, R.layout.item_gamer_layout, mc, fromSpinner, toSpinner, 0);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        //super.bindView(view, context, cursor);

        final ImageView imlThumb = (ImageView) view.findViewById(R.id.igl_iv_thumb);
        final TextView tvName = (TextView) view.findViewById(R.id.igl_tv_name);
        final TextView tvCountry = (TextView) view.findViewById(R.id.igl_tv_country);
        final TextView tvRating = (TextView) view.findViewById(R.id.igl_tv_rating);
        final ImageView tvFavorite = (ImageView) view.findViewById(R.id.igl_iv_like);

        Gamer item = ((GamerCursor) cursor).getItem();

        //imlThumb.getScaleX()

        Picasso.with(context).load(item.thumb).resize(80, 80).centerCrop().into(imlThumb);
        tvName.setText(item.full_name);
        tvCountry.setText(item.country);
        tvRating.setText(String.valueOf(item.rating));
        tvFavorite.setImageResource(R.drawable.like_noselected_icon);
    }
}
