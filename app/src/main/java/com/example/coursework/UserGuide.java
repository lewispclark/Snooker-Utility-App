package com.example.coursework;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * User guide is used as a WebView to give users a visual guide of how to use the app
 */
public class UserGuide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        WebView webView = findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/user-guide.html");
    }
}