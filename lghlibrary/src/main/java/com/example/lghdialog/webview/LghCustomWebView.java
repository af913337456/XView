package com.example.lghdialog.webview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2016/5/27.
 */
public class LghCustomWebView extends WebView implements NestedScrollingChild {
    private WebSettings settings;
    private String Url;
    private ProgressBar progressBar;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private FrameLayout videoView;
    private View customView;
    private Context context;
    private View progressLayout;
    private int progressId;
    private WebChromeClient chromeClient;
    private ScreenChangeListener listener;
    private String loadType="GET";
    public static final String POST="POST";
    public static final String GET="GET";
    //private RequestParams params;

    private NestedScrollingChildHelper mChildHelper;
    private int downY;
    private final int[] mScrollOffset=new int[2];
    private final int[] mScrollConsumed=new int[2];
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private VelocityTracker mVelocityTracker;

    public interface ScreenChangeListener{
        public void landscape();
        public void portrait();
    }

    public interface WebViewTitleReceiver{
        public void getTitle(String title);
    }

    private WebViewTitleReceiver receiver;

    public LghCustomWebView withWebViewTitleReceiver(WebViewTitleReceiver receiver){
        this.receiver=receiver;
        return this;
    }

//    public CustomWebView withParams(RequestParams params){
//        this.params=params;
//        return this;
//    }

    public LghCustomWebView withURL(String url){
        this.Url=url;
        return this;
    }

    public LghCustomWebView startLoad(){
        if (Url!=null){
            switch (loadType){
                case POST:
//                    if (params!=null){
//                        String postData=params.toString();
//                        this.postUrl(Url, EncodingUtils.getBytes(postData,"utf-8"));
//                    }
                    break;
                case GET:
                    this.loadUrl(Url);
                    break;
            }
        }
        return this;
    }


    private void isSettingNull(){
        if (settings==null){
            settings=this.getSettings();
        }
    }
    public LghCustomWebView withScreenChangeListener(ScreenChangeListener listener){
        this.listener=listener;
        return this;
    }
    //支持JavaScript
    public LghCustomWebView withJavaScriptEnabled(boolean isEnabled){
        isSettingNull();
        settings.setJavaScriptEnabled(isEnabled);
        return this;
    }

    public LghCustomWebView withLoadType(String type){
        this.loadType=type;
        return this;
    }

    public LghCustomWebView withJavaScriptCanOpenWindowsAutomatically(boolean isEnabled){
        isSettingNull();
        settings.setJavaScriptCanOpenWindowsAutomatically(isEnabled);
        return this;
    }
    //使页面自适应屏幕
    public LghCustomWebView withUseWideViewPort(boolean isEnabled){
        isSettingNull();
        settings.setUseWideViewPort(isEnabled);
        return this;
    }
    //缩放至屏幕的大小
    public LghCustomWebView withLoadWithOverviewMode(boolean isEnabled){
        isSettingNull();
        settings.setLoadWithOverviewMode(isEnabled);
        return this;
    }
    //支持自动加载图片
    public LghCustomWebView withLoadsImagesAutomatically(boolean isEnabled){
        isSettingNull();
        settings.setLoadsImagesAutomatically(isEnabled);
        return this;
    }

    public LghCustomWebView withUserAgent(String userAgent){
        isSettingNull();
        settings.setUserAgentString(settings.getUserAgentString()+userAgent);
        return this;
    }

    public LghCustomWebView withDomStorageEnabled(boolean isEnabled){
        isSettingNull();
        settings.setDomStorageEnabled(isEnabled);
        return this;
    }

    public LghCustomWebView withSaveFormData(boolean isEnabled){
        isSettingNull();
        settings.setSaveFormData(isEnabled);
        return this;
    }

    public LghCustomWebView withLoadUrlType(){
        isSettingNull();

        settings.setDefaultTextEncodingName("gbk");
        return this;
    }

    public LghCustomWebView(Context context) {
        this(context,null);
    }

