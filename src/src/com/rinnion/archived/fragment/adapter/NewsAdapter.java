package com.rinnion.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObject;

public class NewsAdapter extends SimpleCursorAdapter {
    public static String[] fromSpinner = {
            ApiObjectHelper._ID,
            ApiObjectHelper.COLUMN_THUMB,
            ApiObjectHelper.COLUMN_TITLE,
            ApiObjectHelper.COLUMN_CONTENT
    };
    private static int[] toSpinner = {
            R.id.inl_iv_thumb,
            R.id.inl_tv_caption,
            R.id.inl_tv_data
    };

    public NewsAdapter(Context context, NewsCursor mc) {
        super(context, R.layout.item_news_layout, mc, fromSpinner, toSpinner, 0);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);

        final ImageView imlThumb = (ImageView) view.findViewById(R.id.inl_iv_thumb);
        final TextView tvCaption = (TextView) view.findViewById(R.id.inl_tv_caption);
        final TextView tvData = (TextView) view.findViewById(R.id.inl_tv_data);

        ApiObjectCursor aoc = (ApiObjectCursor) cursor;

        ApiObject item = aoc.getItem();

        String thumb = item.thumb;
        String caption = item.title;
        String data = item.date;

        Bitmap bitmap = ArchivedApplication.getBitmap(thumb);

        imlThumb.setImageBitmap(bitmap);

        tvCaption.setText(caption);
        tvData.setText(data);
    }

}
