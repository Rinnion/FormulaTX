package com.company;

/**
* Created by tretyakov on 27.01.2016.
*/
public class SpbOpenDrawsQualification extends SpbOpenDrawsImplementation
{
    public static final String TYPE = "qualification";

    @Override
    protected String getParserType() {
        return TYPE;
    }
}
