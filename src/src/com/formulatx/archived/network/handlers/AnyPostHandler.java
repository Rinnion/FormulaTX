package com.formulatx.archived.network.handlers;

import android.os.Bundle;
import com.formulatx.archived.network.IResponseHandler;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 12.03.14
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
public final class AnyPostHandler implements IResponseHandler {


    public static final String LOCATION = "LOCATION";

    @Override
    public Bundle Handle(HttpResponse response) throws Exception {
        StatusLine sl = response.getStatusLine();
        if (sl.getStatusCode() == 201) {
            Header hdrLocation = response.getFirstHeader("Location");
            String location = hdrLocation.getValue();
            Bundle bundle = new Bundle();
            bundle.putString(LOCATION, location);
            return bundle;
        }
        return Bundle.EMPTY;
    }

    @Override
    public Bundle Handle(String string) throws Exception {
        return Bundle.EMPTY;
    }
}
