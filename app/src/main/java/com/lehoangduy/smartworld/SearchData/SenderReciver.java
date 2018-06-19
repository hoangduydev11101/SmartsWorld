package com.lehoangduy.smartworld.SearchData;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by Admin on 12/22/2016.
 */

public class SenderReciver extends AsyncTask<Void, Void, String> {

    Context context;
    String urlAddress;
    String query;
    ListView listView;
    ImageView noData;

    ProgressDialog dialog;

    public SenderReciver(Context context, String urlAddress, String query, ListView listView, ImageView imageView) {
        this.context = context;
        this.urlAddress = urlAddress;
        this.query = query;
        this.listView = listView;
        this.noData = imageView;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(context);
        dialog.setTitle("Search");
        dialog.setMessage("Searching....Please wait");
        dialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.sendAnhrecive();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        dialog.dismiss();
        listView.setAdapter(null);

        if(s != null){
            if(!s.contains("null")){
                Parser parser = new Parser(context, s, listView);
                parser.execute();
            }
        }else {
            noData.setVisibility(View.VISIBLE);
        }

    }
    private String sendAnhrecive(){
        HttpURLConnection connection = Connector.connect(urlAddress);
        if(connection == null){
            return null;
        }
        try{
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(new DataPackager(query).packageData());
            bufferedWriter.flush();

            bufferedWriter.close();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if(responseCode==connection.HTTP_OK){

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer response = new StringBuffer();

                if(bufferedReader == null){
                    while ((line = bufferedReader.readLine()) != null){
                        response.append(line+"\n");
                    }
                }else {
                    return null;
                }
                return response.toString();


            }else {
                return String.valueOf(responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
