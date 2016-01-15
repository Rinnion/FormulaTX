package com.rinnion.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.GamerCursor;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.cursor.ProductCursor;
import com.rinnion.archived.database.model.ApiObjects.Gamer;
import com.rinnion.archived.database.model.ApiObjects.Product;
import com.rinnion.archived.utils.Log;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends SimpleCursorAdapter {
    private final String TAG = getClass().getSimpleName();
    public static String[] fromSpinner = {
            "_id",
            "_id",
            "_id",
            "_id"
    };
    private static int[] toSpinner = {
            R.id.ipl_iv_thumb,
            R.id.ipl_tv_title,
            R.id.ipl_tv_price
    };

    public ProductAdapter(Context context, NewsCursor mc) {
        super(context, R.layout.item_product_layout, mc, fromSpinner, toSpinner, 0);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        //super.bindView(view, context, cursor);

        final ImageView imlThumb = (ImageView) view.findViewById(R.id.ipl_iv_thumb);
        final TextView tvTitle = (TextView) view.findViewById(R.id.ipl_tv_title);
        final TextView tvPrice= (TextView) view.findViewById(R.id.ipl_tv_price);

        Product item = ((ProductCursor) cursor).getItem();

        //imlThumb.getScaleX()

        try {
            Picasso.with(context)
                    .load(item.thumb)
                    .placeholder(R.drawable.logo_splash_screen)
                    .error(R.drawable.logo_splash_screen)
                    .resize(75, 75)
                    .centerCrop()
                    .into(imlThumb);
        }catch(Exception ignore){
            Log.e(TAG, ignore.getLocalizedMessage());
        }
        tvTitle.setText(item.title);
        tvPrice.setText(item.price);
    }
}
