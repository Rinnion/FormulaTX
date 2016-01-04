package com.rinnion.archived.network;

import com.rinnion.archived.Settings;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 29.11.13
 * Time: 12:54
 * To change this template use File | Settings | File Templates.
 */
public class MyNetworkContentContract {


    public static final String URL_LOCAL = "http://192.168.56.1:3000/";
    public static final String URL_OUTSIDE = "http://app.formulatx.com/";
    public static final String URL = (Settings.NETDEBUG) ? URL_LOCAL : URL_OUTSIDE;
    public static final String URL_API = URL + "api/";

    public static class FormulaTXApi {
        public static class StaticPage {
            public static final String URL = URL_API + "static_page";

            public static class getallstaticpagebydisplaymethod
            {
                static{
                    ArrayList<NameValuePair> dm_object = new ArrayList<NameValuePair>(1);
                    dm_object.add(new BasicNameValuePair("display_method", "object"));
                    DISPLAY_METHOD_OBJECT = dm_object;

                    ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>(1);
                    dm_partner.add(new BasicNameValuePair("display_method", "partner"));
                    DISPLAY_METHOD_PARTNER = dm_object;
                }

                public static final String URL_METHOD = URL + "?method=getallstaticpagebydisplaymethod";
                public static final ArrayList<NameValuePair> DISPLAY_METHOD_OBJECT;
                public static final ArrayList<NameValuePair> DISPLAY_METHOD_PARTNER;
            }

            public static class getpage
            {
                public static final String URL_METHOD = URL + "?method=getpage";

                public static ArrayList<NameValuePair> getObject(String id) {
                    ArrayList<NameValuePair> dm_object = new ArrayList<NameValuePair>(1);
                    dm_object.add(new BasicNameValuePair("display_method", "object"));

                    dm_object.add(new BasicNameValuePair(MyNetworkContentContract.FormulaTXApi.StaticPage.getpage.ID,id));
                    dm_object.add(new BasicNameValuePair(LANG,LANG_RU));
                    return dm_object;
                }

                public static final String ID = "id";
                public static final String LANG = "lang";
                public static final String LANG_RU = "ru";
                public static final String LANG_EN = "en";
            }

            public static class getallstaticpagefromparent
            {
                public static final String URL_METHOD = URL + "?method=getallstaticpagefromparent";

                public static ArrayList<NameValuePair> getParent(String parent){
                    ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>(1);
                    dm_partner.add(new BasicNameValuePair("parent", parent));
                    return dm_partner;
                }
            }

            public static class getallstaticpagefromparentdisplaymethod
            {
                public static final String URL_METHOD = URL + "?method=getallstaticpagefromparentdisplaymethod";
                public static final String PARTNER = "partner";
                public static final String CARD = "card";
                public static final String GAMER = "gamer";
                public static final String PRODUCT = "product";
                public static final String AREA = "area";
                private static String DISPLAY_METHOD = "display_method";

                public static ArrayList<NameValuePair> getParent(String parent){
                    ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>();
                    dm_partner.add(new BasicNameValuePair("parent", parent));
                    return dm_partner;
                }

                public static ArrayList<NameValuePair> getPartners(String parent){
                    ArrayList<NameValuePair> dm_partner = getParent(parent);
                    dm_partner.add(new BasicNameValuePair(DISPLAY_METHOD, PARTNER));
                    return dm_partner;
                }

                public static ArrayList<NameValuePair> getGamers(String parent){
                    ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>();
                    dm_partner.add(new BasicNameValuePair(DISPLAY_METHOD, GAMER));
                    dm_partner.add(new BasicNameValuePair("parent", parent));
                    return dm_partner;
                }

                public static ArrayList<NameValuePair> getProducts(String parent){
                    ArrayList<NameValuePair> dm_partner = getParent(parent);
                    dm_partner.add(new BasicNameValuePair(DISPLAY_METHOD, PRODUCT));
                    return dm_partner;
                }

                public static ArrayList<NameValuePair> getCards(String parent){
                    ArrayList<NameValuePair> dm_partner = getParent(parent);
                    dm_partner.add(new BasicNameValuePair(DISPLAY_METHOD, CARD));
                    return dm_partner;
                }

                public static ArrayList<NameValuePair> getAreas(String parent){
                    ArrayList<NameValuePair> dm_partner = getParent(parent);
                    dm_partner.add(new BasicNameValuePair(DISPLAY_METHOD, AREA));
                    return dm_partner;
                }

            }


        }
        public static class Gallery {
            public static final String URL = URL_API + "gallery";

            public static class getgallery
            {
                public static final String URL_METHOD = URL + "?method=getgallery";

                public static ArrayList<NameValuePair> getUrl(long id){
                    ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>(1);
                    dm_partner.add(new BasicNameValuePair("id", String.valueOf(id)));
                    return dm_partner;
                }
            }
        }
    }

}
