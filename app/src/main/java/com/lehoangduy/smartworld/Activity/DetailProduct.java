package com.lehoangduy.smartworld.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lehoangduy.smartworld.Model.Config;
import com.lehoangduy.smartworld.Model.GioHang;
import com.lehoangduy.smartworld.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailProduct extends AppCompatActivity {

    ArrayList<GioHang> mangGioHang;
    Button btnMuaNgay;
    ImageButton btnThemVaoGio;
    TextView txtTenSanPham, txtGiaSanPham, txtChiTietSP, txtUp, txtDown, txtSoLuong;
    ImageView imgHinhChiTiet;
    int id = 0;
    int soluong = 0;
    String tensp = "";
    String email = "";
    int giasp = 0;
    String gia = "";
    String hinh = "";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        AnhXa();
        progressBar.setEnabled(true);
        Intent intent = getIntent();
        id = intent.getIntExtra("Id", 0);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        //Toast.makeText(this, email, Toast.LENGTH_SHORT).show();

        getDetailPro();
        XuLySoLuong();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationIcon(R.drawable.back_mini);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailProduct.this, ThanhToan.class);
                intent1.putExtra("Id", id);
                startActivity(intent1);
            }
        });



        btnThemVaoGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tensp = txtTenSanPham.getText().toString();
                soluong = Integer.parseInt(txtSoLuong.getText().toString());
                giasp = Integer.parseInt(gia);

                if(email.toString().equals("Not Available")){
                    Toast.makeText(DetailProduct.this, "Mời bạn đăng nhập trước khi mua hàng!", Toast.LENGTH_SHORT).show();
                }

                else if(soluong<1){
                    Toast.makeText(DetailProduct.this, "Bạn chưa chọn số lượng!", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor get = MainActivity.db.getData("SELECT * FROM GioHang_Table WHERE MaSP = '"+id+"'");
                    mangGioHang.clear();
                    if(get!=null){
                        while (get.moveToNext()){
                            mangGioHang.add(new GioHang(get.getInt(0), get.getString(1), get.getInt(2), get.getString(3), get.getInt(4), get.getString(5), get.getInt(6)));
                        }
                        if(mangGioHang.size()>0){
                            final int sl = soluong + mangGioHang.get(0).SoLuongSp;
                            AlertDialog.Builder dialog = new AlertDialog.Builder(DetailProduct.this);
                            dialog.setIcon(R.drawable.mark);
                            dialog.setTitle("Sản phẩm đã có trong giỏ hàng");
                            dialog.setMessage("Bạn có thể thêm số lượng hoặc cập nhật số lượng?");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mangGioHang.clear();
                                    int tong = sl*giasp;
                                    MainActivity.db.UPDATE_GIOHANG(id, tensp, sl, email, giasp, hinh, tong, id, email);
                                    Toast.makeText(DetailProduct.this, "Đã thêm số lượng!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.setNeutralButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.setNegativeButton("Cập nhật", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int tong = soluong*giasp;
                                    MainActivity.db.UPDATE_GIOHANG(id, tensp, soluong, email, giasp, hinh, tong, id, email);
                                    Toast.makeText(DetailProduct.this, "Đã chập nhật số lượng!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            dialog.show();

                        }else {
                            int tong = soluong*giasp;
                            MainActivity.db.INSERT_GIOHANG(id, tensp, soluong, email, giasp, hinh, tong);
                            Toast.makeText(DetailProduct.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
        });


    }


    private void XuLySoLuong(){


        txtDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluong = Integer.parseInt(txtSoLuong.getText().toString());
                soluong = soluong - 1;
                txtSoLuong.setText(String.valueOf(soluong));
                if (soluong < 0){
                    txtSoLuong.setText("0");
                }
            }
        });
        txtUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluong = Integer.parseInt(txtSoLuong.getText().toString());
                soluong = soluong+1;
                txtSoLuong.setText(String.valueOf(soluong));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(1, 111, 1, "Giohang");
        item.setIcon(R.mipmap.ic_launcher_cart);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 111:
                Intent intent = new Intent(DetailProduct.this, MainActivity.class);
                intent.putExtra("viewpager_position", 2);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDetailPro() {

        String Id = String.valueOf(id);

        String url = Config.DETAIL_URL+Id;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showDetail(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailProduct.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showDetail(String response){
        progressBar.setEnabled(false);
        String name="";
        String chitiet = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString("Ten");
            gia = collegeData.getString("Gia");
            chitiet = collegeData.getString("ChiTiet");
            hinh = collegeData.getString("HinhAnh");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final DecimalFormat decimal = new DecimalFormat("##,###,###");
        int giasp = Integer.parseInt(gia);
        txtTenSanPham.setText(name);
        txtGiaSanPham.setText("Giá sản phẩm: "+decimal.format(giasp)+" vnđ");
        txtChiTietSP.setText(chitiet);
        Picasso.with(this).load(hinh).placeholder(R.drawable.image).into(imgHinhChiTiet);
    }

    private void AnhXa(){
        progressBar = (ProgressBar) findViewById(R.id.progresssBar);
        btnMuaNgay  = (Button) findViewById(R.id.btnMua);
        btnThemVaoGio   = (ImageButton) findViewById(R.id.btnThem);
        txtTenSanPham   = (TextView) findViewById(R.id.txtTenSanPhamCT);
        txtGiaSanPham   = (TextView) findViewById(R.id.txtGiaSanPhamCT);
        txtChiTietSP    = (TextView) findViewById(R.id.txtChiTietSP);
        imgHinhChiTiet  = (ImageView) findViewById(R.id.imgHinhSanPhamCT);
        txtUp           = (TextView) findViewById(R.id.txtUp);
        txtDown         = (TextView) findViewById(R.id.txtDown);
        txtSoLuong      = (TextView) findViewById(R.id.txtSoLuongCT);
        mangGioHang     = new ArrayList<>();
    }

}
