package com.rinnion.archived.fragment.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.rinnion.archived.fragment.ProgramCursor;
import com.rinnion.archived.utils.Log;

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
        LinearLayout llLink = (LinearLayout) v.findViewById(R.id.ipel_ll_link);

        tvTitle.setText(item.getTitle());
        tvTime.setText(item.getTime());
        boolean past = item.getInPast();
        llLink.setAlpha(past ? 0.15f : 1f);

    }

    private void fillDayView(View v, ProgramCursor item) {
        TextView tvTitle = (TextView) v.findViewById(R.id.ipdl_tv_title);
        ImageView ivImage = (ImageView) v.findViewById(R.id.ipdl_iv_image);

        tvTitle.setText(item.getTitle());
        boolean today = item.getToday();
        boolean past = item.getInPast();
        if (today) ivImage.setImageResource(R.drawable.square_selected_today);
        if (!today && past) ivImage.setImageResource(R.drawable.square_noselected_past);
        if (!today && !past) ivImage.setImageResource(R.drawable.square_selected_future);
    }

}
