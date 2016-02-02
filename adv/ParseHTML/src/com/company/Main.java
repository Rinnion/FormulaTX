package com.company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

public class Main {

    private static ParserFactory pf;

    public static void main(String[] args) throws IOException, JSONException {

        String html;
        String data;
        JSONObject ret;

        //ClassLoader classLoader=ClassLoader.getSystemClassLoader();
        //InputStream resource = classLoader.getResourceAsStream(filename);

        pf = new ParserFactory();
        parseFile("parsers-48.json");

        Match m = new Match();
        m.header="header";
        m.type="type";
        m.team1=new Team();
        m.team2=new Team();

        m.team1.count="15";
        m.team1.extra="WC";
        m.team1.r1="6";
        m.team1.r2="3";
        m.team1.r3="0";
        m.team1.shot=true;
        m.team1.gamers.add(new Gamer("Name1", "cc", "link"));
        m.team1.gamers.add(new Gamer("Name2", "cc", "link"));

        m.team2.count="40";
        m.team2.extra="";
        m.team2.r1="3";
        m.team2.r2="4";
        m.team2.r3="0";
        m.team2.shot=false;
        m.team2.gamers.add(new Gamer("t21", "cc", "link"));
        m.team2.gamers.add(new Gamer("t22", "cc", "link"));

        System.out.println(m.getJSONObject());

        //parseFile("parsers-20.json");
        //parseFile("parsers-22.json");
        //parseFile("parsers-23.json");
        //parse = pf.parse("parsers-19.json"); System.out.println(parse);
        //parse = pf.parse("parsers-20.json"); System.out.println(parse);
        //parse = pf.parse("parsers-22.json"); System.out.println(parse);
        //parse = pf.parse("parsers-23.json"); System.out.println(parse);
        //parse = pf.parse("parsers-35.json"); System.out.println(parse);
        //parse = pf.parse("parsers-37.json"); System.out.println(parse);
        //parse = pf.parse("parsers-46.json"); System.out.println(parse);
        //parse = pf.parse("parsers-47.json"); System.out.println(parse);
        //parse = pf.parse("parsers-48.json"); System.out.println(parse);



        /*
        html = getJsonString("parsers-46.json");
        data = getHtmlData(html);
        ret = new SpbOpenDrawsQualification().parse(data);
        System.out.println(ret.toString());
        */
        /*
        html = getJsonString("parsers-47.json");
        data = getHtmlData(html);
        ret = new SpbOpenDrawsMainEvent().parse(data);
        System.out.println(ret.toString());
        */
        /*
        html = getJsonString("parsers-48.json");
        data = getHtmlData(html);
        ret = new SpbOpenDrawsDoubles().parse(data);
        System.out.println(ret.toString());
        */
        /*
        html = getJsonString("parsers-19.json");
        data = getHtmlData(html);
        ret = new VseVTennisParser().parse(data);
        System.out.println(ret.toString());
        */
        /*
        html = getJsonString("parsers-20.json");
        data = getHtmlData(html);
        ret = new SportCursor().parse(data);
        System.out.println(ret.toString());
        */
        /*
        html = getJsonString("parsers-35.json");
        data = getHtmlData(html);
        ret = new SportCursor().parse(data);
        System.out.println(ret.toString());
        */
        /*
        html = getJsonString("parsers-22.json");
        data = getHtmlData(html);
        ret = new SpbOpenDrawsMainEvent().parse(data);
        System.out.println(ret.toString());
        */

        /*
        html = getJsonString("parsers-23.json");
        data = getHtmlData(html);
        ret = new SpbOpenTimeTable().parse(data);
        System.out.println(ret.toString());
        */
    }

    private static void parseFile(String s) {

        Match[] parse = pf.parse(s);

        for (Match m :parse){
            try {
                System.out.println(m.getJSONObject().toString());
            } catch (JSONException e) {
                System.out.println("error");
            }
        }
        System.out.println("");
    }


}
