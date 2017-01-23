package com.example.lghdialog.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lghdialog.R;


/**
 * Created by lzq on 2017/1/8.
 */

public class LghWebViewActivity extends Activity {

    public static final String SCHEMA="com.example.lghdialog://message_private_url";
    public static final String PARAM_UID="uid";
    private static final Uri PROFILE_URI= Uri.parse(SCHEMA);
    private String url;

    TextView titleTv;
    LghCustomWebView webView;
    ProgressBar progressBar;
    LghErrorEmptyLayout lghErrorEmptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lgh_webview_layout);
        initView();
        initData();
    }

    private void initView() {
        titleTv = (TextView) findViewById(R.id.title);

        webView = (LghCustomWebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        lghErrorEmptyLayout = (LghErrorEmptyLayout) findViewById(R.id.error);

        webView.withProgressView(progressBar)
                .withWebviewChromeClient()
                .withJavaScriptEnabled(true)
                .withWebViewClient()
                .withWebViewClient(webView.new CustomWebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        lghErrorEmptyLayout.setLayoutType(LghErrorEmptyLayout.HIDE_LAYOUT);
                    }
                })
                .withWebViewTitleReceiver(new LghCustomWebView.WebViewTitleReceiver() {
                    @Override
                    public void getTitle(String title) {
                        /** 自定义标题 */
                        String title1 = getIntent().getStringExtra("title");
                        if(title1!=null){
                            titleTv.setText(title1);
                        }else{
                            titleTv.setText(title);
                        }
                        titleTv.setSelected(true);
                    }
                })
                .withLoadUrlType();
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()== KeyEvent.ACTION_DOWN){
                    if (keyCode== KeyEvent.KEYCODE_BACK){
                        if (webView.canGoBack()){
                            webView.goBack();
                        }else {
                            finish();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initData() {
        extraUidFromUri();
//        /** 聊天页面跳进来 */
//        if(getIntent().getStringExtra("illUrl")!=null){
//            url = getIntent().getStringExtra("illUrl");
//        }
        webView.withURL(url);
        webView.startLoad();
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    private void extraUidFromUri(){
        Uri uri=getIntent().getData();
        if (uri!=null&&PROFILE_URI.getScheme().equals(uri.getScheme())){
            url=uri.getQueryParameter(PARAM_UID);
        }
        if (!TextUtils.isEmpty(url)){
            if (url.indexOf("www")==0){
                url="http://"+url;
            }else if (url.indexOf("https")==0){
                String bUid=url.substring(5,url.length());
                url="http"+bUid;
            }
        }else {
            url="";
        }
        Log.d("LghWebViewActivity","url="+url);
    }
}
