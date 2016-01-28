package com.rinnion.archived.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

/**
 * Created by tretyakov on 27.01.2016.
 */
public class VseVTennisParser implements IHtmlParser{

    public static final String TYPE = "vsevtennis.ru";

    public VseVTennisParser(String settings) {

    }

    @Override
    public Match[] parse(String data) {
        try {
            Document doc = Jsoup.parseBodyFragment(data);

            JSONArray gamers = new JSONArray();
            JSONObject gamer;
            Element line;

            line = doc.select("div.first-line").get(0);
            String header = (line.childNode(0) instanceof TextNode) ? ((TextNode) line.childNode(0)).text() : "";
            Team team1 = getGamer(line);;

            line = doc.select("div.second-line").get(0);
            Team team2 = getGamer(line);;

            Match ret = new Match();
            ret.header = header;
            ret.type = "live";
            ret.team1 = team1;
            ret.team2 = team2;
            return new Match[]{ret};

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Team getGamer(Element el) throws JSONException {
        String extra = el.select("ul>li:nth-child(1)").get(0).text();
        String name = el.select("ul>li:nth-child(2)").get(0).text();
        String current = el.select("ul>li:nth-child(3)").get(0).text();
        String r1 = el.select("ul>li:nth-child(4)").get(0).text();
        String r2 = el.select("ul>li:nth-child(5)").get(0).text();
        String r3 = el.select("ul>li:nth-child(6)").get(0).text();

        Team team = new Team();
        team.extra=extra;
        team.gamers.add(new Gamer(name));
        team.count=current;
        team.r1=r1;
        team.r2=r2;
        team.r3=r3;

        return team;
    }
}
