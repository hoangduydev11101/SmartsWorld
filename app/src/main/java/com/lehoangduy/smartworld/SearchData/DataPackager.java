package com.lehoangduy.smartworld.SearchData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Admin on 12/22/2016.
 */

public class DataPackager {
    String query;

    public DataPackager(String query) {
        this.query = query;
    }
    public String packageData(){
        JSONObject object = new JSONObject();
        StringBuffer queryString = new StringBuffer();

        try{

            object.put("Query", query);
            Boolean firstValue = true;
            Iterator iterator = object.keys();

            do {
                String key = iterator.next().toString();
                String value = object.get(key).toString();
                if (firstValue){
                    firstValue = false;
                }else {
                    queryString.append("&");
                }

                queryString.append(URLEncoder.encode(key, "UTF-8"));
                queryString.append("=");
                queryString.append(URLEncoder.encode(key, "UTF-8"));

            }while (iterator.hasNext());

        }catch (JSONException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
