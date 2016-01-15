package com.rinnion.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.CardCursor;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.model.ApiObjects.Card;
import com.rinnion.archived.utils.Log;
import com.squareup.picasso.Picasso;

public class CardAdapter extends SimpleCursorAdapter {
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

    public CardAdapter(Context context, NewsCursor mc) {
        super(context, R.layout.item_card_layout, mc, fromSpinner, toSpinner, 0);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        //super.bindView(view, context, cursor);

        final ImageView imlThumb = (ImageView) view.findViewById(R.id.icl_iv_thumb);
        final TextView tvTitle = (TextView) view.findViewById(R.id.icl_tv_title);
        final TextView tvstatus= (TextView) view.findViewById(R.id.icl_tv_status);

        Card item = ((CardCursor) cursor).getItem();

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
        tvstatus.setText(item.status);
    }
}
