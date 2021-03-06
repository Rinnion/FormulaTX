package com.formulatx.archived.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.formulatx.archived.Utils;
import com.formulatx.archived.fragment.MatchCursor;
import com.formulatx.archived.network.loaders.cursor.ProgramCursor;
import com.rinnion.archived.R;
import com.formulatx.archived.network.loaders.cursor.ParserDataCursor;
import com.formulatx.archived.parsers.Gamer;
import com.formulatx.archived.parsers.Match;
import com.formulatx.archived.parsers.Team;

public class ScheduleAdapter extends SimpleCursorAdapter {
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

    public ScheduleAdapter(Activity activity, ProgramCursor mc) {
        super(activity, R.layout.item_table_layout, mc, fromSpinner, toSpinner, 0);
        this.activity = activity;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Match match = ((MatchCursor)cursor).getMatch();

        TextView tvHeader = (TextView) view.findViewById(R.id.itl_tv_header);
        TextView tvMatch = (TextView) view.findViewById(R.id.itl_tv_match);
        tvHeader.setText(match.cort);
        tvMatch.setText(match.header);

        View t1 = view.findViewById(R.id.itl_ll_team_1);
        View t2 = view.findViewById(R.id.itl_ll_team_2);

        fitTeam(t1, match.team1);
        fitTeam(t2, match.team2);

        fitTeamBest(t1,t2,match.team1, match.team2);

    }

    private void fitTeamBest(View t1, View t2, Team team1, Team team2) {
        TextView r11 = (TextView) t1.findViewById(R.id.itl_tv_round_1);
        TextView r12 = (TextView) t1.findViewById(R.id.itl_tv_round_2);
        TextView r13 = (TextView) t1.findViewById(R.id.itl_tv_round_3);
        TextView r21 = (TextView) t2.findViewById(R.id.itl_tv_round_1);
        TextView r22 = (TextView) t2.findViewById(R.id.itl_tv_round_2);
        TextView r23 = (TextView) t2.findViewById(R.id.itl_tv_round_3);

        int i11 = Utils.parseWithDefault(team1.r1, 0);
        int i12 = Utils.parseWithDefault(team1.r2, 0);
        int i13 = Utils.parseWithDefault(team1.r3, 0);
        int i21 = Utils.parseWithDefault(team2.r1, 0);
        int i22 = Utils.parseWithDefault(team2.r2, 0);
        int i23 = Utils.parseWithDefault(team2.r3, 0);

        r11.setBackgroundResource(i11 > i21 ? R.drawable.white_background_10 : android.R.color.transparent);
        r12.setBackgroundResource(i12 > i22 ? R.drawable.white_background_10 : android.R.color.transparent);
        r13.setBackgroundResource(i13 > i23 ? R.drawable.white_background_10 : android.R.color.transparent);

        r21.setBackgroundResource(i11 < i21 ? R.drawable.white_background_10 : android.R.color.transparent);
        r22.setBackgroundResource(i12 < i22 ? R.drawable.white_background_10 : android.R.color.transparent);
        r23.setBackgroundResource(i13 < i23 ? R.drawable.white_background_10 : android.R.color.transparent);
    }

    private void fitTeam(View view, Team team) {
        View gv1 = view.findViewById(R.id.td_rl_gamer_1);
        View gv2 = view.findViewById(R.id.td_rl_gamer_2);

        Gamer g1 =  (team.gamers.size() >0) ? team.gamers.get(0) : new Gamer("-");
        Gamer g2 =  (team.gamers.size() >1) ? team.gamers.get(1) : null;

        fitGamer(gv1, g1);
        fitGamer(gv2, g2);

        TextView r11 = (TextView) view.findViewById(R.id.itl_tv_round_1);
        TextView r12 = (TextView) view.findViewById(R.id.itl_tv_round_2);
        TextView r13 = (TextView) view.findViewById(R.id.itl_tv_round_3);
        TextView c = (TextView) view.findViewById(R.id.itl_tv_count);
        View iv = view.findViewById(R.id.itl_iv_shot);

        r11.setText(team.r1);
        r12.setText(team.r2);
        r13.setText(team.r3);
        c.setText(team.count == null ? "" : String.valueOf(team.count));
        c.setVisibility(team.count == null ? View.GONE : View.VISIBLE);
        iv.setVisibility(team.shot == null || !team.shot ? View.GONE : View.VISIBLE);

    }

    private void fitGamer(View v1, Gamer gamer) {
        if (gamer == null){
            v1.setVisibility(View.GONE);
            return;
        }else{
            v1.setVisibility(View.VISIBLE);
        }
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
