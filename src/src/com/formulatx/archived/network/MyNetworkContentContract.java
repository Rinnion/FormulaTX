package com.formulatx.archived.network;

import com.formulatx.archived.Settings;
import com.formulatx.archived.utils.MyLocale;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

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
                    DISPLAY_METHOD_PARTNER = dm_partner;

                    ArrayList<NameValuePair> dm_area = new ArrayList<NameValuePair>(1);
                    dm_area.add(new BasicNameValuePair("display_method", "area"));
                    DISPLAY_METHOD_AREA = dm_area;
                }

                public static final String URL_METHOD = URL + "?method=getallstaticpagebydisplaymethod";
                public static final ArrayList<NameValuePair> DISPLAY_METHOD_OBJECT;
                public static final ArrayList<NameValuePair> DISPLAY_METHOD_AREA;
                public static final ArrayList<NameValuePair> DISPLAY_METHOD_PARTNER;
            }

            public static class getpage
            {
                public static final String URL_METHOD = URL + "?method=getpage";

                /*
                public static ArrayList<NameValuePair> getObject(String id) {
                    ArrayList<NameValuePair> dm_object = new ArrayList<NameValuePair>(1);
                    dm_object.add(new BasicNameValuePair("display_method", "object"));

                    dm_object.add(new BasicNameValuePair(MyNetworkContentContract.FormulaTXApi.StaticPage.getpage.ID,id));
                    dm_object.add(new BasicNameValuePair(LANG, MyLocale.getCurrent()));
                    return dm_object;
                }
                */

                public static String getObject(long id){
                    return URL_METHOD + "&id=" + String.valueOf(id) + "&lang="+MyLocale.getCurrent();
                }

                public static final String ID = "id";
                public static final String LANG = "lang";

            }

            public static class getallstaticpagefromparent
            {
                public static final String URL_METHOD = URL + "?method=getallstaticpagefromparent";

                public static ArrayList<NameValuePair> getParent(long parent){
                    ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>(1);
                    dm_partner.add(new BasicNameValuePair("id", String.valueOf(parent)));
                    return dm_partner;
                }
            }

            public static class getallstaticpagefromparentnews
            {
                public static final String URL_METHOD = URL + "?method=getallstaticpagefromparentnews";

                public static String getParent(long parent){
                    return URL_METHOD + "&id="+String.valueOf(parent);
                }
            }

            public static class getallstaticpagefromparentproduct
            {
                public static final String URL_METHOD = URL + "?method=getallstaticpagefromparentproduct";
            }

            public static class getallstaticpagefromparentcard
            {
                public static final String URL_METHOD = URL + "?method=getallstaticpagefromparentcard";
            }

            public static class getallstaticpagefromparentpartner
            {
                public static final String URL_METHOD = URL + "?method=getallstaticpagefromparentpartner";
            }

            public static class getallstaticpagefromparentradio
            {
                public static final String URL_METHOD = URL + "?method=getallstaticpagefromparentradio";
            }

            public static class getadditionalfields
            {
                public static final String URL_METHOD = URL + "?method=getadditionalfields";

                public static String getParent(long parent){
                    return URL_METHOD + "&id="+String.valueOf(parent);
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

                public static ArrayList<NameValuePair> getGamer(long parent){
                    ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>();
                    dm_partner.add(new BasicNameValuePair(DISPLAY_METHOD, GAMER));
                    dm_partner.add(new BasicNameValuePair("parent", String.valueOf(parent)));
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

            public static class getListGallery
            {
                public static final String URL_METHOD = URL + "?method=getListGallery";
                public static final String URL_PODCAST = URL + "?method=getListGallery&type=podcast";
                public static final String URL_GALLERY = URL + "?method=getListGallery&type=gallery";
            }
        }

        public static class Parser {
            public static final String URL = URL_API + "parsers";

            public static class getparser
            {
                public static final String URL_METHOD = URL + "?method=getparsersbyid";

                public static ArrayList<NameValuePair> getUrl(long id){
                    ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>(1);
                    dm_partner.add(new BasicNameValuePair("id", String.valueOf(id)));
                    return dm_partner;
                }
            }
        }

        public static class Feedback {
            public static final String URL = URL_API + "feedback";

            public static class getgallery
            {
                public static final String URL_METHOD = URL + "?method=addfeedbackwithoutcaptcha";

                public static ArrayList<NameValuePair> getUrl(long id){
                    ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>(1);
                    dm_partner.add(new BasicNameValuePair("id", String.valueOf(id)));
                    return dm_partner;
                }
            }
        }

        public static class References {
            public static final String URL = URL_API + "references";

            public static class getreferencebyidapproved
            {
                public static final String URL_METHOD = URL + "?method=getreferencebyidapproved";

                public static ArrayList<NameValuePair> getUrl(long id, long page){
                    ArrayList<NameValuePair> dm_partner = new ArrayList<NameValuePair>(5);
                    dm_partner.add(new BasicNameValuePair("id", String.valueOf(id)));
                    dm_partner.add(new BasicNameValuePair("page", String.valueOf(page)));
                    dm_partner.add(new BasicNameValuePair("num_rec", String.valueOf(10)));
                    dm_partner.add(new BasicNameValuePair("sort_by", "date"));
                    dm_partner.add(new BasicNameValuePair("sort_method", "desc"));
                    return dm_partner;
                }
            }
        }

        public static  class Schedules {
            private static final String ladies = "http://parser.formulatx.com/ladies/order/%s/";

            public static String getLadies() {
                return String.format(ladies, MyLocale.getCurrent());
            }
        }
    }

}
