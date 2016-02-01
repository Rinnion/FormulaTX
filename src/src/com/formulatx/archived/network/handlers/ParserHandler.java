package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.ParserHelper;
import com.formulatx.archived.database.model.Parser;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tretyakov on 07.07.2015.
 */
public class ParserHandler extends FormulaTXObjectResponseHandler {

    @Override
    public Bundle onTrueStatus(JSONObject message, Bundle bundle) throws JSONException {
        ParserHelper ph = new ParserHelper(FormulaTXApplication.getDatabaseOpenHelper());
        long id = message.getLong("id");
        String title = message.getString("title");
        String date = message.getString("date");
        String data = message.getString("data");
        String system = message.getString("system");
        String settings = message.getString("settings");

        Parser parser = new Parser(id, title, date, message.toString(), system, settings);
        ph.merge(parser);

        return bundle;
    }

    @Override
    public Bundle onErrorStatus(JSONObject message, Bundle bundle) {
        return Bundle.EMPTY;
    }


}
