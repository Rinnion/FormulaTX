package com.formulatx.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.formulatx.archived.database.cursor.NewsCursor;
import com.formulatx.archived.database.cursor.ProductCursor;
import com.formulatx.archived.database.model.ApiObjects.Product;
import com.rinnion.archived.R;
import com.formulatx.archived.utils.Log;
import com.squareup.picasso.Callback;
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


        final View progress=view.findViewById(R.id.progressBar);
        final View shadow=view.findViewById(R.id.il_v_shadow );
        final Product item = ((ProductCursor) cursor).getItem();
        progress.setTag(R.id.product_tag_img, item.thumb);

        progress.setVisibility(View.VISIBLE);
        shadow.setVisibility(View.VISIBLE);




        //int intBool=Integer.parseInt(item.top);


        //imlThumb.getScaleX()

        try {
            Picasso.with(context)
                    .load(item.thumb)
                    .placeholder(R.drawable.logo_splash_screen)
                    .error(R.drawable.logo_splash_screen)
                    .resize(200, 200)
                    .centerCrop()
                    .into(imlThumb, new Callback() {
                        @Override
                        public void onSuccess() {
                            String tag= (String)progress.getTag(R.id.product_tag_img);

                            if(tag.equals(item.thumb)) {
                                progress.setVisibility(View.GONE);
                                shadow.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {
                            String tag= (String)progress.getTag(R.id.product_tag_img);

                            if(tag.equals(item.thumb)) {
                                progress.setVisibility(View.GONE);
                                shadow.setVisibility(View.GONE);
                            }
                        }
                    });
        }catch(Exception ignore){
            Log.e(TAG, ignore.getLocalizedMessage());
            progress.setVisibility(View.GONE);
            shadow.setVisibility(View.GONE);
        }
        tvTitle.setText(item.title);
        tvPrice.setText(item.price);
    }
}
