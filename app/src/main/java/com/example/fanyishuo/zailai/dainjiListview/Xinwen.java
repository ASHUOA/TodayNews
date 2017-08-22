package com.example.fanyishuo.zailai.dainjiListview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.fanyishuo.zailai.R;

/**
 * Created by fanyishuo on 2017/8/15.
 */

public class Xinwen extends AppCompatActivity{

    private WebView web;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xinwen);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        web = (WebView) findViewById(R.id.web);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        web.loadUrl(url);
        web.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressbar.setProgress(newProgress);
            }
        });
    }
}
