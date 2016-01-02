package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.network.IResponseHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 27.01.14
 * Time: 14:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class JSONHandler implements IResponseHandler {

    @Override
    public final Bundle Handle(HttpResponse response) throws Exception {
        Bundle bundle = new Bundle();

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String content = EntityUtils.toString(entity);
            bundle.putAll(onStringHandle(content));
        }
        return bundle;
    }

    protected abstract Bundle onStringHandle(String content) throws JSONException;
}
