package com.formulatx.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.formulatx.archived.network.loaders.cursor.ProgramCursor;
import com.formulatx.archived.utils.Log;
import org.json.JSONArray;
import org.json.JSONObject;
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
        public boolean marked;
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
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd LLLL yy", new Locale("ru", "ru"));

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

        mc.moveToFirst();
        return mc;
    }

    private ProgramObject[] getProgram() {
        ProgramObject[] arr;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd h:mm", new Locale("ru", "ru"));

            JSONArray array = new JSONArray(data);
            ArrayList<ProgramObject> apo = new ArrayList<ProgramObject>(array.length()-1);
            ProgramObject marked = new ProgramObject();
            JSONArray arrObj = array.getJSONArray(0);
            try {
                marked.date = arrObj.getString(0);
                marked.time = arrObj.getString(1);
                marked.name = arrObj.getString(2);
                marked.fulldate = formatter.parse(marked.date + " " + marked.time).getTime();
            }catch (Exception ignored){
                Log.w(TAG, "couldn't create marked item. " + ignored.getMessage());
            }

            for (int i = 1; i<array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String date = obj.getString(String.valueOf(i*3+0));
                String time = obj.getString(String.valueOf(i * 3 + 1));
                String name = obj.getString(String.valueOf(i * 3 + 2));
                ProgramObject po = new ProgramObject();
                try{
                    po.fulldate = formatter.parse(date + " " + time).getTime();
                }   catch(ParseException ex){
                    SimpleDateFormat frmt2 = new SimpleDateFormat("dd MMM yyyy h:mm", new Locale("ru", "ru"));
                    po.fulldate = frmt2.parse(date + " " + time).getTime();
                }
                po.time = time;
                po.name = name;
                po.date = date;
                po.marked = po.fulldate == marked.fulldate;
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
