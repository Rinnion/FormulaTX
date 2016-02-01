package com.formulatx.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.formulatx.archived.database.cursor.NewsCursor;
import com.rinnion.archived.R;
import com.formulatx.archived.database.cursor.PartnerCursor;
import com.formulatx.archived.database.model.ApiObjects.Partner;
import com.formulatx.archived.utils.Log;
import com.squareup.picasso.Picasso;

public class PartnerAdapter extends SimpleCursorAdapter {
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

    public PartnerAdapter(Context context, NewsCursor mc) {
        super(context, R.layout.item_partner_layout, mc, fromSpinner, toSpinner, 0);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        //super.bindView(view, context, cursor);

        final ImageView imlThumb = (ImageView) view.findViewById(R.id.icl_iv_thumb);
        final TextView tvTitle = (TextView) view.findViewById(R.id.icl_tv_title);
        final TextView tvStatus= (TextView) view.findViewById(R.id.icl_tv_status);
        final TextView tvGoLink = (TextView) view.findViewById(R.id.icl_tv_buy);

        Partner item = ((PartnerCursor) cursor).getItem();

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
        tvGoLink.setText(item.link);
        tvStatus.setText(item.status);
    }
}
