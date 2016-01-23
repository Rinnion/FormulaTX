package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.fragment.ProgramCursor;
import com.rinnion.archived.fragment.ProgramFragment;
import com.rinnion.archived.utils.Log;
import org.lorecraft.phparser.SerializedPhpParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.*;

/**
* Created by tretyakov on 18.01.2016.
*/
public class ProgramAsyncLoader extends AsyncTaskLoader<ProgramCursor> {

    private final String data;
    private String TAG = getClass().getSimpleName();

    public ProgramAsyncLoader(Context context, String data) {
        super(context);
        this.data = data;
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

    class ProgramObject{
        public long fulldate;
        public String date;
        public String time;
        public String name;
    }

    private ProgramCursor getProgramCursor() {

        ProgramObject[] arr = getProgram();
        if (arr == null||arr.length==0) return null;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long curDate = cal.getTimeInMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMMMM yy");

        ProgramCursor mc = new ProgramCursor();
        int i = 0;
        while (i < arr.length){
            ProgramObject po = arr[i];
            String date = po.date;

            mc.addRow(ProgramCursor.TYPE_DAY, formatter.format(po.fulldate), null, curDate > po.fulldate, curDate == po.fulldate);
            do{
                ProgramObject in = arr[i];
                mc.addRow(ProgramCursor.TYPE_EVT, in.name, in.time, curDate > in.fulldate);
            }while (++i < arr.length && arr[i].date.equals(date));
        }


        /*ProgramCursor mc = new ProgramCursor();
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
        */
        mc.moveToFirst();
        return mc;
    }

    private ProgramObject[] getProgram() {
        ProgramObject[] arr;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy h:mm", new Locale("ru", "RU"));
            SerializedPhpParser php = new SerializedPhpParser(data);
            Map parse = (Map) php.parse();
            ArrayList<ProgramObject> apo = new ArrayList<ProgramObject>(parse.size());
            for (Object item : parse.keySet()) {
                Object[] event = ((LinkedHashMap) (parse.get(item))).values().toArray();
                String date = event[0].toString();
                String time = event[1].toString();
                String name = event[2].toString();
                ProgramObject po = new ProgramObject();
                try{
                    po.fulldate = formatter.parse(date + " " + time).getTime();
                }   catch(ParseException ex){
                    SimpleDateFormat frmt2 = new SimpleDateFormat("dd MMMM yyyy h:mm", new Locale("ru", "RU"));
                    po.fulldate = frmt2.parse(date + " " + time).getTime();
                }
                po.time = time;
                po.name = name;
                po.date = date;
                apo.add(po);
            }
            arr = apo.toArray(new ProgramObject[apo.size()]);
            sort(arr, new Comparator<ProgramObject>() {
                @Override
                public int compare(ProgramObject po1, ProgramObject po2) {
                    if (po1.fulldate > po2.fulldate) return 1;
                    if (po1.fulldate < po2.fulldate) return -1;
                    return 0;
                }
            });
            return arr;

        }catch(Exception ex){
            Log.e(TAG, "Cannot parse program", ex);
            return null;
        }
    }


}
