package com.company;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tretyakov on 27.01.2016.
 */
public abstract class SpbOpenDrawsImplementation implements IHtmlParser{

    @Override
    public final Match[] parse(String data) {
        try {
            return getArray(data);
        }catch(Exception ex){
            throw new IllegalArgumentException("Wrong data", ex);
        }
    }

    protected abstract String getParserType();

    public Match[] getArray(String data) {
        try {
            Document doc = Jsoup.parseBodyFragment(data);
            Elements headers = doc.select("body>table>thead>tr>td");

            Elements tabs = doc.select("body>table>tbody>tr>td");


            ArrayList<Match> matches = new ArrayList<Match>();
            for (int i = 0; i < headers.size(); i++) {
                Element header = headers.get(i);
                JSONObject retObject = new JSONObject();
                retObject.put("Name", header.text());
                Element tab = tabs.get(i);
                Elements tables = tab.select("table");
                for (int j = 0; j < tables.size(); j++) {
                    Element table = tables.get(j);
                    Elements trs = table.select("tr");
                    ArrayList<Team> teams = new ArrayList<Team>();
                    for (int k = 0; k < trs.size(); k++) {
                        Element tr = trs.get(k);
                        String extra = tr.select("td>span").get(0).text();
                        List<Node> chIndex = tr.select("td:first-child").get(0).childNodes();
                        String index = (chIndex.get(chIndex.size() - 1) instanceof TextNode) ? ((TextNode) chIndex.get(chIndex.size() - 1)).text() : "";
                        String name = tr.select("td.nm").get(0).text();
                        String r1 = tr.select("td:nth-child(3)").get(0).text();
                        String r2 = tr.select("td:nth-child(4)").get(0).text();
                        String r3 = tr.select("td:nth-child(5)").get(0).text();
                        Team team = new Team();
                        team.extra=extra;
                        team.gamers.add(new Gamer(name));
                        team.r1 = r1;
                        team.r2 = r2;
                        team.r3 = r3;
                        teams.add(team);
                    }

                    Match match = new Match();
                    match.type = getParserType();
                    match.header = header.text();
                    match.team1 = (teams.size() > 0) ? teams.get(0) : null;
                    match.team2 = (teams.size() > 1) ? teams.get(1) : null;
                    match.team3 = (teams.size() > 2) ? teams.get(2) : null;
                    match.team4 = (teams.size() > 3) ? teams.get(3) : null;
                    matches.add(match);
                }
            }
            return matches.toArray(new Match[matches.size()]);
        }catch(Exception ex){
            throw new IllegalArgumentException("Wrong data", ex);
        }
    }

}
