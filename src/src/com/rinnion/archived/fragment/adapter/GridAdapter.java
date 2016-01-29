package com.rinnion.archived.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.rinnion.archived.R;
import com.rinnion.archived.Utils;
import com.rinnion.archived.network.loaders.cursor.ParserDataCursor;
import com.rinnion.archived.network.loaders.cursor.ProgramCursor;
import com.rinnion.archived.parsers.Gamer;
import com.rinnion.archived.parsers.Match;
import com.rinnion.archived.parsers.Team;

public class GridAdapter extends SimpleCursorAdapter {
    private final String TAG = getClass().getSimpleName();
    private final Activity activity;
    public static String[] fromSpinner = {
            "_id",
            "_id",
            "_id",
            "_id"
    };
    private static int[] toSpinner = {
            R.id.itl_tv_caption,
            R.id.itl_tv_caption,
            R.id.itl_tv_caption
    };

    public GridAdapter(Activity activity, ProgramCursor mc) {
        super(activity, R.layout.item_grid_layout, mc, fromSpinner, toSpinner, 0);
        this.activity = activity;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Match match = ((ParserDataCursor)cursor).getItem().getMatch();
        if (match == null) return;

        TextView tv = (TextView) view.findViewById(R.id.itl_tv_match);
        tv.setText(match.header);

        View t1 = view.findViewById(R.id.itl_ll_team_1);
        View t2 = view.findViewById(R.id.itl_ll_team_2);
        View t3 = view.findViewById(R.id.itl_ll_team_3);
        View t4 = view.findViewById(R.id.itl_ll_team_4);

        fitTeam(t1, match.team1);
        fitTeam(t2, match.team2);
        fitTeam(t3, match.team3);
        fitTeam(t4, match.team4);

    }

    private void fitTeam(View view, Team team) {
        if (team == null){
            view.setVisibility(View.GONE);
            return;
        }

        view.setVisibility(View.VISIBLE);
        View g1 = view.findViewById(R.id.td_rl_gamer_1);
        View g2 = view.findViewById(R.id.td_rl_gamer_2);

        fitGamer(g1, team.gamers.get(0));
        if (team.gamers.size() == 2) {
            g2.setVisibility(View.VISIBLE);
            fitGamer(g2, team.gamers.get(1));
        }
        else{
            g2.setVisibility(View.GONE);
        }


        TextView extra = (TextView) view.findViewById(R.id.tdg_tv_extra);
        extra.setText(team.extra);

        TextView r11 = (TextView) view.findViewById(R.id.itl_tv_round_1);
        TextView r12 = (TextView) view.findViewById(R.id.itl_tv_round_2);
        TextView r13 = (TextView) view.findViewById(R.id.itl_tv_round_3);

        r11.setText(team.r1);
        r12.setText(team.r2);
        r13.setText(team.r3);
    }

    private void fitGamer(View v1, Gamer gamer) {
        TextView name = (TextView) v1.findViewById(R.id.itl_tv_gamer);
        ImageView cc = (ImageView) v1.findViewById(R.id.itl_iv_gamer_cc);

        name.setText(gamer.name);

        if (gamer.cc != null) {
            cc.setVisibility(View.VISIBLE);
            //TODO Load image here
        }else{
            cc.setVisibility(View.GONE);
        }

        if (gamer.photo != null){
            cc.setVisibility(View.VISIBLE);
            //TODO Load photo here
        }else{
            cc.setVisibility(View.GONE);
        }
    }


}
