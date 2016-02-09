package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.AreaHelper;
import com.formulatx.archived.database.model.ApiObject;
import com.formulatx.archived.database.model.ApiObjects.Area;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by alekseev on 29.12.2015.
 */

public class AreaHandler extends JSONObjectHandler {

    private AreaHelper th;

    public AreaHandler(){
        this.th = new AreaHelper(FormulaTXApplication.getDatabaseOpenHelper());
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");

            Area ao = null;
            for (int i = 0; i < message.length(); i++) {
                JSONObject obj = (JSONObject) message.get(i);
                String id = obj.getString("post_id");
                String key = obj.getString("key");
                String value = obj.getString("value");

                if (ao == null) ao = th.getArea(Long.parseLong(id));
                if (ao == null) ao = new Area(Long.parseLong(id));
                if (key.equals("Adress")) {
                    ao.address = value;
                }
                if (key.equals("Maps")) {
                    ao.map = value;
                }
            }

            if (ao == null) return Bundle.EMPTY;

            ApiObjectHelper aoh = new ApiObjectHelper(FormulaTXApplication.getDatabaseOpenHelper());
            ApiObject apiObject = aoh.get(ao.id);
            ao.title = apiObject.title;
            ao.content = getContent(apiObject);

            th.merge(ao);

            return super.Handle(object);
        }
        return Bundle.EMPTY;
    }

    private String getContent(ApiObject apiObject) throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><style>");
        sb.append("body { color:white; };");
        sb.append("body table:first-child strong { font-size:14pt; }");
        sb.append("body table:first-child strong { font-size:14pt; }");
        sb.append("body table:first-child { font-size:12pt; }");
        sb.append("body table:last-child { width:100%; font-size:12pt;}");
        sb.append("body table:last-child td, body table:last-child th { height:2em; border-radius:5px; padding:5px; text-align:center;}");
        sb.append("body table:last-child tr.odd td{ background-color:rgba(255,255,255,0.4); }");
        sb.append("body table:last-child tr.even td{ background-color:rgba(255,255,255,0.2); }");
        sb.append("body table:last-child thead th{ font-size:10pt; background-color:rgba(255,255,255,0.4); }");
        sb.append("body table:last-child tbody td{ font-size:12pt; font-weight:bolder; }");
        sb.append("</style></head><body>");
        sb.append(apiObject.content);

        JSONArray table = new JSONArray(apiObject.tables);

        if (table.length() > 0) {

            JSONArray names = table.getJSONArray(0);
            if (names.length() == 2) {
                sb.append("<table>");
                sb.append("<thead><tr><th>");
                sb.append(names.get(0));
                sb.append("</th><th>");
                sb.append(names.get(1));
                sb.append("</th></tr></thead>");

                sb.append("<tbody>");

                for (int i = 1; i < table.length(); i++) {
                    JSONObject item = table.getJSONObject(i);
                    sb.append("<tr class='").append((i % 2 == 0) ? "even" : "odd").append("'><td align='left'>");
                    sb.append(item.getString(String.valueOf(i * 2)));
                    sb.append("</td>");
                    sb.append("<td align='right'>");
                    sb.append(item.getString(String.valueOf(i * 2 + 1)));
                    sb.append("</td></tr>");
                }

                sb.append("</tbody>");

                sb.append("</table>");
            }
        }
        sb.append("</body></html>");
        return sb.toString();
    }
}


