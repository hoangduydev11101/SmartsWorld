package com.lehoangduy.smartworld.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lehoangduy.smartworld.Adapter.DonHangAdapter;
import com.lehoangduy.smartworld.Model.Config;
import com.lehoangduy.smartworld.Model.GioHang;
import com.lehoangduy.smartworld.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ThanhToan extends AppCompatActivity {

    TextView txtTongTien;
    ListView lvDonHang;
    ArrayList<GioHang> arrDonHang;
    int id = 0;
    String email = "";
    DonHangAdapter adapter;
    Button btnThanhToan;
    int tongtien = 0;
    ArrayList<String> mangTong;
    int a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        AnhXa();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        getGioHang();
        TongTien();
        DecimalFormat de = new DecimalFormat("###,###,###");
        txtTongTien.setText(de.format(tongtien)+" VNĐ");
        //Toast.makeText(this, a+"", Toast.LENGTH_SHORT).show();

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(ThanhToan.this);
                dialog.setMessage("Xin hãy đợi...");
                dialog.show();
                dialog.setCancelable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(ThanhToan.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 3000);
            }
        });
    }

    private void AnhXa(){
        mangTong    = new ArrayList<>();
        btnThanhToan = (Button) findViewById(R.id.btnThanhToanDH);
        txtTongTien = (TextView) findViewById(R.id.txtTongTien);
        lvDonHang = (ListView) findViewById(R.id.lvDonHang);
        arrDonHang = new ArrayList<>();
    }

    private void TongTien(){
        Cursor cursor = MainActivity.db.getData("SELECT SUM(Tong) AS Total FROM GioHang_Table WHERE Email = '"+email+"'");
        while (cursor.moveToNext()){
            tongtien = cursor.getInt(0);
        }
    }

    private void getGioHang(){
        Cursor get = MainActivity.db.getData("SELECT * FROM GioHang_Table WHERE Email = '"+email+"'");
        while (get.moveToNext()){
            arrDonHang.add(new GioHang(get.getInt(0), get.getString(1), get.getInt(2), get.getString(3), get.getInt(4), get.getString(5), get.getInt(6)));
            //a = get.getInt(6);
        }

        adapter = new DonHangAdapter(ThanhToan.this, R.layout.dong_donhang, arrDonHang);
        lvDonHang.setAdapter(adapter);
    }

}
