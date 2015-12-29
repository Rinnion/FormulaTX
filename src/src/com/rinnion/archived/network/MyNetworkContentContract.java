package com.rinnion.archived.network;

import com.rinnion.archived.Settings;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 29.11.13
 * Time: 12:54
 * To change this template use File | Settings | File Templates.
 */
public class MyNetworkContentContract {


    public static final String URL_LOCAL = "http://192.168.56.1:3000/";
    public static final String URL_OUTSIDE = "http://archived-001-site1.smarterasp.net/";
    public static final String URL = (Settings.NETDEBUG) ? URL_LOCAL : URL_OUTSIDE;
    public static final String URL_API = URL + "api/";

    public static class FormulaTXApi {
        public static class StaticPage {
            public static final String URL = URL_API + "static_page";
            public static final String URL_METHOD = URL + "?method=getallstaticpagebydisplaymethod";
            public static final String URL_ACTION_OBJECT = "display_method=object";
            public static final String URL_ACTION_PARTNER = "display_method=partner";
        }
    }

    public static class Messages {
        public static final String URL_MESSAGES_LIST = URL_API + "feed/";
        public static final String URL_MESSAGES_INFO = URL_API + "feed/%s";
        public static final String URL_MESSAGES_COMMENTS = URL_API + "messages/%s/comments/";

        public static String getUrlList() {
            return String.format(URL_MESSAGES_LIST);
        }

        public static String getUrlInfo(long messageId) {
            return String.format(URL_MESSAGES_INFO, messageId);
        }

        public static String getUrlCommentList(long messageId) {
            return String.format(URL_MESSAGES_COMMENTS, messageId);
        }
    }

    public static class Drafts {
        public static final String URL_DRAFT_LIST = URL_API + "drafts/";
        public static final String URL_DRAFT_INFO = URL_API + "drafts/%s";

        public static String getUrlList() {
            return String.format(URL_DRAFT_LIST);
        }

        public static String getUrlInfo(long messageId) {
            return String.format(URL_DRAFT_INFO, messageId);
        }
    }

    public static class Users {
        public static final String URL_USER_NEW = URL_API + "users/new";

        public static String getUrlNew() {
            return String.format(URL_USER_NEW);
        }
    }

    public static class My {
        public static final String URL_MY_PROFILE = URL_API + "my/profile";
        public static final String URL_MY_LIKES = URL_API + "my/likes/%s";
        public static final String URL_MY_MESSAGES = URL_API + "my/messages/";
        public static final String URLT_MY_COMMENTS = URL_API + "my/comments/";

        public static String getUrlProfile() {
            return String.format(URL_MY_PROFILE);
        }

        public static String getUrlLikes(String id) {
            return String.format(URL_MY_LIKES, id);
        }

        public static String getUrlLikes(long id) {
            return String.format(URL_MY_LIKES, String.valueOf(id));
        }

        public static String getUrlLikes() {
            return getUrlLikes("");
        }

        public static String getUrlMessages() {
            return String.format(URL_MY_MESSAGES);
        }

        public static String getUrlComments() {
            return String.format(URLT_MY_COMMENTS);
        }
    }

}
