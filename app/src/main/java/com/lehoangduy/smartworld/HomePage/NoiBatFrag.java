package com.lehoangduy.smartworld.HomePage;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lehoangduy.smartworld.Activity.DetailProduct;
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

import static android.view.View.VISIBLE;

/**
 * Created by Admin on 12/22/2016.
 */

public class NoiBatFrag extends Fragment {

    AlphaAnimation alphaAnimation;
    View view;
    ImageButton btnReloadPage1;
    TextView txtInternet;
    GridView gridViewSanPham;
    ArrayList<SanPham> mangSanPham;
    NoiBatAdater adapter = null;
    ProgressBar load;
    int vitri =0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_noibat, container, false);

        AnhXa();
        isInternetOn();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetPageNB().execute("http://hostfree321.esy.es/getspnoibat.php");
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = load.getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            load.setIndeterminateDrawable(drawable);
        }

        btnReloadPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(alphaAnimation);
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                startActivity(getActivity().getIntent());
                getActivity().overridePendingTransition(0, 0);
            }
        });

        gridViewSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vitri = position;
                Intent intent = new Intent(getActivity(), DetailProduct.class);
                intent.putExtra("Id", mangSanPham.get(position).MaSanPham);
                startActivity(intent);
            }
        });



        return view;

    }

    private class GetPageNB extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            load.setVisibility(view.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            return GetSanPham(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            load.setVisibility(View.INVISIBLE);

            super.onPostExecute(s);

            try {
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    JSONObject object = array.getJSONObject(i);
                    long seed = System.nanoTime();
                    mangSanPham.add(new SanPham(object.getString("Hinh"), object.getInt("Ma"), object.getString("Ten"), object.getInt("Gia"), object.getString("ChiTiet")));
                    Collections.shuffle(mangSanPham, new Random(seed));
                }

                adapter = new NoiBatAdater(getActivity(), R.layout.dong_noibat, mangSanPham);
                gridViewSanPham.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

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
            btnReloadPage1.setVisibility(VISIBLE);
            txtInternet.setVisibility(VISIBLE);
            return false;
        }
        return false;
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

    private void AnhXa(){
        alphaAnimation = new AlphaAnimation(1F, 0.5F);
        btnReloadPage1 = (ImageButton) view.findViewById(R.id.btnReloadPageNB);
        txtInternet = (TextView) view.findViewById(R.id.txtInternetNB);
        gridViewSanPham = (GridView) view.findViewById(R.id.gridViewSanPhamNB);
        mangSanPham = new ArrayList<>();
        load = (ProgressBar) view.findViewById(R.id.progresssBarNB);
    }

}
