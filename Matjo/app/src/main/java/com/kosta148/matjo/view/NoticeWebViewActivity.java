package com.kosta148.matjo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kosta148.matjo.R;

/**
 * Created by kota on 2017-06-17.
 */

public class NoticeWebViewActivity extends AppCompatActivity {
    Context context;
    WebView noticewebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        context = getApplicationContext();

        noticewebView = (WebView)findViewById(R.id.noticeWebView);
        //자바스트립트 사용 셋팅
        noticewebView.getSettings().setLoadWithOverviewMode(true); // 웹뷰에서 페이지가 확대되는 문제해결
        noticewebView.getSettings().setUseWideViewPort(true);
        noticewebView.setInitialScale(1); // 기기별 화면사이트에 맞게 조절
        noticewebView.setWebViewClient(new WebViewClientHandler());
        noticewebView.getSettings().setJavaScriptEnabled(true);
        noticewebView.loadUrl("http://ldh66210.cafe24.com/notice/selectNoticeListMobile.do");


    }

    class WebViewClientHandler extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }
        @Override
        public void onPageFinished(WebView view, String url) {
        }
    }
}
