package com.lehoangduy.smartworld.SearchData;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 12/22/2016.
 */

public class Parser extends AsyncTask<Void, Void, Integer> {

    Context context;
    String data;
    ListView listView;

    ArrayList<String> names = new ArrayList<>();

    public Parser(Context context, String data, ListView listview) {
        this.context = context;
        this.data = data;
        this.listView = listview;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parse();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if(integer==1){
            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, names);
            listView.setAdapter(adapter);
        }else {
            Toast.makeText(context, "Unable to parse!", Toast.LENGTH_SHORT).show();

        }

    }

    private int parse(){
        try{
            JSONArray array = new JSONArray(data);
            JSONObject object = null;
            names.clear();
            for(int i=0; i<array.length(); i++){
                object = array.getJSONObject(i);
                String name = object.getString("TenSanPham");
                names.add(name);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
