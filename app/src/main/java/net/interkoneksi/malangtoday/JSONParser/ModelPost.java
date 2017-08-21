package net.interkoneksi.malangtoday.JSONParser;

import android.util.Log;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ardhan MZ on 16-Aug-17.
 */

public class ModelPost extends SugarRecord{
    private static final String TAG="ModelPost";
    public int id, comment_count;
    public String url,title,content,date,author,comment_status;
    public String status, img, view_count;
    public List<String> categories;
    public List<ModelKomentar> comment;
    public JSONArray kategorijson, komentarjson;

    public ModelPost(){
        id=-1;
        url="";
        title="";
        content="";
        author="";
        date="";
        comment_status="";
        status="";
        categories = new ArrayList<>();
        comment = new ArrayList<>();
    }

    public void parsePost(JSONObject jsonObject){
        try {
            id=jsonObject.getInt("id");
            url=jsonObject.getString("url");
            title=jsonObject.getString("title");
            status=jsonObject.getString("status");
            content= jsonObject.getString("date");
            for (int i=0;i<jsonObject.getJSONArray("categories").length();i++){
                categories.add(jsonObject.getJSONArray("categories").getJSONObject(i)
                .getString("title"));
            }
            author = jsonObject.getJSONObject("author").getString("nickname");
            for (int i=0;i < jsonObject.getJSONArray("comments").length();i++){
                ModelKomentar modelKomentar = new ModelKomentar();
                modelKomentar.parseComment(jsonObject.getJSONArray("comments").getJSONObject(i));
                comment.add(modelKomentar);
            }
            comment_count = jsonObject.getInt("comment_count");
            comment_status = jsonObject.getString("comment_status");
            img = jsonObject.getString("thumbnail");
            view_count = jsonObject.getJSONObject("custom_fields").getString("post_views_count");
        }catch (JSONException je){
            je.printStackTrace();
            Log.d(TAG, "====Kesalahan Parsing Posting=====");
        }
    }
    public JSONObject parseJSON(){
        JSONObject jsonObject = new JSONObject();
        JSONObject authorobject = new JSONObject();
        try {
            jsonObject.put("id",id);jsonObject.put("url",url);
            jsonObject.put("title",title);jsonObject.put("status",status);
            jsonObject.put("content",content);jsonObject.put("date",date);

            kategorijson = new JSONArray();
            for (int i = 0; i < categories.size(); i++){
                kategorijson.put(new JSONObject().put("title", categories.get(i)));
            }
            jsonObject.put("categories",kategorijson);
            authorobject.put("nickname", author);
            jsonObject.put("author", authorobject);
            komentarjson = new JSONArray();
            for (int i=0; i < comment.size();i++){
                komentarjson.put(comment.get(i).parseJSON());
            }
            jsonObject.put("comment", komentarjson);
            jsonObject.put("comment_count", comment_count);
            jsonObject.put("comment_status", comment_status);
            jsonObject.put("thumbnail", img);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "+====KESALAHAN PACKING JSON POST=====");
        }
        return  jsonObject;
    }
}
