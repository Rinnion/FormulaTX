package com.formulatx.archived.network;

import android.os.Bundle;
import org.apache.http.HttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 21.02.14
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */
public final class EmptyResponseHandler implements IResponseHandler {
    @Override
    public Bundle Handle(HttpResponse response) throws Exception {
        return Bundle.EMPTY;
    }

    @Override
    public Bundle Handle(String string) throws Exception {
        return Bundle.EMPTY;
    }
}
