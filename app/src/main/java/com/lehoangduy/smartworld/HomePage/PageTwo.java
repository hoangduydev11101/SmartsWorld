package com.lehoangduy.smartworld.HomePage;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lehoangduy.smartworld.Activity.DetailProduct;
import com.lehoangduy.smartworld.Adapter.SpMoiAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class PageTwo extends Fragment {


    public PageTwo() {
        // Required empty public constructor
    }

    AlphaAnimation alphaAnimation;
    View view;
    ImageButton btnReloadPage1;
    TextView txtInternet;
    GridView gridViewSanPham;
    ArrayList<SanPham> mangSanPham;
    SpMoiAdapter adapter = null;
    int vitri =0;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_page_two, container, false);
        AnhXa();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetPageHome().execute("http://hostfree321.esy.es/getspmoi.php");
            }
        });

        gridViewSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailProduct.class);
                intent.putExtra("Id", mangSanPham.get(position).MaSanPham);
                startActivity(intent);
            }
        });

        return view;
    }

    private void AnhXa(){
        alphaAnimation = new AlphaAnimation(1F, 0.5F);
        btnReloadPage1 = (ImageButton) view.findViewById(R.id.btnReloadPage);
        txtInternet = (TextView) view.findViewById(R.id.txtInternetPage1);
        gridViewSanPham = (GridView) view.findViewById(R.id.gridViewSanPhamMoi);
        mangSanPham = new ArrayList<>();
    }

    private class GetPageHome extends AsyncTask<String, Integer, String> {

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
                    mangSanPham.add(new SanPham(object.getString("Hinh"), object.getInt("Ma"), object.getString("Ten"), object.getInt("Gia"), object.getString("ChiTiet")));
                    Collections.shuffle(mangSanPham, new Random(seed));
                }

                adapter = new SpMoiAdapter(getActivity(), R.layout.dong_spmoi, mangSanPham);
                gridViewSanPham.setAdapter(adapter);

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

}
