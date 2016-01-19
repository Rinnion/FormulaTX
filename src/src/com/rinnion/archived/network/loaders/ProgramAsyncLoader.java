package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.fragment.ProgramCursor;
import com.rinnion.archived.utils.Log;

/**
* Created by tretyakov on 18.01.2016.
*/
public class ProgramAsyncLoader extends AsyncTaskLoader<ProgramCursor> {

    private String TAG = getClass().getSimpleName();

    public ProgramAsyncLoader(Context context) {
        super(context);
        Log.d(TAG, ".ctor");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        //deliverResult(getProgramCursor());
    }


    @Override
    public ProgramCursor loadInBackground() {
        ProgramCursor mc = getProgramCursor();
        return mc;
    }

    private ProgramCursor getProgramCursor() {
        ProgramCursor mc = new ProgramCursor();
        mc.addRow(ProgramCursor.TYPE_DAY, "Пн, 11 янв 16", null, true, false);
        mc.addRow(ProgramCursor.TYPE_EVT, "Начало", "12:00", true);
        mc.addRow(ProgramCursor.TYPE_EVT, "Презентация", "23:00", true);
        mc.addRow(ProgramCursor.TYPE_DAY, "Вт, 12 янв 16", null, true, true);
        mc.addRow(ProgramCursor.TYPE_EVT, "Нало продолжения", "8:30", false);
        mc.addRow(ProgramCursor.TYPE_DAY, "Ср, 13 янв 16", null, false, false);
        mc.addRow(ProgramCursor.TYPE_EVT, "Завершение", "9:00", false);
        mc.addRow(ProgramCursor.TYPE_EVT, "Кофе пауза", "12:00", false);
        mc.addRow(ProgramCursor.TYPE_DAY, "Чт, 14 янв 16", null, false, false);
        mc.addRow(ProgramCursor.TYPE_EVT, "Начало", "12:00", false);
        mc.addRow(ProgramCursor.TYPE_EVT, "Презентация", "23:00", false);
        mc.addRow(ProgramCursor.TYPE_DAY, "Пт, 15 янв 16", null, false, false);
        mc.addRow(ProgramCursor.TYPE_EVT, "Нало продолжения", "8:30", false);
        mc.addRow(ProgramCursor.TYPE_DAY, "Cб, 16 янв 16", null, false, false);
        mc.addRow(ProgramCursor.TYPE_EVT, "Завершение", "9:00", false);
        mc.addRow(ProgramCursor.TYPE_EVT, "Кофе пауза", "12:00", false);
        mc.moveToFirst();
        return mc;
    }


}
