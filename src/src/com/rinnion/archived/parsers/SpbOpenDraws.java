package com.rinnion.archived.parsers;

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
public class SpbOpenDraws implements IHtmlParser{

    public static final String TYPE = "spbopen.ru/draws";

    IHtmlParser implementation;

    public SpbOpenDraws(String settings) {
        if (settings.equalsIgnoreCase("qualification")) {
            implementation = new SpbOpenDrawsQualification();
        }
        if (settings.equalsIgnoreCase("main_event")) {
            implementation = new SpbOpenDrawsMainEvent();
        }
        if (settings.equalsIgnoreCase("doubles")) {
            implementation = new SpbOpenDrawsDoubles();
        }
        implementation = new SpbOpenDrawsMainEvent();
    }

    @Override
    public Match[] parse(String data) {
        return implementation.parse(data);
    }

}
