package net.interkoneksi.malangtoday.model;

import android.util.Log;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ardhan MZ on 16-Aug-17.
 */

public class ModelKomentar extends SugarRecord{
    private final String TAG = ("LOG");
    public int id;
    public String name,date,content;

    public ModelKomentar(){
        id=-1;
        name=date=content="";
    }
    //Method Untuk Parsing JSON API Sebuah Komentar menjadi POJOs//
    public void parseComment(JSONObject jsonObject){
        try {
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            date = jsonObject.getString("date");
            content = jsonObject.getString("content");

        }catch (JSONException je){
            je.printStackTrace();
            Log.d(TAG,"----KESALAHAN PARSING KOMENTAR");
        }
    }
    //Method Untuk Parsing Komentar Menjadi JSON untuk dikirimkan menuju API//
    public JSONObject parseJSON(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("id", id);
            jsonObject.put("name",name);
            jsonObject.put("date",date);
            jsonObject.put("content",content);
        }catch (JSONException je){
            je.printStackTrace();
            Log.d(TAG,"---KESALAHAN PACKING JSON KOMENTAR");
        }
        return jsonObject;
    }
}
