package com.formulatx.archived.database.model;

import com.formulatx.archived.Settings;

import java.io.Serializable;
import java.util.Calendar;

public class Parser implements Serializable {

    public static final String SPBOPEN_TIMETABLE = "spbopen.ru/timetable";
    public static final String SPBOPEN_DRAWS= "spbopen.ru/draws";
    public static final String SPBOPEN_DRAWS_QUALIFICATION= "qualification";
    public static final String SPBOPEN_DRAWS_MAIN_EVENT= "main_event";
    public static final String SPBOPEN_DRAWS_DOUBLES= "doubles";

    public final long id;
    public String title;
    public String date;
    public String data;
    public String system;
    public String settings;
    public long downloaded;
    public long parsed;

    public Parser(long id) {
        this.id=id;
    }

    public Parser(long id, String title, String date, String data, String system, String settings, long downloaded) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.data = data;
        this.system = system;
        this.settings = settings;
        this.downloaded = downloaded;
        parsed = 0;
    }

    public Parser(long id, String title, String date, String data, String system, String settings, long downloaded, long parsed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.data = data;
        this.system = system;
        this.settings = settings;
        this.downloaded = downloaded;
        this.parsed = parsed;
    }

    @Override
    public String toString() {
        return "Parser: " + id;
    }

    public boolean isOutOfDate() {
        return Calendar.getInstance().getTimeInMillis() > downloaded + Settings.OUT_DATE_TIME_QUERY_PARSER_ID;

    }
    public boolean isOutOfDateParsed() {
        return Calendar.getInstance().getTimeInMillis() > parsed + Settings.OUT_DATE_TIME_QUERY_PARSER_ID;

    }
}
