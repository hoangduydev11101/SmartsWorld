package com.lehoangduy.smartworld.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lehoangduy.smartworld.Adapter.NoiBatAdater;
import com.lehoangduy.smartworld.Model.SanPham;
import com.lehoangduy.smartworld.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class NoiBatActivity extends AppCompatActivity {

    AlphaAnimation alphaAnimation;
    View view;
    GridView gridViewSanPham;
    ArrayList<SanPham> mangSanPham;
    NoiBatAdater adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noi_bat);
        AnhXa();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetPageNB().execute("http://hostfree321.esy.es/getspnoibat.php");
            }
        });

        gridViewSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(NoiBatActivity.this, DetailProduct.class);
                intent.putExtra("Id", mangSanPham.get(position).MaSanPham);
                startActivity(intent);
            }
        });

    }

    private class GetPageNB extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            return GetSanPham(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            try {
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    JSONObject object = array.getJSONObject(i);
                    long seed = System.nanoTime();
                    mangSanPham.add(new SanPham(object.getString("Hinh"), object.getInt("Ma"), object.getString("Ten"), object.getInt("Gia"), object.getString("ChiTiet")));
                    Collections.shuffle(mangSanPham, new Random(seed));
                }

                adapter = new NoiBatAdater(NoiBatActivity.this, R.layout.dong_noibat, mangSanPham);
                gridViewSanPham.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private static String GetSanPham(String theUrl)    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

    private void AnhXa() {
        alphaAnimation = new AlphaAnimation(1F, 0.5F);
        gridViewSanPham = (GridView) findViewById(R.id.gridViewSanPhamNB);
        mangSanPham = new ArrayList<>();
    }

}
