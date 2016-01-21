package com.rinnion.archived.database.model;

import java.io.Serializable;

public class Parser implements Serializable {

    public static final String SPBOPEN_TIMETABLE = "spbopen.ru/timetable";

    public final long id;
    public String title;
    public String date;
    public String data;
    public String system;

    public Parser(long id) {
        this.id=id;
    }

    public Parser(long id, String title, String date, String data, String system) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.data = data;
        this.system = system;
    }

    @Override
    public String toString() {
        return "Parser: " + id;
    }
}
