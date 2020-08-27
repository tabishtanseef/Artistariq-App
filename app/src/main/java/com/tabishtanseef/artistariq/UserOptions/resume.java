package com.tabishtanseef.artistariq.UserOptions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tabishtanseef.artistariq.R;
import com.github.barteksc.pdfviewer.PDFView;


public class resume extends Fragment {

    WebView webView;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resume, container, false);
        //webView = (WebView)view.findViewById(R.id.mywebview);
        //webView.setWebViewClient(new WebViewClient());
        //webView.getSettings().setJavaScriptEnabled(true);
        //progressBar = view.findViewById(R.id.progress_bar);

        /*progressDialog = new ProgressDialog();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Artistariq - Architect & Artist");*/
        PDFView pdfView = view.findViewById(R.id.pdfView);
        pdfView.fromAsset("Tariq Tanseef Resume.pdf").load();
        /*String pdf = "https://www.artistariq.com/Tariq%20Tanseef%20Resume.pdf";
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result){
                return super.onJsAlert(view, url, message, result);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });*/
        return view;
    }
}