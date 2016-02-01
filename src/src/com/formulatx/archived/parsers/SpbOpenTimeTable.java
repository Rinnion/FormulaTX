package com.formulatx.archived.parsers;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by tretyakov on 27.01.2016.
 */
public class SpbOpenTimeTable implements IHtmlParser{

    public static final String TYPE = "spbopen.ru/timetable";

    public SpbOpenTimeTable(String settings) {

    }

    @Override
    public Match[] parse(String data) {

        try {
            Document doc = Jsoup.parseBodyFragment(data);

            Elements tables = getElementsByCSS(doc, "table");
            ArrayList<Match> matches = new ArrayList<Match>();
            for (int i = 0; i <tables.size(); i++) {
                Element table = tables.get(i);
                if (getElementsByCSS(table, ">tbody>tr>td[class=fio]").size() == 0) continue;

                Element item;
                item = getFirstElementByCSS(table, ">tbody>tr:nth-child(1)");
                Team team1 = getGamer(item);

                item = getFirstElementByCSS(table, ">tbody>tr:nth-child(2)");
                Team team2 = getGamer(item);

                Match match = new Match();
                match.team1 = team1;
                match.team2 = team2;
                match.type = "live";
                matches.add(match);


            }

            return matches.toArray(new Match[matches.size()]);

        } catch (Exception e) {
            throw new IllegalArgumentException("wrong data",e);
        }
    }



    private String getTextFromElement(Element line, int index) throws ParseException {
        String value;
        if(line.childNode(index) instanceof TextNode) value = ((TextNode)line.childNode(index)).text();
        else throw new ParseException("Node '" + line.cssSelector() + "' hasn't text node at" + index, 0);
        return value;
    }

    private String getTextFromElement(Element line, int index, String def) throws ParseException {
        return line.childNode(index) instanceof TextNode ? ((TextNode) line.childNode(index)).text() : def;
    }

    private Element getFirstElementByCSS(Element doc, String cssQuery) throws ParseException {
        return getElementByCSS(doc, cssQuery, 0);
    }

    private Element getElementByCSS(Element doc, String cssQuery, int index) throws ParseException {
        Elements elements = getElementsByCSS(doc, cssQuery);
        if (elements.size() < index+1) {
            throw new ParseException("No dom element '" + doc.cssSelector() + " > " + cssQuery + "'", index);
        }
        return elements.get(index);
    }


    private Elements getElementsByCSS(Element doc, String cssQuery) {
        return doc.select(cssQuery);
    }

    private Team getGamer(Element el) throws JSONException, ParseException {
        String extra = getFirstElementByCSS(el, "td:nth-child(1)").text();
        String photo = getFirstElementByCSS(el, "td:nth-child(2) img").attr("src");
        String name = getFirstElementByCSS(el, "td:nth-child(3)").text();
        String cc = getFirstElementByCSS(el, "td:nth-child(4)").attr("src");

        String bool = getAttributeFromFirstElementByCSS(el, "td:nth-child(5)");
        String r1 = getTextFromFirstElementByCSS(el, "td:nth-child(6)");
        String r2 = getTextFromFirstElementByCSS(el, "td:nth-child(7)");
        String r3 = getTextFromFirstElementByCSS(el, "td:nth-child(8)");

        Team team = new Team();
        team.extra = extra;
        team.gamers.add(new Gamer(name, cc, photo));
        team.shot = Boolean.TRUE.toString().equalsIgnoreCase(bool);
        team.r1 = r1;
        team.r2 = r2;
        team.r3 = r3;
        return team;
    }

    private String getAttributeFromFirstElementByCSS(Element line, String cssQuery) {
        Elements node5 = getElementsByCSS(line, cssQuery);
        return (node5.size() == 1) ? node5.get(0).attr("class") : null;
    }

    private String getTextFromFirstElementByCSS(Element line, String cssQuery) {
        Elements node5 = getElementsByCSS(line, cssQuery);
        return (node5.size() == 1) ? node5.get(0).text() : null;
    }
}
