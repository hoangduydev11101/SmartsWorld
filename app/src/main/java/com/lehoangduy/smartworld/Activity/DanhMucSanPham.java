package com.lehoangduy.smartworld.Activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lehoangduy.smartworld.Adapter.DanhMucAdapter;
import com.lehoangduy.smartworld.Model.DanhMuc;
import com.lehoangduy.smartworld.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DanhMucSanPham extends AppCompatActivity {

    TextView txtInternet;
    AlphaAnimation alphaAnimation;
    ImageButton btnReload;
    GridView gridViewDanhMuc;
    ArrayList<DanhMuc> mangDanhMuc;
    DanhMucAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc_san_pham);
        AnhXa();
        alphaAnimation = new AlphaAnimation(1F, 0.5F);

        isInternetOn();
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(alphaAnimation);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        gridViewDanhMuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DanhMucSanPham.this, DienThoaiActivity.class);
                intent.putExtra("maloai", mangDanhMuc.get(position).MaLoai);
                startActivity(intent);
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetPhanLoai().execute("http://hostfree321.esy.es/getphanloai.php");
            }
        });
    }

    class GetPhanLoai extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... params) {

            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    JSONObject object = array.getJSONObject(i);
                    mangDanhMuc.add(new DanhMuc(object.getInt("Ma"), object.getString("Ten"), object.getString("Hinh")));
                }
                adapter = new DanhMucAdapter(DanhMucSanPham.this, R.layout.dong_danhmuc, mangDanhMuc);
                gridViewDanhMuc.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            btnReload.setVisibility(View.VISIBLE);
            txtInternet.setVisibility(View.VISIBLE);
            return false;
        }
        return false;
    }

    private void AnhXa(){
        txtInternet = (TextView) findViewById(R.id.txtInternet);
        gridViewDanhMuc = (GridView) findViewById(R.id.gridViewDanhMuc);
        mangDanhMuc = new ArrayList<DanhMuc>();
        btnReload = (ImageButton) findViewById(R.id.btnReload);
    }

    private static String docNoiDung_Tu_URL(String theUrl)    {
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
