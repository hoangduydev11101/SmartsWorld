package com.lehoangduy.smartworld.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lehoangduy.smartworld.Adapter.PhanLoaiSPAdapter;
import com.lehoangduy.smartworld.Model.Config;
import com.lehoangduy.smartworld.Model.SanPham;
import com.lehoangduy.smartworld.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DienThoaiActivity extends AppCompatActivity {

    ArrayList<SanPham> mangSanPham;
    GridView gvSanPham;
    PhanLoaiSPAdapter adapter;
    int ma = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);

        AnhXa();

        final Intent intent = getIntent();
        ma = intent.getIntExtra("maloai", 0);

        getSanPhamPL();

        gvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(DienThoaiActivity.this, DetailProduct.class);
                intent1.putExtra("Id", mangSanPham.get(position).MaSanPham);
                startActivity(intent1);
            }
        });

    }

    private void getSanPhamPL() {
        String MaLoai = String.valueOf(ma);

        String url = Config.DATA_PL+MaLoai;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showPhone(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DienThoaiActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showPhone(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            for (int i=0; i<result.length(); i++){
                JSONObject collegeData = result.getJSONObject(i);
                mangSanPham.add(new SanPham(collegeData.getString("Hinh"), collegeData.getInt("Ma"), collegeData.getString("Ten"), collegeData.getInt("Gia"), collegeData.getString("ChiTiet")));
            }
            adapter = new PhanLoaiSPAdapter(DienThoaiActivity.this, R.layout.dong_sanpham_phanloai, mangSanPham);
            gvSanPham.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void AnhXa(){
        mangSanPham = new ArrayList<>();
        gvSanPham   = (GridView) findViewById(R.id.gridViewSamPhamPL);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
