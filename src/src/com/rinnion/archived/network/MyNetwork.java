package com.rinnion.archived.network;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.Settings;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.helper.CommentHelper;
import com.rinnion.archived.database.helper.NewsHelper;
import com.rinnion.archived.network.handlers.WeatherHandler;
import com.rinnion.archived.database.model.Profile;
import com.rinnion.archived.database.model.User;
import com.rinnion.archived.network.handlers.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Describes all Network operations
 */
public final class MyNetwork {

    private static final String TAG = "MyNetwork";

    //Загрузка данных с сервера
    public static Bundle queryMessages() {
        Log.d(TAG, "queryMessages");
        final DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();

        String credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS);

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher = builder.setName("queryMessages")
                .setGetRequest(MyNetworkContentContract.Messages.getUrlList())
                .setCredentials(credentials)
                .setHandler(new JSONArrayHandler(new NewsHandler()) {
                    @Override
                    public void beforeArrayHandle() {
                        NewsHelper mh = new NewsHelper(doh);
                        mh.clear();
                    }
                })
                .create();

        return fetcher.execute();
    }

    //Загрузка данных с сервера
    public static Bundle queryWeather(String country) {
        Log.d(TAG, String.format("query wheather: %s", country));
        final DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        String credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS);

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher = builder.setName("queryMessages")
                .setGetRequest("http://api.openweathermap.org/data/2.5/weather?q="+country+",ru&units=metric&appid=d20301f9f0795290b4e28b322f0f355d")
                .setHandler(new WeatherHandler(country))
                .create();

        return fetcher.execute();
    }

    public static Bundle queryDrafts() {
        Log.d(TAG, "queryDrafts");
        final DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();

        String credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS);

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher = builder.setName("queryDrafts")
                .setGetRequest(MyNetworkContentContract.Drafts.getUrlList())
                .setCredentials(credentials)
                .setHandler(new JSONArrayHandler(new NewsHandler()) {
                    @Override
                    public void beforeArrayHandle() {
                        NewsHelper mh = new NewsHelper(doh);
                        mh.clear();
                    }
                })
                .create();

        return fetcher.execute();
    }

    public static void queryComments(long messageId) {
        Log.d(TAG, "queryComments");
        final DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();

        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester fetcher = builder.setName("queryComments")
                .setGetRequest(MyNetworkContentContract.Messages.getUrlCommentList(messageId))
                .setHandler(new JSONArrayHandler(new CommentHandler(messageId)) {
                    @Override
                    public void beforeArrayHandle() {
                        CommentHelper ch = new CommentHelper(doh);
                        ch.clear();
                    }
                })
                .create();

        fetcher.execute();
    }

    public static void sendComment(long messageId, String comment) throws Exception {
        String credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS);
        if (credentials == null) {
            queryNewUser();
            credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS);
        }
        try {

            JSONObject newComment = new JSONObject();
            newComment.put("Content", comment);

            HttpRequester.Builder builder = new HttpRequester.Builder();
            HttpRequester sender = builder.setName("sendComment")
                    .setPostRequest(MyNetworkContentContract.Messages.getUrlCommentList(messageId))
                    .setCredentials(credentials)
                    .setContent(newComment)
                    .setHandler(new AnyPostHandler())
                    .create();
            sender.execute();
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    private static void queryNewUser() {
        HttpRequester.Builder builder = new HttpRequester.Builder();
        HttpRequester sender = builder.setName("queryNewUser")
                .setGetRequest(MyNetworkContentContract.Users.getUrlNew())
                .setHandler(new NewUserHandler())
                .create();
        Bundle execute = sender.execute();
        User user = (User) execute.getSerializable(NewUserHandler.VALUE);
        if (user.token == null) throw new IllegalStateException();

        //FIXME: should use type long instead String.valueOf
        ArchivedApplication.Settings.setUserId(user.id);
        ArchivedApplication.Settings.setUserName(user.name);
        ArchivedApplication.Settings.setUserAvatar(user.avatar);

        byte[] bytes = user.token.getBytes();
        final String base64Credentials = android.util.Base64.encodeToString(bytes, Base64.NO_WRAP);
        ArchivedApplication.setParameter(Settings.CREDENTIALS, base64Credentials);

        bytes = user.message_token.getBytes();
        final String base64MessageCredentials = android.util.Base64.encodeToString(bytes, Base64.NO_WRAP);
        ArchivedApplication.setParameter(Settings.CREDENTIALS_MESSAGE, base64MessageCredentials);

    }

    public static void sendMessage(String message) throws Exception {
        String credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS_MESSAGE);
        if (credentials == null) {
            queryNewUser();
            credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS_MESSAGE);
        }
        try {
            JSONObject newMessage = new JSONObject();
            newMessage.put("Content", message);

            JSONObject newTag = new JSONObject();
            newTag.put("Name", "newly");
            JSONArray newTags = new JSONArray();
            newTags.put(newTag);
            newMessage.put("Tags", newTags);
            newMessage.put("Background", null);

            HttpRequester.Builder builder = new HttpRequester.Builder();
            HttpRequester sender = builder.setName("sendMessage")
                    .setPostRequest(MyNetworkContentContract.Messages.getUrlList())
                    .setCredentials(credentials)
                    .setContent(newMessage)
                    .create();
            sender.execute();
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public static void sendLike(long id) throws Exception {
        String credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS_MESSAGE);
        if (credentials == null) {
            queryNewUser();
            credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS_MESSAGE);
        }
        try {
            JSONObject newMessage = new JSONObject();
            newMessage.put("MessageId", id);

            HttpRequester.Builder builder = new HttpRequester.Builder();
            HttpRequester sender = builder.setName("sendLike")
                    .setPostRequest(MyNetworkContentContract.My.getUrlLikes())
                    .setCredentials(credentials)
                    .setContent(newMessage)
                    .create();
            sender.execute();
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public static void sendDislike(long id) throws Exception {
        String credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS_MESSAGE);
        if (credentials == null) {
            queryNewUser();
            credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS_MESSAGE);
        }
        try {
            JSONObject newMessage = new JSONObject();
            newMessage.put("MessageId", id);

            HttpRequester.Builder builder = new HttpRequester.Builder();
            HttpRequester sender = builder.setName("sendDislike")
                    .setDeleteRequest(MyNetworkContentContract.My.getUrlLikes(id))
                    .setCredentials(credentials)
                    .setContent(newMessage)
                    .create();
            sender.execute();
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public static void updateProfile(Profile mUser) throws Exception {
        String credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS_MESSAGE);
        if (credentials == null) {
            queryNewUser();
            credentials = ArchivedApplication.getParameter(Settings.CREDENTIALS_MESSAGE);
        }
        try {
            JSONObject uUser = new JSONObject();
            uUser.put("Name", mUser.name);
            uUser.put("Avatar", mUser.avatar);

            HttpRequester.Builder builder = new HttpRequester.Builder();
            HttpRequester sender = builder.setName("updateProfile")
                    .setPutRequest(MyNetworkContentContract.My.getUrlProfile())
                    .setCredentials(credentials)
                    .setContent(uUser)
                    .create();
            sender.execute();
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }
}
