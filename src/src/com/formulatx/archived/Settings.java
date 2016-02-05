package com.formulatx.archived;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 19.12.13
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public final class Settings {
    public static final boolean NETDEBUG =false;
    public static final boolean DEBUG = false;

    public static final int ABOUT_API_OBJECT = 201;
    public static final String ABOUT_API_OBJECT_ALIAS = "about";

    public static final String USER_ID = "UserId";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_AVATAR = "USER_AVATAR";
    public static final String CREDENTIALS = "Credentials"; //Authorization token
    public static final String CREDENTIALS_MESSAGE = "CREDENTIALS_MESSAGE"; //Authorization token for send message anonymously

    public static final String USER_LATITUDE = "UserLatitude";
    public static final String USER_LONGITUDE = "UserLongitude";
    public static final String OUT_DATE_TIME_SEARCH = "SearchOrderTimeOutDate";
    public static final String OUT_DATE_TIME_OWN = "OwnOrderTimeOutDate";
    public static final String OUT_DATE_TIME_ASSIGNED = "AssignedOrderTimeOutDate";
    public static final String USER_LOGIN = "Login";
    public static final String LAST_APPROVAL_CODE = "Last approval code";
    public static final String SHOW_ROLE_TOOLTIP = "show start screen";
    public static final String MY_ROLE = "my role";
    public static final String CUSTOMER = "customer";
    public static final String COURIER = "courier";


    public static final String SELECTED_FILTER = "selected filter";
    public static final int OUT_DATE_TIME_SEARCH_DEFAULT = 5 * 60 * 1000; //5 minuts
    public static final int OUT_DATE_TIME_OWN_DEFAULT = 5 * 24 * 60 * 60 * 1000; //5 days
    public static final int OUT_DATE_TIME_ASSIGNED_DEFAULT = 24 * 60 * 60 * 1000; // 1 day
    public static final long UPDATE_TIME = 60000; //60 sec
    public static final int REFRESH_AUDION_POSITION_TIME=500;
    public static final String VERSION_NUMBER = "0.9.0.0201";
    public static final String VERSION_BUILD_TIME = "2237";
    public static final String VERSION = "v" + VERSION_NUMBER + "." + VERSION_BUILD_TIME + (NETDEBUG ? "n" : "") + (DEBUG ? "d" : "");
    public static final String LOW_COMMENT_IDENTIFIER = "LOW_COMMENT_IDENTIFIER";

    public static final String LOADING_TYPE = "preloading_type";
    public static final String LOADING_PROGRESS = "preloading_progress";
    public static final String LOADING_ERROR = "preloading_error";
    public static final String LOADING_CUSTOM_MESSAGE = "preloading_custom_message";

    public static final long OUT_DATE_TIME_QUERY_PARSER_ID = 24 * 60 * 60 * 1000; //one day
    public static final long OUT_DATE_TIME_QUERY_PARSER_PARSED = 1 * 60 * 60 * 1000; //one hour
    public static final String GRIDS = "grid";

    public static String COLLECT_GPS_DATA = "CollectGPSData";
    public static String SYNC_TO_SERVER = "SyncToServer";
    public static final String EXTERNAL_PATH="FormulaTX";
}

