package com.formulatx.archived.parsers;

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
public class SpbOpenDrawsDoubles extends SpbOpenDrawsImplementation
{
    public static final String TYPE = "doubles";

    @Override
    protected String getParserType() {
        return TYPE;
    }

    @Override
    public Match[] getArray(String data) {
        try {
            Document doc = Jsoup.parseBodyFragment(data);
            Elements headers = doc.select("body>table>thead>tr>td");

            Elements tabs = doc.select("body>table>tbody>tr>td");

            ArrayList<Match> matches = new ArrayList<Match>();

            for (int i = 0; i < headers.size(); i++) {
                Element hreader = headers.get(i);
                Element tab = tabs.get(i);
                Elements tables = tab.select(">table");
                for (int j = 0; j < tables.size(); j++) {
                    Element table = tables.get(j);
                    Elements trs = table.select("tr");
                    ArrayList<Team> teams = new ArrayList<Team>();
                    for (int k = 0; k < trs.size(); k++) {
                        Element tr = trs.get(k);
                        String extra = tr.select("td>span").get(0).text();
                        List<Node> chIndex = tr.select("td:first-child").get(0).childNodes();
                        String index = (chIndex.get(chIndex.size() - 1) instanceof TextNode) ? ((TextNode) chIndex.get(chIndex.size() - 1)).text() : "";

                        ArrayList<Gamer> gamers = getGamers(tr);

                        String r1 = tr.select("td:nth-child(3)").get(0).text();
                        String r2 = tr.select("td:nth-child(4)").get(0).text();
                        String r3 = tr.select("td:nth-child(5)").get(0).text();

                        Team team = new Team();
                        team.extra = extra;
                        team.gamers = gamers;
                        team.r1 = r1;
                        team.r2 = r2;
                        team.r3 = r3;
                        teams.add(team);
                    }
                    Match match = new Match();
                    match.type = TYPE;
                    match.header = hreader.text();
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

    private static ArrayList<Gamer> getGamers(Element tr) {
        ArrayList<Gamer> gamers = new ArrayList<Gamer>();
        List<Node> chNames = tr.select("td.nm").get(0).childNodes();
        if (chNames.size() == 3){
            gamers.add(new Gamer(((TextNode)chNames.get(0)).text()));
            gamers.add(new Gamer(((TextNode)chNames.get(2)).text()));
        }
        if (chNames.size() == 2){
            if (chNames.get(0) instanceof TextNode){
                gamers.add(new Gamer(((TextNode) chNames.get(0)).text()));
                gamers.add(new Gamer("x"));
            }else{
                gamers.add(new Gamer("x"));
                gamers.add(new Gamer(((TextNode) chNames.get(1)).text()));
            }
        }
        if (chNames.size() < 2){
            gamers.add(new Gamer("x"));
            gamers.add(new Gamer("x"));
        }
        return gamers;
    }
}
