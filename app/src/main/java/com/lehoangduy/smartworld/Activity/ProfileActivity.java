package com.lehoangduy.smartworld.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    TextView txtEmail, txtHoTen, txtSDT, txtDiaChi, txtSoLuong;
    Button btnThanhToan;
    String email;
    ArrayList<GioHang> mangGioHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AnhXa();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        txtEmail.setText(email);
        getKhachHang();
        DemGioHang();

        txtSoLuong.setText(mangGioHang.size()+"");

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ThanhToan.class);
                startActivity(intent);
            }
        });
    }

    private void DemGioHang(){
        Cursor dem = MainActivity.db.getData("SELECT * FROM GioHang_Table WHERE Email = '"+email+"'");
        if(dem!=null){
            while (dem.moveToNext()){
                mangGioHang.add(new GioHang(dem.getInt(0), dem.getString(1), dem.getInt(2), dem.getString(3), dem.getInt(4), dem.getString(5), dem.getInt(6)));
            }
        }
    }

    private void getKhachHang() {
        String Mail = txtEmail.getText().toString().trim();

        String url = Config.DATA_URL+Mail;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showKH(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showKH(String response){
        String name="";
        String address="";
        String sdt = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString("Ten");
            address = collegeData.getString("Diachi");
            sdt = collegeData.getString("Sdt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        txtHoTen.setText(name);
        txtDiaChi.setText(address);
        txtSDT.setText(sdt);
    }


    private void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(1, 111, 1, "Đăng xuất");
        menu.add(1, 222, 2, "Đổi mật khẩu");
        menu.add(1, 333, 3, "Sửa thông tin");
        menu.add(1, 666, 6, "Thoát");
        menu.add(1, 444, 4, "Trang chủ");

        MenuItem menuItem = menu.add(1, 555, 5, "Giỏ hàng");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(R.mipmap.ic_launcher_cart);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case 111: logout();
                break;
            case 222:
                Intent intent = new Intent(ProfileActivity.this, ChangepassActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                break;
            case 333:
                Intent intent1 = new Intent(ProfileActivity.this, AddInformationActivity.class);
                intent1.putExtra("email", email);
                startActivity(intent1);
                break;
            case 444: Intent intent2 = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
            case 555:
                Intent go = new Intent(getBaseContext(), MainActivity.class);
                go.putExtra("viewpager_position", 2);
                startActivity(go);
                break;
            case 666:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                alertDialogBuilder.setTitle("Thoát ứng dụng?");
                alertDialogBuilder.setIcon(R.mipmap.ic_launcher_delete);
                alertDialogBuilder
                        .setMessage("Bạn có chắc chắn muốn thoát ứng dụng?")
                        .setCancelable(false)
                        .setPositiveButton("Có",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("EXIT", true);
                                        startActivity(intent);
                                    }
                                })

                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) ProfileActivity.this.getSystemService(ProfileActivity.this.CONNECTIVITY_SERVICE);

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
            return false;
        }
        return false;
    }

    private void AnhXa(){
        mangGioHang = new ArrayList<>();
        txtEmail    = (TextView) findViewById(R.id.txtEmailKhachHang);
        txtDiaChi   = (TextView) findViewById(R.id.txtDiaChiKH);
        txtSDT      = (TextView) findViewById(R.id.txtSDTKhachHang);
        txtHoTen    = (TextView) findViewById(R.id.txtHoTenKH);
        txtSoLuong  = (TextView) findViewById(R.id.txtSoLuongSP);
        btnThanhToan = (Button) findViewById(R.id.btnThanhToanKH);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}
