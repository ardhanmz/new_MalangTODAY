package net.interkoneksi.malangtoday.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Ardhan MZ on 16-Aug-17.
 */

public class KonfigurasiAPI {

    private static final String apiURL ="http://malangtoday.net/api/";
    public static final int postCount = 5;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static String getAbsoluteUrl(String addon){
        return apiURL + addon;
    }

    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
}
