package com.formulatx.archived.fragment.utils;

import android.graphics.Color;
import android.view.View;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.rinnion.archived.R;

/**
 * Created by Lenovo on 03.02.2016.
 */
public final class BackgroundSelector {
    public static void setProperBackground(View view, String type) {
        if (type.equals(TournamentHelper.TOURNAMENT_LADIES_TROPHY)) {
            view.setBackgroundResource(R.drawable.st_lady_bg);
            return;
        }
        if (type.equals(TournamentHelper.TOURNAMENT_OPEN)) {
            view.setBackgroundResource(R.drawable.st_open_bg);
            return;
        }
        view.setBackgroundColor(Color.TRANSPARENT);
    }
}
