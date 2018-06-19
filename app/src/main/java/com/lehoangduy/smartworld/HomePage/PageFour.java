package com.lehoangduy.smartworld.HomePage;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lehoangduy.smartworld.Activity.ReadNews;
import com.lehoangduy.smartworld.Model.XMLDOMParser;
import com.lehoangduy.smartworld.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFour extends Fragment {

    ListView lvNews;
    ArrayList<String> arrayTitle;
    ArrayList<String> arrayLink;
    View view;

    public PageFour() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_page_four, container, false);

        lvNews = (ListView) view.findViewById(R.id.lvTinTuc);
        arrayTitle = new ArrayList<String>();
        arrayLink = new ArrayList<String>();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new DocNoiDung().execute("http://vnexpress.net/rss/so-hoa.rss");
            }
        });

        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ReadNews.class);
                intent.putExtra("linktintuc", arrayLink.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    private class DocNoiDung extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            if(s!=null && s.trim().length() > 0){
                Document document = parser.getDocument(s);
                NodeList nodeList = document.getElementsByTagName("item");
                String title = "";
                for(int i=0; i<nodeList.getLength(); i++){
                    Element element = (Element) nodeList.item(i);
                    title = parser.getValue(element, "title")+"\n";
                    arrayTitle.add(title);
                    arrayLink.add(parser.getValue(element, "link"));
                }
                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayTitle);
                lvNews.setAdapter(adapter);
                //Toast.makeText(MainActivity.this, nodeList.getLength()+"", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Vui lòng kiểm tra internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static String docNoiDung_Tu_URL(String theUrl)
    {
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
