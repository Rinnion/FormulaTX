package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

/**
* Created by tretyakov on 27.01.2016.
*/
public class SpbOpenDrawsQualification extends SpbOpenDrawsImplementation
{
    @Override
    public Match[] parse(String data){
        return null;
        /*
        try {
            JSONArray ret = getArray(data);
            JSONObject obj = new JSONObject();
            obj.put("qualification", ret);
            return obj;
        }catch(Exception ex){
            throw new IllegalArgumentException("Wrong data", ex);
        }
        */
    }

}
