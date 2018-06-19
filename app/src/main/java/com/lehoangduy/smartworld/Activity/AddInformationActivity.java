package com.lehoangduy.smartworld.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lehoangduy.smartworld.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddInformationActivity extends AppCompatActivity {

    String email="";
    Button btnConfirm, btnCancel;
    EditText edtName, edtSDT, edtDiaChi;
    private static final String REGISTER_URL = "http://hostfree321.esy.es/updatecustomer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_information);
        AnhXa();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isInternetOn()){
                    Toast.makeText(AddInformationActivity.this, "Error in Internet Connection!", Toast.LENGTH_SHORT).show();
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Infomation().execute("http://hostfree321.esy.es/updatecustomer.php");
                        }
                    });
                }
            }
        });
    }

    private class Infomation extends AsyncTask<String, Integer, String>{

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(AddInformationActivity.this, "Please Wait",null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            return UpdateInformation(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            String result = s.trim();
            loading.dismiss();
            if(result.equalsIgnoreCase("successfully")){
                Intent intent = new Intent(AddInformationActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(AddInformationActivity.this, "Update succeed!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private String UpdateInformation(String link){
        HttpURLConnection connect;
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error!";
        }
        try {
            // cấu hình HttpURLConnection
            connect = (HttpURLConnection)url.openConnection();
            connect.setReadTimeout(10000);
            connect.setConnectTimeout(15000);
            connect.setRequestMethod("POST");
            // Gán tham số vào URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("email", email)
                    .appendQueryParameter("name", edtName.getText().toString())
                    .appendQueryParameter("phone", edtSDT.getText().toString().trim())
                    .appendQueryParameter("diachi", edtDiaChi.getText().toString());
            String query = builder.build().getEncodedQuery();

            // Mở kết nối gửi dữ liệu
            OutputStream os = connect.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            connect.connect();

        } catch (IOException e1) {
            e1.printStackTrace();
            return "Error!";
        }
        try {
            int response_code = connect.getResponseCode();

            // kiểm tra kết nối ok
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Đọc nội dung trả về
                InputStream input = connect.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                String s = result.toString().trim();

//                if(s.equals("successfully")){
//                    finish();
//                }else {
//                    Toast.makeText(this, "Error when change password!", Toast.LENGTH_SHORT).show();
//                }

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }else{
                return "Error!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error!";
        } finally {
            connect.disconnect();
        }
    }

    private void UpdateCustomer() {
        String name = edtName.getText().toString().trim().toLowerCase();
        String phone = edtSDT.getText().toString().trim().toLowerCase();
        String diachi = edtDiaChi.getText().toString().trim().toLowerCase();

        update(name, phone, diachi, email);
    }

    private void update(String name, String phone, String diachi, String email) {
        String urlSuffix = "?name="+name+"&phone="+phone+"&diachi="+diachi+"&email"+email;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddInformationActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                String result = s.trim();
                loading.dismiss();
                if(result.equalsIgnoreCase("successfully")){
                    Toast.makeText(AddInformationActivity.this, "Update succeed!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                }catch(Exception e){
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) AddInformationActivity.this.getSystemService(AddInformationActivity.this.CONNECTIVITY_SERVICE);

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
       btnCancel    = (Button) findViewById(R.id.btnCancelKH);
       btnConfirm   = (Button) findViewById(R.id.btnConfirmKH);
       edtDiaChi    = (EditText) findViewById(R.id.edtAdressKH);
       edtName      = (EditText) findViewById(R.id.edtNameKH);
       edtSDT       = (EditText) findViewById(R.id.edtPhoneKH);
   }
}
