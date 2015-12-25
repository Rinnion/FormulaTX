package com.rinnion.archived.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.model.Message;
import com.rinnion.archived.network.tasks.AsyncActivityTask;

/**
 * Created by tretyakov on 18.08.2015.
 */
public class NewsAdapter extends SimpleCursorAdapter {
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

    public NewsAdapter(Context context, NewsCursor mc) {
        super(context, R.layout.item_news_layout, mc, fromSpinner, toSpinner, 0);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);

        //final Message msg = ((MessageCursor) cursor).getItem();

        final ImageView imlThumb = (ImageView) view.findViewById(R.id.inl_iv_thumb);
        final TextView tvCaption = (TextView) view.findViewById(R.id.inl_tv_caption);
        final TextView tvData = (TextView) view.findViewById(R.id.inl_tv_data);

        String thumb = cursor.getString(cursor.getColumnIndex(fromSpinner[0]));
        String caption = cursor.getString(cursor.getColumnIndex(fromSpinner[1]));
        String data = cursor.getString(cursor.getColumnIndex(fromSpinner[2]));

        //if (thumb != null) imlThumb.setImageBitmap();
        tvCaption.setText(caption);
        tvData.setText(data);

    }

    private int getRandomBackgroundResource() {
        int r = (int) Math.floor(Math.random() * 8);
        switch (r) {
            case 0:
                return R.drawable.bkg_black;
            case 1:
                return R.drawable.bkg_red;
            case 2:
                return R.drawable.bkg_yellow;
            case 3:
                return R.drawable.bkg_green;
            case 4:
                return R.drawable.bkg_cyan;
            case 5:
                return R.drawable.bkg_blue;
            case 6:
                return R.drawable.bkg_purple;
            case 7:
                return R.drawable.bkg_white;
            default:
                return R.drawable.bkg_black;
        }
    }

    public interface IMessageClickListener {
        void Share(Message message);
    }

    private class SendLikeAsyncHandler implements AsyncActivityTask.IAsyncHandler<Void> {

        @Override
        public void onBeforeExecute() {
        }

        @Override
        public void onAfterExecute(Void aVoid) {
        }

        @Override
        public void onCancelExecute(Void aVoid) {
        }
    }
}
