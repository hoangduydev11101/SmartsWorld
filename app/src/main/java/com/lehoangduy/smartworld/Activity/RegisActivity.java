package com.lehoangduy.smartworld.Activity;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lehoangduy.smartworld.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword, edtPassAgain;
    Button btnDangKy, btnNhapLai;
    private static final String REGISTER_URL = "http://hostfree321.esy.es/registration.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AnhXa();

        btnNhapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtEmail.setText("");
                edtPassword.setText("");
                edtPassAgain.setText("");
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String pass = edtPassword.getText().toString().trim();
                String passagain = edtPassAgain.getText().toString().trim();

                if(email.equals("") || pass.equals("") || passagain.equals("")){
                    Toast.makeText(RegisActivity.this, "You must put full field!", Toast.LENGTH_SHORT).show();
                }else if(!pass.equals(passagain)){
                    Toast.makeText(RegisActivity.this, "Password is not match!", Toast.LENGTH_SHORT).show();
                }else if(pass.equals(passagain) && pass.length()<6){
                    Toast.makeText(RegisActivity.this, "Password least is 6 character!", Toast.LENGTH_SHORT).show();
                }else if(isInternetOn()){
                    registerUser();
                }else {
                    Toast.makeText(RegisActivity.this, "Error in Network Connection", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) RegisActivity.this.getSystemService(RegisActivity.this.CONNECTIVITY_SERVICE);

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
        edtEmail     = (EditText) findViewById(R.id.edtUserRegis);
        edtPassAgain = (EditText) findViewById(R.id.edtPassAgainRegis);
        edtPassword  = (EditText) findViewById(R.id.edtPassRegis);
        btnDangKy    = (Button) findViewById(R.id.btnDangKy);
        btnNhapLai   = (Button) findViewById(R.id.btnClearRegis);
    }

    private void registerUser() {
        String password = edtPassword.getText().toString().trim().toLowerCase();
        String email = edtEmail.getText().toString().trim().toLowerCase();
        String passwordagain = edtPassAgain.getText().toString().trim().toLowerCase();

        register(email, password, passwordagain);
    }

    private void register(String email, String password, String passwordagain) {
        String urlSuffix = "?email="+email+"&password="+password+"&passwordagain="+passwordagain;
        class RegisterUser extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                String result = s.trim();
                loading.dismiss();
                if(result.equalsIgnoreCase("successfully")){
                    Toast.makeText(RegisActivity.this, "Register succeed!", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(RegisActivity.this, "Email alredy exist!", Toast.LENGTH_LONG).show();
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

}
