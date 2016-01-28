package com.company;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by tretyakov on 27.01.2016.
 */
public abstract class SpbOpenDrawsImplementation implements IHtmlParser{
    public JSONArray getArray(String data) {
        try {
            Document doc = Jsoup.parseBodyFragment(data);
            Elements headers = doc.select("body>table>thead>tr>td");

            Elements tabs = doc.select("body>table>tbody>tr>td");

            JSONArray ret = new JSONArray();

            for (int i = 0; i < headers.size(); i++) {
                Element hreader = headers.get(i);
                JSONObject retObject = new JSONObject();
                retObject.put("Name", hreader.text());
                Element tab = tabs.get(i);
                Elements tables = tab.select("table:nth-child(" + (i + 1)+")");
                JSONArray gamers = new JSONArray();
                for (int j = 0; j < tables.size(); j++) {
                    Element table = tables.get(j);
                    Elements trs = table.select("tr");
                    for (int k = 0; k < trs.size(); k++) {
                        Element tr = trs.get(k);
                        String extra = tr.select("td>span").get(0).text();
                        List<Node> chIndex = tr.select("td:first-child").get(0).childNodes();
                        String index = (chIndex.get(chIndex.size() - 1) instanceof TextNode) ? ((TextNode) chIndex.get(chIndex.size() - 1)).text() : "";
                        String name = tr.select("td.nm").get(0).text();
                        String r1 = tr.select("td:nth-child(3)").get(0).text();
                        String r2 = tr.select("td:nth-child(4)").get(0).text();
                        String r3 = tr.select("td:nth-child(5)").get(0).text();
                        JSONObject gamer = new JSONObject();
                        gamer.put("extra", extra);
                        gamer.put("index", index);
                        gamer.put("name", name);
                        gamer.put("r1", r1);
                        gamer.put("r2", r2);
                        gamer.put("r3", r3);
                        gamers.put(gamer);
                    }
                }

                retObject.put("Gamers", gamers);
                ret.put(retObject);
            }
            return ret;
        }catch(Exception ex){
            throw new IllegalArgumentException("Wrong data", ex);
        }
    }

}
