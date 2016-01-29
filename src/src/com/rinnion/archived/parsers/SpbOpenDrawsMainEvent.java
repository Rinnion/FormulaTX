package com.rinnion.archived.parsers;

/**
* Created by tretyakov on 27.01.2016.
*/
public class SpbOpenDrawsMainEvent extends SpbOpenDrawsImplementation
{
    public static final String TYPE = "main_event";

    @Override
    protected String getParserType() {
        return TYPE;
    }
}
