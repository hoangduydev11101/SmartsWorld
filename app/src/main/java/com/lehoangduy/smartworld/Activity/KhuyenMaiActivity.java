package com.lehoangduy.smartworld.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lehoangduy.smartworld.Adapter.SPKhuyenMaiAdapter;
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

public class KhuyenMaiActivity extends AppCompatActivity {

    GridView gvKhuyenMai;
    ArrayList<SanPham> arrKhuyenMai;
    SPKhuyenMaiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khuyen_mai);

        gvKhuyenMai = (GridView) findViewById(R.id.gridViewSanPhamKM);
        arrKhuyenMai = new ArrayList<>();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetPageKM().execute("http://hostfree321.esy.es/getspkhuyenmai.php");
            }
        });

        gvKhuyenMai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KhuyenMaiActivity.this, DetailProduct.class);
                intent.putExtra("Id", arrKhuyenMai.get(position).MaSanPham);
                startActivity(intent);
            }
        });
    }

    private class GetPageKM extends AsyncTask<String, Integer, String> {

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
                    arrKhuyenMai.add(new SanPham(object.getString("Hinh"), object.getInt("Ma"), object.getString("Ten"), object.getInt("Gia"), object.getString("ChiTiet")));
                    Collections.shuffle(arrKhuyenMai, new Random(seed));
                }

                adapter = new SPKhuyenMaiAdapter(KhuyenMaiActivity.this, R.layout.dong_khuyenmai, arrKhuyenMai);
                gvKhuyenMai.setAdapter(adapter);

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

    @Override
    public void onBackPressed() {
        finish();
    }
}
