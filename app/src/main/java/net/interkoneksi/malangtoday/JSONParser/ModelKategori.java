package net.interkoneksi.malangtoday.JSONParser;

import android.util.Log;

import com.orm.SugarRecord;

import net.interkoneksi.malangtoday.util.KonfigurasiAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ardhan MZ on 16-Aug-17.
 */

public class ModelKategori extends SugarRecord{
    private final String TAG = "ModalKategori";
    //Inisiasi Variabel
    public String title;
    public int id, postCount;

    public String getUrl(int page){
        return "get_category_index/" +id+ "&count" + KonfigurasiAPI.postCount+ "&page"
           +page;
    }
    public void parseKategori(JSONObject jsonObject){
        try{
            this.id=jsonObject.getInt("id");
            this.title=jsonObject.getString("title");
            this.postCount=jsonObject.getInt("post_count");
        }catch (JSONException je){
            je.printStackTrace();
            Log.d(TAG, "====KESALAHAN PARSING KATEGORI=====");
        }
    }
}
