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
            "CAPTION",
            "DATA"
    };
    private static int[] toSpinner = {
            R.id.inl_iv_thumb,
            R.id.inl_tv_caption,
            R.id.inl_tv_data
    };

    public GamerAdapter(Context context, NewsCursor mc) {
        super(context, R.layout.item_news_layout, mc, fromSpinner, toSpinner, 0);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);

        final ImageView imlThumb = (ImageView) view.findViewById(R.id.inl_iv_thumb);
        final TextView tvCaption = (TextView) view.findViewById(R.id.inl_tv_caption);
        final TextView tvData = (TextView) view.findViewById(R.id.inl_tv_data);

        String thumb = cursor.getString(cursor.getColumnIndex(fromSpinner[0]));
        String caption = cursor.getString(cursor.getColumnIndex(fromSpinner[1]));
        String data = cursor.getString(cursor.getColumnIndex(fromSpinner[2]));

        imlThumb.setImageBitmap(BitmapFactory.decodeFile(thumb));
        tvCaption.setText(caption);
        tvData.setText(data);
    }
}
