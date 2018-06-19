package com.lehoangduy.smartworld.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lehoangduy.smartworld.Model.Config;
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

public class ChangepassActivity extends AppCompatActivity {

    Button btnChange;
    EditText edtOldPass, edtNewPass, edtPassComfirm;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        AnhXa();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old = edtOldPass.getText().toString().trim();
                String moi = edtNewPass.getText().toString().trim();
                String xn = edtPassComfirm.getText().toString().trim();
                if(old.equals("") || moi.equals("") || xn.equals("")){
                    Toast.makeText(ChangepassActivity.this, "Please fill all feild!", Toast.LENGTH_SHORT).show();
                }else if(old.length()<6 || old.length()<6){
                    Toast.makeText(ChangepassActivity.this, "Password least 6 charactoer!", Toast.LENGTH_SHORT).show();
                }else if(!moi.equals(xn)){
                    Toast.makeText(ChangepassActivity.this, "Password is not match!", Toast.LENGTH_SHORT).show();
                    edtPassComfirm.findFocus();
                }else if(!isInternetOn()){
                    Toast.makeText(ChangepassActivity.this, "Error in Internet conection!", Toast.LENGTH_SHORT).show();
                }else {
                    new XuLyPass().execute("http://hostfree321.esy.es/changepassword.php");

                }
            }
        });

    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) ChangepassActivity.this.getSystemService(ChangepassActivity.this.CONNECTIVITY_SERVICE);

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

    private class XuLyPass extends AsyncTask<String, Integer, String>{

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(ChangepassActivity.this, "Please Wait",null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            return ChangePassword(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            String result = s.trim();
            loading.dismiss();
            if(result.equalsIgnoreCase("successfully")){
                SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                //Getting editor
                SharedPreferences.Editor editor = preferences.edit();

                //Puting the value false for loggedin
                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                //Putting blank value to email
                editor.putString(Config.EMAIL_SHARED_PREF, "");

                //Saving the sharedpreferences
                editor.commit();
                Intent intent = new Intent(ChangepassActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(ChangepassActivity.this, "Change succeed!", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    private String ChangePassword(String link){
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
                    .appendQueryParameter("oldpass", edtOldPass.getText().toString().trim())
                    .appendQueryParameter("newpass", edtNewPass.getText().toString().trim());
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

    private void AnhXa(){
        btnChange   = (Button) findViewById(R.id.btnChange);
        edtOldPass  = (EditText) findViewById(R.id.edtOldPassword);
        edtNewPass  = (EditText) findViewById(R.id.edtNewPassword);
        edtPassComfirm = (EditText) findViewById(R.id.edtNewPassConfirm);
    }
}
