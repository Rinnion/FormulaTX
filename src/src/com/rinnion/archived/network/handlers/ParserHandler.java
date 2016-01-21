package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Utils;
import com.rinnion.archived.database.cursor.GalleryDescriptionCursor;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.database.helper.ParserHelper;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.database.model.Parser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tretyakov on 07.07.2015.
 */
public class ParserHandler extends FormulaTXObjectResponseHandler {

    @Override
    public Bundle onTrueStatus(JSONObject message, Bundle bundle) throws JSONException {
        ParserHelper ph = new ParserHelper(ArchivedApplication.getDatabaseOpenHelper());
        long id = message.getLong("id");
        String title = message.getString("title");
        String date = message.getString("date");
        String data = message.getString("data");
        String system = message.getString("system");

        Parser parser = new Parser(id, title, date, data, system);
        ph.merge(parser);

        return bundle;
    }

    @Override
    public Bundle onErrorStatus(JSONObject message, Bundle bundle) {
        return Bundle.EMPTY;
    }


}
