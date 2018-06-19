package com.lehoangduy.smartworld.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lehoangduy.smartworld.R;

public class ReadNews extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);

        webView = (WebView) findViewById(R.id.wvReadNews);

        Intent intent = getIntent();
        String linktin = intent.getStringExtra("linktintuc");
        webView.loadUrl(linktin);
        webView.setWebViewClient(new WebViewClient());

    }
}
