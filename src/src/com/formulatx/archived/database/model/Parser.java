package com.formulatx.archived.database.model;

import java.io.Serializable;

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

    public Parser(long id) {
        this.id=id;
    }

    public Parser(long id, String title, String date, String data, String system, String settings) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.data = data;
        this.system = system;
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "Parser: " + id;
    }
}
