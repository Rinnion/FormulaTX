package com.rinnion.archived.utils;

import java.util.Locale;

/**
 * Created by alekseev on 12.01.2016.
 */
public class MyLocale {
    public static final String  TAG="MyLocale";
    public static final String  EN_LOCALE_ENG="en";
    public static final String  EN_LOCALE_RUS="ru";

    private static String mCurLocale;

    public static String getCurrent()
    {
        return mCurLocale.toLowerCase();
    }

    public static void Initialize()
    {
        mCurLocale= Locale.getDefault().getLanguage().toLowerCase();


        if((mCurLocale.equals(EN_LOCALE_RUS))||
        (mCurLocale.equals(EN_LOCALE_ENG))){
            Log.d(TAG,"Locale success detected == \"" + mCurLocale + "\"");
            return;
        }

        Log.d("TAG","Locale == \"" + mCurLocale + "\" not supported.");
        mCurLocale=EN_LOCALE_RUS;

        Log.d(TAG,"Set default locale == \"" + mCurLocale + "\"");

    }



}
