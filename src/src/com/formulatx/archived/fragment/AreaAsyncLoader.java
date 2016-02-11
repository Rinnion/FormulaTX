package com.formulatx.archived.fragment;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.MatrixCursor;
import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Settings;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.GalleryHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.database.model.ApiObjects.Area;
import com.formulatx.archived.database.model.ApiObjects.AreaOnline;
import com.formulatx.archived.network.HttpRequester;
import com.formulatx.archived.network.MyNetwork;
import com.formulatx.archived.network.handlers.AreaHandler;
import org.apache.http.protocol.HTTP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Lenovo on 11.02.2016.
 */
public class AreaAsyncLoader extends AsyncTaskLoader<AreaOnline[]> {

    private final long id;

    public AreaAsyncLoader(Context context, long id) {
        super(context);
        this.id = id;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    public AreaOnline[] loadInBackground() {

        String parameter = "AreaAsyncLoader-" + id;
        String parameter_result = parameter + "-result";

        long last_time = FormulaTXApplication.getLongParameter(parameter, 0);
        Bundle bundle = FormulaTXApplication.getBundleParameter(parameter_result, null);
        if (bundle == null || last_time + Settings.OUT_OF_DATE_LOADER_AREAS < Calendar.getInstance().getTimeInMillis() ) {
            FormulaTXApplication.setParameter(parameter, Calendar.getInstance().getTimeInMillis());
            bundle = MyNetwork.queryAreas(id);
        }

        if (bundle == null) return new AreaOnline[0];
        String result = bundle.getString(HttpRequester.RESULT);
        if (result == null || !result.equals(HttpRequester.RESULT_HTTP)) return new AreaOnline[0];

        FormulaTXApplication.setParameter(parameter_result, bundle);

        Bundle http = bundle.getBundle(HttpRequester.RESULT_HTTP);
        if (http == null) return new AreaOnline[0];
        Bundle parse = http.getBundle(HttpRequester.RESULT_HTTP_PARSE);
        if (parse == null) return null;
        Serializable ser = parse.getSerializable(AreaHandler.AREAS_ARRAY);
        if (ser instanceof AreaOnline[]) return (AreaOnline[])ser;
        Object[] areas = (Object[])(ser);
        AreaOnline[] res = new AreaOnline[areas.length];
        for (int i = 0; i<areas.length;i++){
            res[i] = (AreaOnline)areas[i];
        }
        return res;

    }
}
