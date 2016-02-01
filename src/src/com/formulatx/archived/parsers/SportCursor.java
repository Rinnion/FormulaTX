package com.formulatx.archived.parsers;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.text.ParseException;

/**
 * Created by tretyakov on 27.01.2016.
 */
public class SportCursor implements IHtmlParser{

    public static final String TYPE = "sportcursor.ru";

    public SportCursor(String settings) {

    }

    @Override
    public Match[] parse(String data) {
        try {
            Document doc = Jsoup.parseBodyFragment(data);

            Element line = getFirstElementByCSS(doc, "body>table>tbody>tr:nth-child(1)>td>table>tbody>tr>td");
            String header = getTextFromElement(line, 0);

            line = getFirstElementByCSS(doc, "body>table>tbody>tr:nth-child(2)");
            Team team1 = getGamer(line);

            line = getFirstElementByCSS(doc, "body>table>tbody>tr:nth-child(3)");
            Team team2 = getGamer(line);

            Match ret = new Match();
            ret.header = header;
            ret.type = "live";
            ret.team1 = team1;
            ret.team2 = team2;
            return new Match[]{ret};

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
            throw new ParseException("No dom element '" + cssQuery + "'", index);
        }
        return elements.get(index);
    }


    private Elements getElementsByCSS(Element doc, String cssQuery) {
        return doc.select(cssQuery);
    }

    private Team getGamer(Element el) throws JSONException, ParseException {
        String name = getFirstElementByCSS(el, "td:nth-child(1)").text();
        String nop = getFirstElementByCSS(el, "td:nth-child(2)").text();
        String extra = getFirstElementByCSS(el, "td:nth-child(3)").text();
        String current = getFirstElementByCSS(el, "td:nth-child(4)").text();
        String r1 = getFirstElementByCSS(el, "td:nth-child(5)").text();
        String r2 = getFirstElementByCSS(el, "td:nth-child(6)").text();
        String r3 = getFirstElementByCSS(el, "td:nth-child(7)").text();

        Team team = new Team();
        team.extra = extra;
        team.gamers.add(new Gamer(name));
        team.count = current;
        team.r1 = r1;
        team.r2 = r2;
        team.r3 = r3;

        return team;
    }
}
