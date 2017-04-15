package com.example.asus.reader.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.asus.reader.R;

public class ActivityWeb extends Activity {
    private WebView webView;
    public static final String PAGE_URL = "com.example.asus.reader.gui.PAGE_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent in = getIntent();
        String page_url = in.getStringExtra(PAGE_URL);

        webView = (WebView) findViewById(R.id.activityWeb);
        //webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(page_url);

        webView.setWebViewClient(new DisPlayWebPageActivityClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class DisPlayWebPageActivityClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
