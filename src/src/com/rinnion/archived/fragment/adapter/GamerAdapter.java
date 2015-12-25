package com.rinnion.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.NewsCursor;

public class GamerAdapter extends SimpleCursorAdapter {
    public static String[] fromSpinner = {
            "_id",
            "THUMB",
            "NAME",
            "COUNTRY",
            "COUNTRY-CODE",
            "RATING"
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
        super.bindView(view, context, cursor);

        final ImageView imlThumb = (ImageView) view.findViewById(R.id.igl_iv_thumb);
        final TextView tvName = (TextView) view.findViewById(R.id.igl_tv_name);
        final TextView tvCountry = (TextView) view.findViewById(R.id.igl_tv_country);
        final TextView tvRating = (TextView) view.findViewById(R.id.igl_tv_rating);

        String thumb = cursor.getString(cursor.getColumnIndex(fromSpinner[1]));
        String name = cursor.getString(cursor.getColumnIndex(fromSpinner[2]));
        String country = cursor.getString(cursor.getColumnIndex(fromSpinner[3]));
        String rating = cursor.getString(cursor.getColumnIndex(fromSpinner[5]));

        imlThumb.setImageBitmap(BitmapFactory.decodeFile(thumb));
        tvName.setText(name);
        tvCountry.setText(country);
        tvRating.setText(rating);
    }
}
