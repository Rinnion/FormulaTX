package com.rinnion.archived.network;

import android.os.Bundle;
import org.apache.http.HttpResponse;

/**
 * Created with IntelliJ IDEA.
 * User: tretyakov
 * Date: 27.01.14
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
public interface IResponseHandler {
    public Bundle Handle(HttpResponse response) throws Exception;
}
