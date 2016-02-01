package com.formulatx.archived.parsers;

/**
 * Created by tretyakov on 27.01.2016.
 */
public class SpbOpenDraws implements IHtmlParser{

    public static final String TYPE = "spbopen.ru/draws";

    IHtmlParser implementation;

    public SpbOpenDraws(String settings) {
        implementation = new SpbOpenDrawsMainEvent();
        if (settings.equalsIgnoreCase(SpbOpenDrawsQualification.TYPE)) {
            implementation = new SpbOpenDrawsQualification();
        }
        if (settings.equalsIgnoreCase(SpbOpenDrawsMainEvent.TYPE)) {
            implementation = new SpbOpenDrawsMainEvent();
        }
        if (settings.equalsIgnoreCase(SpbOpenDrawsDoubles.TYPE)) {
            implementation = new SpbOpenDrawsDoubles();
        }
    }

    @Override
    public Match[] parse(String data) {
        return implementation.parse(data);
    }

}
