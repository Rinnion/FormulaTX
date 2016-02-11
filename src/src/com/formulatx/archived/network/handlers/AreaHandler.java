package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.AreaHelper;
import com.formulatx.archived.database.model.ApiObjects.AreaOnline;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alekseev on 29.12.2015.
 */

public class AreaHandler extends JSONObjectHandler {

    public static final String AREAS_ARRAY = "areas";
    private AreaHelper th;

    public AreaHandler(){
        this.th = new AreaHelper(FormulaTXApplication.getDatabaseOpenHelper());
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONObject message = object.getJSONObject("message");

            ArrayList<AreaOnline> areas = new ArrayList<AreaOnline>();
            for (int i = 0; i < message.length(); i++) {
                if (!message.has(String.valueOf(i))) continue;
                JSONObject obj = (JSONObject) message.get(String.valueOf(i));


                JSONObject tables = obj.getJSONObject("tables");
                String content = obj.getString("content");
                String address = obj.getString("adress");
                String title = obj.getString("title");
                String content_short = obj.getString("content_short");
                String maps = obj.getString("maps");
                String nameroute = obj.getString("nameroute");

                AreaOnline ao = new AreaOnline();
                ao.content = getContent(tables, content);
                ao.address = address;
                ao.title = title;
                ao.content_short = content_short;
                ao.maps = maps;
                ao.nameroute = nameroute;

                areas.add(ao);
            }

            Bundle bundle = super.Handle(object);
            bundle.putSerializable(AREAS_ARRAY, areas.toArray(new AreaOnline[areas.size()]));

            return bundle;
        }
        return Bundle.EMPTY;
    }

    private String getContent(JSONObject tables, String content) throws JSONException {
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
        sb.append(content);

        if (tables.length() > 0) {

            JSONObject names = tables.getJSONObject("0");
            if (names.length() == 2) {
                sb.append("<table>");
                sb.append("<thead><tr><th>");
                sb.append(names.get("0"));
                sb.append("</th><th>");
                sb.append(names.get("1"));
                sb.append("</th></tr></thead>");

                sb.append("<tbody>");

                for (int i = 1; i < tables.length(); i++) {
                    if (!tables.has(String.valueOf(i))) continue;
                    JSONObject item = tables.getJSONObject(String.valueOf(i));
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


