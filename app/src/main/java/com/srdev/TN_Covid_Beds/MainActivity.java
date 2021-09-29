package com.srdev.TN_Covid_Beds;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {


    private long backPressedTime;
    private Toast backToast;
    RelativeLayout layout;
    SwipeRefreshLayout swipeRefreshLayout;

   ProgressBar progressbar;

    WebView mWebView;
    private RelativeLayout container;

    String urlLink ="https://tncovidbeds.tnega.org/";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setWebViewClient(new myWebViewClient());
        progressbar = findViewById(R.id.progress_bar);
        layout = findViewById(R.id.Relativelayout);
        swipeRefreshLayout = findViewById(R.id.swipe);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);

        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.getSettings().setLoadsImagesAutomatically(true);

//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//               loader.setVisibility(View.GONE);
//            }
//        }, 4000);

        mWebView.loadUrl(urlLink);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        mWebView.loadUrl(urlLink);
                        layout.setVisibility(View.GONE);
                    }
                },  3000);
            }
        });

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark)
        );
        mWebView.loadUrl(urlLink);
//        mWebView.setWebViewClient(new WebViewClient() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
//                view.getContext().startActivity(intent);
//                return true;
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Tamil Nadu COVID Beds\n" +
                        "\nCheck vacancy of Beds, Oxygen available in Tamil Nadu hospitals\n(Both Government and Private)" +"\nhttps://play.google.com/store/apps/details?id=" + this.getPackageName();
                String shareSubject = "App Link";

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);

                startActivity(Intent.createChooser(shareIntent, "Share Using"));
                return true;
            case R.id.reload:
                mWebView.loadUrl(urlLink);
                layout.setVisibility(View.INVISIBLE);
                return true;
        }
        return true;
    }


    public class myWebViewClient extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
            view.getContext().startActivity(intent);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            layout.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageStarted(WebView view, String urlLink, Bitmap favicon) {
            super.onPageStarted(view, urlLink, favicon);
            progressbar.setVisibility(View.VISIBLE);

        }

        @Override
        public void onPageFinished(WebView view, String urlLink) {
            super.onPageFinished(view, urlLink);
           progressbar.setVisibility(View.INVISIBLE);

        }
    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}