package com.ghostruck.android;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
    
    private static final String BASE_URL = "https://m.ghostruck.com";
    private static final int PICK_CAMERA_IMAGE = 2;

    private WebView mWebView;
    private ProgressBar mProgressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Defaults to visible.  We will hide on onPageFinished
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.addJavascriptInterface(new JSBridge(), "JSBridge");
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        
        // Load url
        mWebView.loadUrl(BASE_URL);
        loadInjectJS();
    }
    
    protected void loadInjectJS() {
        mWebView.loadUrl(getJs("inject.js"));
    }
    
    protected void doSomething() {
        callJavascriptFunc("doSomething", "");
    }
    
    protected void injectTakePictureOnClick() {
        callJavascriptFunc("injectTakePictureOnClick", "");
    }
    
    protected void callJavascriptFunc(final String function, final String args) {
        
        runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                // Wrapping in a try to catch any exceptions in case this tries to 
                // run after the WebView has been destroyed
                try {
                    mWebView.loadUrl(String.format("javascript:%s('%s');", function, args));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private class MyWebChromeClient extends WebChromeClient {
        
        // There are a bunch of methods you can override here, i.e.:
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);

            //  Do something with it
        }
    }
    
    private class MyWebViewClient extends WebViewClient {
        
        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            
            // Handle error
            Toast.makeText(MainActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
        }
        
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Page finished, now we'll do something", Toast.LENGTH_SHORT).show();
            doSomething();
            injectTakePictureOnClick();
        }
    }
    
    protected final class JSBridge {
        
        // Add javascript callbacks here - the javascript file is in assets/inject.js
        @JavascriptInterface
        public void onDidSomething() {
            runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "We did something", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @JavascriptInterface
        public void onLaunchCamera() {
            runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Launching camera", Toast.LENGTH_SHORT).show();
                    launchCamera();
                }
            });
        }
    }
    
    // Get our javascript files from assets
    protected String getJs(String fileName) {
        InputStream is;
        try {
            is = getResources().getAssets().open(fileName);
            String s = FileUtilities.convertStreamToString(is);
            return "javascript:" + s;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void onBackPressed() {
        
        // Handle back in browser history
        if (!mWebView.getUrl().equals(mWebView.getOriginalUrl())) {
            mWebView.goBack();
        } else {
            // This wants to finish the activity
            super.onBackPressed();
        }
    }
    
    protected void launchCamera() {
        // see http://stackoverflow.com/questions/16799818/open-camera-using-intent

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_CAMERA_IMAGE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        switch (requestCode) {
            case PICK_CAMERA_IMAGE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Picture taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        
    }

}