    public LghCustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        mChildHelper=new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        ViewConfiguration configuration= ViewConfiguration.get(context);
        mMinimumVelocity=configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity=configuration.getScaledMaximumFlingVelocity();
    }

    public LghCustomWebView withProgressView(ProgressBar progressView){
        this.progressBar=progressView;
        return this;
    }

    public LghCustomWebView withWebviewChromeClient(){
        this.chromeClient=new CustomWebViewChromeClient();
        this.setWebChromeClient(this.chromeClient);
        return this;
    }

    public LghCustomWebView withWebviewChromeClient(WebChromeClient client){
        this.setWebChromeClient(client);
        return this;
    }

    public LghCustomWebView withWebViewClient(){
        this.setWebViewClient(new CustomWebViewClient());
        return this;
    }

    public LghCustomWebView withWebViewClient(WebViewClient client){
        this.setWebViewClient(client);
        return this;
    }

    public LghCustomWebView withVideoView(FrameLayout videoView){
        this.videoView=videoView;
        return this;
    }

    public LghCustomWebView withVideoProgressLayoutId(int id){
        this.progressId=id;
        return this;
    }

    public class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setProgress(0);
            progressBar.setVisibility(GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(VISIBLE);
            super.onPageStarted(view,url,favicon);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            receiver.getTitle("");
        }
    }
    public class CustomWebViewChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressBar!=null){
                progressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (customView!=null){
                callback.onCustomViewHidden();
                return;
            }
            customView=view;
            customViewCallback=callback;
            videoView.addView(view);
            videoView.setVisibility(VISIBLE);
            if (listener!=null){
                listener.landscape();
            }
            setFullScreen();
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (receiver!=null){
                receiver.getTitle(title);
            }
        }

        @Override
        public void onHideCustomView() {
            if (customView==null){
                return;
            }
            if (listener!=null){
                listener.portrait();
            }
            exitFullScreen();
            customView.setVisibility(GONE);
            videoView.removeView(customView);
            customView=null;
            videoView.setVisibility(GONE);
            customViewCallback.onCustomViewHidden();
        }

        @Override
        public View getVideoLoadingProgressView() {
            if (progressLayout==null){
                LayoutInflater inflater= LayoutInflater.from(context);
                progressLayout=inflater.inflate(progressId,null);
            }
            return progressLayout;
        }
    }
    private void setFullScreen(){
        ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    private void exitFullScreen(){
        WindowManager.LayoutParams attrs=((Activity)context).getWindow().getAttributes();
        attrs.flags &=(~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity)context).getWindow().setAttributes(attrs);
        ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public boolean WebViewGoBack(){
        if (customView!=null){
            chromeClient.onHideCustomView();
            return true;
        }else {
            if (this.canGoBack()){
                this.goBack();
            }else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        boolean eventAddedToVelocityTracker=false;
        final int action= MotionEventCompat.getActionMasked(event);

        if (mVelocityTracker==null){
            mVelocityTracker= VelocityTracker.obtain();
        }
        switch (action){
            case MotionEvent.ACTION_DOWN:
                downY= (int) event.getRawY();
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY= (int) event.getRawY();
                int dy=-(moveY-downY);
                downY=moveY;
                if (dispatchNestedPreScroll(0,dy,mScrollConsumed,mScrollOffset)){
                    dy-=mScrollConsumed[1];
                    scrollBy(0,dy);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.addMovement(event);
                eventAddedToVelocityTracker=true;
                mVelocityTracker.computeCurrentVelocity(1000,mMaximumVelocity);
                int mScrollPointerId= MotionEventCompat.getPointerId(event, MotionEventCompat.getActionIndex(event));
                float vY=-VelocityTrackerCompat.getYVelocity(mVelocityTracker,mScrollPointerId);
                if (Math.abs(vY)>mMinimumVelocity&&!dispatchNestedPreFling(0,vY)){
                    dispatchNestedFling(0,vY,true);
                }
                resetTouch();
                break;
        }
        if (!eventAddedToVelocityTracker){
            mVelocityTracker.addMovement(event);
        }
        return true;
    }

    private void resetTouch(){
        if (mVelocityTracker!=null){
            mVelocityTracker.clear();
        }
        stopNestedScroll();
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
