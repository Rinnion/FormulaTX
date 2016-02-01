package com.formulatx.archived.network.loaders.cursor;

/**
* Created by tretyakov on 25.01.2016.
*/
public class WeatherCursor {
    public static String MOSCOW = "moscow";
    public static String PETERSBURG = "petersburg";

    public class City{
        public String main;
        public String temp;
        public int icon;
    }

    public City Peter;
    public City Moscow;

    public WeatherCursor(){
        Peter = new City();
        Moscow = new City();
    }
}
