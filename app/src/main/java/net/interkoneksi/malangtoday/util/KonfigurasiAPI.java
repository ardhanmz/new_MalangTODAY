package net.interkoneksi.malangtoday.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ardhan MZ on 16-Aug-17.
 */

public class KonfigurasiAPI {

    private static final String apiURL ="https://malangtoday.net/api/";
    public static final int postCount = 5;
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String addon){
        return apiURL + addon;
    }

    public static void bagikan(Context context, String title, String body){
        Intent bagi = new Intent(Intent.ACTION_SEND);
        bagi.setType("text/plain");
        bagi.putExtra(Intent.EXTRA_SUBJECT, title);
        bagi.putExtra(Intent.EXTRA_TEXT, body);
        bagi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(bagi, "Bagikan Dengan"));
    }

    public static void alamatluar(Context context, String address){
        Intent luar = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        context.startActivity(luar);
    }

    public static boolean isConnected(Context mContext){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    public static String load(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MalangTODAY"
                ,context.MODE_PRIVATE);
        String text = sharedPreferences.getString(key, "");
        return text;
    }
    public static void save(Context context, String key, String text){
        SharedPreferences.Editor editor = context.getSharedPreferences("MalangTODAY",
                context.MODE_PRIVATE).edit();
        editor.putString(key, text);editor.commit();

    }
    public static boolean isValid(String email){
        String pola_email = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pola = Pattern.compile(pola_email);
        Matcher matcher = pola.matcher(email);
        return matcher.matches();
    }
}
