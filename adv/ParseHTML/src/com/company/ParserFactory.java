package com.company;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tretyakov on 27.01.2016.
 */
public class ParserFactory {

    public Match[] parse(String parse) {
        try {
            JSONObject message = new JSONObject(parse);
            String system = message.getString("system");
            String settings = message.getString("settings");
            String data = getHtmlData(message);
            IHtmlParser parser = getProperParser(system, settings);
            Match[] ret = parser.parse(data);
            return ret;
        } catch (Exception ex) {
            return null;
        }
    }

    private class NullParser implements IHtmlParser{
        public NullParser() {
        }
        @Override
        public Match[] parse(String data) {
            return new Match[0];
        }

    }

    private IHtmlParser getProperParser(String data, String settings) {
        if (data.equalsIgnoreCase(SpbOpenDraws.TYPE)) return new SpbOpenDraws(settings);
        if (data.equalsIgnoreCase(SpbOpenTimeTable.TYPE)) return new SpbOpenTimeTable(settings);
        if (data.equalsIgnoreCase(SportCursor.TYPE)) return new SportCursor(settings);
        if (data.equalsIgnoreCase(VseVTennisParser.TYPE)) return new VseVTennisParser(settings);
        return new NullParser();
    }

    private static String getHtmlData(JSONObject message) throws JSONException {
        String data = message.getString("data");
        data = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(data);
        data = data.replaceAll("^\"|\"$", "");
        return data;
    }

    private JSONObject getJsonObject(String filename) throws IOException, JSONException {
        ClassLoader classLoader= ClassLoader.getSystemClassLoader();
        InputStream resource = classLoader.getResourceAsStream(filename);

        String html = readFile(resource);
        html = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(html);
        html = html.replaceAll("^\"|\"$", "");
        return new JSONObject(html);
    }

    static String readFile(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        reader.close();
        return  out.toString();
    }

}
