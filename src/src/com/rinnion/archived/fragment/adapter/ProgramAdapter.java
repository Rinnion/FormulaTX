package com.rinnion.archived.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.GamerCursor;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.model.ApiObjects.Gamer;
import com.rinnion.archived.fragment.ProgramCursor;
import com.rinnion.archived.utils.Log;
import com.squareup.picasso.Picasso;

public class ProgramAdapter extends SimpleCursorAdapter {
    private final String TAG = getClass().getSimpleName();
    private final Activity activity;
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

    public ProgramAdapter(Activity activity, ProgramCursor mc) {
        super(activity, R.layout.item_gamer_layout, mc, fromSpinner, toSpinner, 0);
        this.activity = activity;
    }

    @Override
    public int getViewTypeCount() {
        int count = 2;
        Log.d(TAG, "getViewTypeCount: " + count );
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        ProgramCursor cursor = (ProgramCursor) getItem(position);
        int type = cursor.getType();
        Log.d(TAG, "pos: " + position + "; id: " + cursor.getId() + "; type:" + type );
        return type;
    }

    @Override
    public View getView(final int pos, View v, ViewGroup parent){
        Log.d(TAG, "getView POS: " +String.valueOf(pos));
        int type = getItemViewType(pos);
        if (v == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            switch (type) {
                case ProgramCursor.TYPE_DAY:
                    v = inflater.inflate(R.layout.item_program_day_layout, parent, false);
                    Log.d(TAG,"inflate " + R.layout.item_program_day_layout);
                    break;
                case ProgramCursor.TYPE_EVT:
                    v = inflater.inflate(R.layout.item_program_evt_layout, parent , false);
                    Log.d(TAG,"inflate " + R.layout.item_program_evt_layout);
                    break;
            }
        }
        switch (type){
            case ProgramCursor.TYPE_DAY: fillDayView(v, (ProgramCursor) getItem(pos)); break;
            case ProgramCursor.TYPE_EVT: fillEvtView(v, (ProgramCursor) getItem(pos)); break;
            default: Log.e(TAG, "wrong type of item"); break;

        }
        Log.d(TAG, "getView " + String.valueOf(v));
        return v;
    }

    private void fillEvtView(View v, ProgramCursor item) {
        TextView tvTitle = (TextView) v.findViewById(R.id.ipdl_tv_title);
        TextView tvTime = (TextView) v.findViewById(R.id.ipdl_tv_time);
        ImageView ivImage = (ImageView) v.findViewById(R.id.ipdl_iv_image);

        tvTitle.setText(item.getTitle());
        tvTime.setText(item.getTime());
    }

    private void fillDayView(View v, ProgramCursor item) {
        TextView tvTitle = (TextView) v.findViewById(R.id.ipdl_tv_title);
        ImageView ivImage = (ImageView) v.findViewById(R.id.ipdl_iv_image);

        tvTitle.setText(item.getTitle());
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        Log.d(TAG, "bind");

        //super.bindView(view, context, cursor);

        final ImageView imlThumb = (ImageView) view.findViewById(R.id.igl_iv_thumb);
        final TextView tvName = (TextView) view.findViewById(R.id.igl_tv_name);
        final TextView tvCountry = (TextView) view.findViewById(R.id.igl_tv_country);
        final TextView tvRating = (TextView) view.findViewById(R.id.igl_tv_rating);
        final ImageView tvFavorite = (ImageView) view.findViewById(R.id.igl_iv_like);

        Gamer item = ((GamerCursor) cursor).getItem();

        //imlThumb.getScaleX()

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
        }
        tvName.setText(item.full_name);
        tvCountry.setText(item.country);
        tvRating.setText(String.valueOf(item.rating));
        tvFavorite.setImageResource(R.drawable.like_noselected_icon);
    }
}
